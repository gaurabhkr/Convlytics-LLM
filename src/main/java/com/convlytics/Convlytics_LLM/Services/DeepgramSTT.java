package com.convlytics.Convlytics_LLM.Services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class DeepgramSTT {

	private final String API_KEY;
	private final String BASE_URL;
	private final WebClient webClient;
	private final ObjectMapper objectmapper;
	
	public DeepgramSTT(WebClient.Builder webclientbuilder , @Value("${deepgram.api-key}")String apikey,@Value("${deepgram.base-url}") String baseurl) {
		this.API_KEY=apikey; 
		this.BASE_URL=baseurl.replace("speak","listen");
		this.webClient=webclientbuilder.baseUrl(this.BASE_URL)
									   .defaultHeader("Authorization","Token "+API_KEY)
									   .build();
		this.objectmapper=new ObjectMapper();
				
		System.out.println("API KEY: "+API_KEY +"BASE URL:"+BASE_URL);
	}
	
	public JsonNode transcribeaudio(MultipartFile file) {
		try {
			byte[] audioData=file.getBytes();
			
			String response=webClient.post()
					.uri(UriBuilder-> UriBuilder
									.queryParam("model","nova-3")
									.queryParam("language","en-IN")
									.build())
					.header("Content-Type", "audio/mp3")
					.body(BodyInserters.fromValue(audioData))
					.retrieve()
					.bodyToMono(String.class)
					.block();
			//Coverting Response into JSON
			JsonNode responsejson=objectmapper.readTree(response);
			return responsejson;
		}
		catch(Exception e) {e.printStackTrace(); return null;}
	}
	
}
