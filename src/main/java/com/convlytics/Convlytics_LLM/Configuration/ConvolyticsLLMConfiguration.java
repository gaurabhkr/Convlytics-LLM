package com.convlytics.Convlytics_LLM.Configuration;

import org.springframework.ai.model.openai.autoconfigure.OpenAiConnectionProperties;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.speech_to_text.v1.SpeechToText;
import com.ibm.watson.text_to_speech.v1.TextToSpeech;

@Configuration
public class ConvolyticsLLMConfiguration {
		
	private String OPENAI_API_KEY;
	private String OPENAI_BASE_URL;
	
	
	public ConvolyticsLLMConfiguration(OpenAiConnectionProperties  con) {
		this.OPENAI_API_KEY=con.getApiKey(); this.OPENAI_BASE_URL=con.getBaseUrl();
	}
	
	@Bean
	@Qualifier("llmchatmodel")
	public OpenAiChatModel llmchatmodel() {
		var options=OpenAiChatOptions.builder().model("nvidia/nemotron-3-nano-30b-a3b:free").build();
		var openaiapi=OpenAiApi.builder().apiKey(OPENAI_API_KEY).baseUrl(OPENAI_BASE_URL).build();
		return  OpenAiChatModel.builder()
				.defaultOptions(options).openAiApi(openaiapi)
				.build(); 
	}
	
	
}
