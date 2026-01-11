package com.convlytics.Convlytics_LLM.Services;


import java.util.Collections; 
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class DeepgramTTS {
	
	private final String API_KEY;
	private final String TTS_BASE_URL;
	private final WebClient webClient;
	
	//Initillzing webClient for tts 
	public DeepgramTTS(WebClient.Builder webclientBuilder,@Value("${deepgram.api-key}") String apikey,@Value("${deepgram.base-url}") String baseurl) {
		
		this.API_KEY=apikey;this.TTS_BASE_URL=baseurl;
		System.out.println(baseurl+"API:"+apikey);
		this.webClient=webclientBuilder
						.baseUrl(TTS_BASE_URL)
						.defaultHeader(HttpHeaders.AUTHORIZATION, "Token "+API_KEY)
						.build();
		
	}
	
	public Mono<byte[]> synthesizeAudio(String text,String model) {
		//Request Body need JSON Object with text field
		Map<String, String> req=Collections.singletonMap("text", text);
		
		try {
		return webClient.post()
		.uri(UriBuilder->UriBuilder
				.queryParam("model","aura-angus-en")
				.queryParam("encoding","mp3")
				.build())
		.contentType(MediaType.APPLICATION_JSON)
		.body(BodyInserters.fromValue(req))
		.retrieve().bodyToMono(byte[].class).doOnError(Error->System.out.println(Error.getMessage()));
		}catch(Exception e) {System.out.println(e.getMessage());return Mono.error(e);}
	}
			
	
}