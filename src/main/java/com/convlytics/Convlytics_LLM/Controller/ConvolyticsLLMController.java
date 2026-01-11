package com.convlytics.Convlytics_LLM.Controller;

import java.util.ArrayList; 
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.convlytics.Convlytics_LLM.Services.DeepgramSTT;
import com.convlytics.Convlytics_LLM.Services.DeepgramTTS;
import com.fasterxml.jackson.databind.JsonNode;
import com.vladsch.flexmark.ext.emoji.EmojiExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;

import reactor.core.publisher.Mono;


@Controller
@RequestMapping("/api")
public class ConvolyticsLLMController {

	private final ChatClient LLMChatClient;
	private final DeepgramSTT speechtotext;
	private final DeepgramTTS texttospeech;
	
	private final Parser parser;
	private final HtmlRenderer renderer;
	private final MutableDataSet options;
	
	private String Model;
	//We are not using array due to data redundancy
	private HashSet<String> low;
	private HashSet<String> mid;
	private int totalword;
	private String ConversationTrascription;
	
	public ConvolyticsLLMController(@Qualifier("llmchatmodel") OpenAiChatModel llmchatmodel,DeepgramTTS deepgramtts,DeepgramSTT deepgramstt) {
		this.LLMChatClient=ChatClient.create(llmchatmodel);
		this.texttospeech=deepgramtts;
		this.speechtotext=deepgramstt;
		this.Model="aura-2-zeus-en";
		
		this.options=new MutableDataSet();
		this.options.set(Parser.EXTENSIONS,Arrays.asList(TablesExtension.create(),EmojiExtension.create()));
		this.parser=Parser.builder(options).build();
		this.renderer=HtmlRenderer.builder(options).build();
		
		
		this.totalword=0;
		this.ConversationTrascription="";
		this.low=new HashSet<>();this.mid=new HashSet<>();
	}
	
	
	@GetMapping("/llm/{message}")
	public ResponseEntity<String> llmresponse(@PathVariable String message) {
		String response;
		response=LLMChatClient.prompt(message).call().content();
		
		response=response.replaceAll( "([*_`\\[\\](){}+\\-.!#])"," ");
		System.out.println("llmresponse");
		userconversation(message); 
		llmconversation(response);
		
		return ResponseEntity.ok(response);
	}
		
	
	//Need to req has model and text
	@PostMapping("/text-to-speech")
	public Mono<ResponseEntity<byte[]>> texttospeech(@RequestParam("text")  String text){
		
		System.out.println("Text to speech");
		return texttospeech.synthesizeAudio(text, this.Model)
							.map(audioBytes -> ResponseEntity.ok()							
							.header(HttpHeaders.CONTENT_TYPE,MediaType.APPLICATION_OCTET_STREAM_VALUE)
							.header(HttpHeaders.CONTENT_DISPOSITION ,"attachment; filename=\"speech.mp3\"")
							.body(audioBytes));
									
	}
	
	
	@PostMapping("/speech-to-text")
	public ResponseEntity<String> speechtotext(@RequestParam("audio") MultipartFile file){
		//orginal Json Node
		
		JsonNode responsejson=speechtotext.transcribeaudio(file);
		JsonNode response=responsejson.path("results")
									.path("channels").path(0)
									.path("alternatives").path(0);
		System.out.println(response.path("transcript").asText());
		try{extractwordconfidence(response);}catch(Exception e){};
		
		return ResponseEntity.ok(response.path("transcript").asText());
		
	}
	
	@PostMapping("/llm-conversation")
	private  Mono<ResponseEntity<byte[]>> llmuserconversation(@RequestParam("user-audio") MultipartFile useraudio) {
		
		String speechresponse;	
		String llmresponse;
		
		try{speechresponse=speechtotext(useraudio).getBody();
			llmresponse=llmresponse(speechresponse).getBody(); 
			return texttospeech(llmresponse);}
		
		catch(Exception e){return texttospeech("Is your device working  ahh, You didn't give any response ");}
	}
	
	
	//Give llm response before file clear
	@GetMapping("/llm-final-trascription")
	private ResponseEntity<String> coversationtrascriptionresponse(){
		String preprompt="Reponse must not contain emoji, table\n"+
						 "Task 1:Analyze Grammer of User in Coversation\n"+
						 "Task 2:Show where user Grammer is wrong, suggest improvement points in concise "
						 + "and Give Feedback as interviewer to user by analyzing coversation in last\n";
		
		String response=LLMChatClient.prompt(preprompt+"Conversation:\n"+this.ConversationTrascription).call().content();
		
		response=this.renderer.render(this.parser.parse(response));
		System.out.println("llm-final:"+response);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/model/{model}")
	private void selectmodel(@PathVariable String model) {
		this.Model=model;
	}

	
	//When meeting will over before clear file(return list<string> of lowwrods
	@GetMapping("/low-words")
	private ResponseEntity<List<String>> LowScoreWords(){
		
		List<String> LowscoreWords=new ArrayList<>();
		for(var it:low) {LowscoreWords.add(it);}
		System.out.println("api/low-words: Working");
		return ResponseEntity.ok(LowscoreWords);
	}
	
	//When meeting will over before clear file(return list<string> of midwords)
	@GetMapping("/mid-words")
	private ResponseEntity<List<String>> HighScoreWords(){
		
		List<String> HighScoreWords=new ArrayList<>();
		for(var it:mid) {HighScoreWords.add(it);}
		System.out.println("/api/mid-words: Working");
		return ResponseEntity.ok(HighScoreWords);
	}
	
	
	
	//When meeting will over before file cleared(return int wordscore)
	@GetMapping("/word-score")
	private ResponseEntity<Integer> wordscore() {
		int a;  
		if(this.totalword==0) {return ResponseEntity.ok(null);}
		a=(this.low.size()+this.mid.size())/this.totalword;   a=(1-a)*100;
		return ResponseEntity.ok(a);
	}
	
	
	//When meeting will over(used to restart or end meeting)
	@GetMapping("/files/clear")
	private ResponseEntity<String> clear() {
		this.mid.clear();	this.low.clear(); this.totalword=0; this.ConversationTrascription="";
		System.out.println("File Cleared");
		LLMChatClient.prompt(preprompt()).call().content();
		return ResponseEntity.ok("File Clear");
	}
	
	
	//give Conversation Transcription used before file clear
	@GetMapping("/conversation-transcription")
	private ResponseEntity<String> conversationtranscriptio(){
		System.out.println("/conversation-transcription: Working");
		return ResponseEntity.ok(this.ConversationTrascription);
	}
	
	@GetMapping("/total-word")
	private ResponseEntity<Integer> totalword(){
		return ResponseEntity.ok(this.totalword);
	}
	
	private void extractwordconfidence(JsonNode response) {
		Double score; String word="";
		response=response.path("words");
		
		for(JsonNode a: response) {
			this.totalword++;
			word=a.path("word").asText();
			score=a.path("confidence").asDouble();	
			if(score<0.6) {this.low.add(word);}
			if(score>=0.6 && score<=0.8) {this.mid.add(word);}
		}
		System.out.println("Word Score: Working");
		
	}
	
	private void userconversation(String message) {
		this.ConversationTrascription=this.ConversationTrascription+"User:"+message+"\n";
	}
	
	private void llmconversation(String message) {
		this.ConversationTrascription=this.ConversationTrascription+"Interviewer:"+message+"\n";
	}
	
	private String preprompt() {
		return "PrePromt: Your Role is an Professional Interviewer and your taking interview of User."
				+ "If User will ask something your reply should concise whether you asking or answering.";
	}
}
