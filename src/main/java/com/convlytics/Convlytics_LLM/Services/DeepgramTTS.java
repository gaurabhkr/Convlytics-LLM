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
//<!DOCTYPE html>
//<html lang="en">
//<head>
//    <meta charset="UTF-8">
//    <meta name="viewport" content="width=device-width, initial-scale=1.0">
//    <title>AI Interview Assistant</title>
//    <script src="https://cdn.jsdelivr.net/npm/@tailwindcss/browser@4"></script>
//    <style>
//        * {
//            margin: 0;
//            padding: 0;
//            box-sizing: border-box;
//        }
//
//        body {
//            background: #0a0a0f;
//            min-height: 100vh;
//            overflow-x: hidden;
//        }
//
//        /* Animated gradient background */
//        .bg-gradient-mesh {
//            position: fixed;
//            inset: 0;
//            z-index: 0;
//            overflow: hidden;
//        }
//
//        .gradient-orb {
//            position: absolute;
//            border-radius: 50%;
//            filter: blur(80px);
//            opacity: 0.6;
//            animation: float 20s ease-in-out infinite;
//        }
//
//        .orb-1 {
//            width: 600px;
//            height: 600px;
//            background: radial-gradient(circle, #7c3aed 0%, transparent 70%);
//            top: -200px;
//            left: -100px;
//            animation-delay: 0s;
//        }
//
//        .orb-2 {
//            width: 500px;
//            height: 500px;
//            background: radial-gradient(circle, #ec4899 0%, transparent 70%);
//            top: 50%;
//            right: -150px;
//            animation-delay: -5s;
//            animation-duration: 25s;
//        }
//
//        .orb-3 {
//            width: 400px;
//            height: 400px;
//            background: radial-gradient(circle, #06b6d4 0%, transparent 70%);
//            bottom: -100px;
//            left: 30%;
//            animation-delay: -10s;
//            animation-duration: 22s;
//        }
//
//        .orb-4 {
//            width: 350px;
//            height: 350px;
//            background: radial-gradient(circle, #8b5cf6 0%, transparent 70%);
//            top: 40%;
//            left: 10%;
//            animation-delay: -7s;
//            animation-duration: 18s;
//        }
//
//        .orb-5 {
//            width: 300px;
//            height: 300px;
//            background: radial-gradient(circle, #f43f5e 0%, transparent 70%);
//            bottom: 20%;
//            right: 20%;
//            animation-delay: -3s;
//            animation-duration: 23s;
//        }
//
//        @keyframes float {
//            0%, 100% { transform: translate(0, 0) scale(1); }
//            25% { transform: translate(50px, -50px) scale(1.1); }
//            50% { transform: translate(-30px, 30px) scale(0.95); }
//            75% { transform: translate(-50px, -20px) scale(1.05); }
//        }
//
//        /* Grid overlay */
//        .grid-overlay {
//            position: fixed;
//            inset: 0;
//            background-image: 
//                linear-gradient(rgba(255,255,255,0.02) 1px, transparent 1px),
//                linear-gradient(90deg, rgba(255,255,255,0.02) 1px, transparent 1px);
//            background-size: 50px 50px;
//            z-index: 1;
//        }
//
//        /* Noise texture overlay */
//        .noise-overlay {
//            position: fixed;
//            inset: 0;
//            opacity: 0.03;
//            z-index: 2;
//            background-image: url("data:image/svg+xml,%3Csvg viewBox='0 0 256 256' xmlns='http://www.w3.org/2000/svg'%3E%3Cfilter id='noise'%3E%3CfeTurbulence type='fractalNoise' baseFrequency='0.9' numOctaves='4' stitchTiles='stitch'/%3E%3C/filter%3E%3Crect width='100%25' height='100%25' filter='url(%23noise)'/%3E%3C/svg%3E");
//        }
//
//        .content-wrapper {
//            position: relative;
//            z-index: 10;
//        }
//
//        /* ============================================
//           IMPROVED HEADER BUTTONS
//           ============================================ */
//        .header-btn-group {
//            display: flex;
//            gap: 12px;
//            align-items: center;
//        }
//
//        .header-btn {
//            position: relative;
//            display: flex;
//            align-items: center;
//            gap: 6px;
//            padding: 8px 12px;
//            border-radius: 12px;
//            font-weight: 600;
//            font-size: 12px;
//            letter-spacing: 0.3px;
//            cursor: pointer;
//            transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
//            overflow: hidden;
//            border: none;
//            outline: none;
//        }
//
//        @media (min-width: 640px) {
//            .header-btn {
//                gap: 10px;
//                padding: 12px 20px;
//                border-radius: 16px;
//                font-size: 14px;
//            }
//        }
//
//        /* New Meeting Button - Glass Card Style */
//        .btn-new-meeting {
//            background: rgba(16, 185, 129, 0.15);
//            backdrop-filter: blur(12px);
//            -webkit-backdrop-filter: blur(12px);
//            border: 1px solid rgba(16, 185, 129, 0.4);
//            color: #10b981;
//            box-shadow: 
//                0 8px 32px rgba(16, 185, 129, 0.2),
//                inset 0 1px 0 rgba(255, 255, 255, 0.3),
//                inset 0 -1px 0 rgba(255, 255, 255, 0.1),
//                inset 0 0 20px 5px rgba(16, 185, 129, 0.1);
//        }
//
//        /* Top highlight line */
//        .btn-new-meeting::before {
//            content: '';
//            position: absolute;
//            top: 0;
//            left: 0;
//            right: 0;
//            height: 1px;
//            background: linear-gradient(
//                90deg,
//                transparent,
//                rgba(255, 255, 255, 0.8),
//                transparent
//            );
//            opacity: 0.8;
//            transition: opacity 0.3s ease;
//        }
//
//        /* Left edge highlight */
//        .btn-new-meeting::after {
//            content: '';
//            position: absolute;
//            top: 0;
//            left: 0;
//            width: 1px;
//            height: 100%;
//            background: linear-gradient(
//                180deg,
//                rgba(255, 255, 255, 0.8),
//                transparent,
//                rgba(255, 255, 255, 0.3)
//            );
//            opacity: 0.6;
//        }
//
//        .btn-new-meeting:hover {
//            transform: translateY(-3px) scale(1.02);
//            border-color: rgba(16, 185, 129, 0.7);
//            box-shadow: 
//                0 15px 40px rgba(16, 185, 129, 0.4),
//                0 0 60px rgba(16, 185, 129, 0.2),
//                inset 0 1px 0 rgba(255, 255, 255, 0.5),
//                inset 0 0 30px 10px rgba(16, 185, 129, 0.2);
//            color: #34d399;
//        }
//
//        .btn-new-meeting:hover::before {
//            opacity: 1;
//            background: linear-gradient(
//                90deg,
//                transparent,
//                rgba(255, 255, 255, 1),
//                transparent
//            );
//        }
//
//        .btn-new-meeting:active {
//            transform: translateY(-1px) scale(0.98);
//            box-shadow: 
//                0 5px 20px rgba(16, 185, 129, 0.3),
//                inset 0 1px 0 rgba(255, 255, 255, 0.3),
//                inset 0 0 20px 5px rgba(16, 185, 129, 0.3);
//        }
//
//        .btn-new-meeting .btn-icon {
//            position: relative;
//            z-index: 2;
//            width: 20px;
//            height: 20px;
//            display: flex;
//            align-items: center;
//            justify-content: center;
//            background: rgba(16, 185, 129, 0.3);
//            border-radius: 8px;
//            transition: all 0.3s ease;
//        }
//
//        .btn-new-meeting:hover .btn-icon {
//            background: rgba(16, 185, 129, 0.5);
//            transform: rotate(10deg) scale(1.1);
//        }
//
//        .btn-new-meeting .btn-icon svg {
//            width: 12px;
//            height: 12px;
//            transition: transform 0.3s ease;
//        }
//
//        .btn-new-meeting:hover .btn-icon svg {
//            transform: scale(1.15);
//        }
//
//        .btn-new-meeting .btn-text {
//            position: relative;
//            z-index: 2;
//            background: linear-gradient(135deg, #10b981 0%, #34d399 100%);
//            -webkit-background-clip: text;
//            -webkit-text-fill-color: transparent;
//            background-clip: text;
//        }
//
//        /* Shimmer effect for New Meeting */
//        .btn-new-meeting .shimmer {
//            position: absolute;
//            top: 0;
//            left: -100%;
//            width: 100%;
//            height: 100%;
//            background: linear-gradient(
//                90deg,
//                transparent,
//                rgba(255, 255, 255, 0.2),
//                transparent
//            );
//            animation: analysis-shimmer 3s ease-in-out infinite;
//        }
//
//        .btn-new-meeting:hover .shimmer {
//            animation-duration: 1.5s;
//        }
//
//        /* Floating glow for New Meeting */
//        .btn-new-meeting .btn-glow {
//            position: absolute;
//            width: 60px;
//            height: 60px;
//            background: radial-gradient(circle, rgba(16, 185, 129, 0.6) 0%, transparent 70%);
//            border-radius: 50%;
//            filter: blur(15px);
//            animation: glow-float 4s ease-in-out infinite;
//            pointer-events: none;
//        }
//
//        .btn-new-meeting:hover .btn-glow {
//            animation-duration: 2s;
//        }
//
//        /* Pulse ring for New Meeting */
//        .btn-new-meeting .btn-pulse-ring {
//            position: absolute;
//            inset: -4px;
//            border-radius: 20px;
//            border: 2px solid rgba(16, 185, 129, 0.5);
//            animation: pulse-ring-analysis 2s ease-out infinite;
//            pointer-events: none;
//        }
//
//        /* Sparkles for New Meeting */
//        .btn-new-meeting .sparkle {
//            position: absolute;
//            width: 4px;
//            height: 4px;
//            background: white;
//            border-radius: 50%;
//            pointer-events: none;
//            opacity: 0;
//        }
//
//        .btn-new-meeting:hover .sparkle {
//            animation: sparkle-float 1.5s ease-out infinite;
//        }
//
//        /* End Meeting Button - Glass Card Style */
//        .btn-end-meeting {
//            background: rgba(239, 68, 68, 0.15);
//            backdrop-filter: blur(12px);
//            -webkit-backdrop-filter: blur(12px);
//            border: 1px solid rgba(239, 68, 68, 0.4);
//            color: rgba(239, 68, 68, 0.9);
//            box-shadow: 
//                0 8px 32px rgba(239, 68, 68, 0.2),
//                inset 0 1px 0 rgba(255, 255, 255, 0.3),
//                inset 0 -1px 0 rgba(255, 255, 255, 0.1),
//                inset 0 0 20px 5px rgba(239, 68, 68, 0.1);
//        }
//
//        /* Top highlight line */
//        .btn-end-meeting::before {
//            content: '';
//            position: absolute;
//            top: 0;
//            left: 0;
//            right: 0;
//            height: 1px;
//            background: linear-gradient(
//                90deg,
//                transparent,
//                rgba(255, 255, 255, 0.8),
//                transparent
//            );
//            opacity: 0.8;
//            transition: opacity 0.3s ease;
//        }
//
//        /* Left edge highlight */
//        .btn-end-meeting::after {
//            content: '';
//            position: absolute;
//            top: 0;
//            left: 0;
//            width: 1px;
//            height: 100%;
//            background: linear-gradient(
//                180deg,
//                rgba(255, 255, 255, 0.8),
//                transparent,
//                rgba(255, 255, 255, 0.3)
//            );
//            opacity: 0.6;
//        }
//
//        .btn-end-meeting:hover {
//            transform: translateY(-3px) scale(1.02);
//            border-color: rgba(239, 68, 68, 0.7);
//            box-shadow: 
//                0 15px 40px rgba(239, 68, 68, 0.4),
//                0 0 60px rgba(239, 68, 68, 0.2),
//                inset 0 1px 0 rgba(255, 255, 255, 0.5),
//                inset 0 0 30px 10px rgba(239, 68, 68, 0.2);
//            color: #f87171;
//        }
//
//        .btn-end-meeting:hover::before {
//            opacity: 1;
//            background: linear-gradient(
//                90deg,
//                transparent,
//                rgba(255, 255, 255, 1),
//                transparent
//            );
//        }
//
//        .btn-end-meeting:active {
//            transform: translateY(-1px) scale(0.98);
//            box-shadow: 
//                0 5px 20px rgba(239, 68, 68, 0.3),
//                inset 0 1px 0 rgba(255, 255, 255, 0.3),
//                inset 0 0 20px 5px rgba(239, 68, 68, 0.3);
//        }
//
//        .btn-end-meeting .btn-icon {
//            position: relative;
//            z-index: 2;
//            width: 20px;
//            height: 20px;
//            display: flex;
//            align-items: center;
//            justify-content: center;
//            background: rgba(239, 68, 68, 0.3);
//            border-radius: 8px;
//            transition: all 0.3s ease;
//        }
//
//        .btn-end-meeting:hover .btn-icon {
//            background: rgba(239, 68, 68, 0.5);
//            transform: rotate(10deg) scale(1.1);
//        }
//
//        .btn-end-meeting .btn-icon svg {
//            width: 12px;
//            height: 12px;
//            transition: transform 0.3s ease;
//        }
//
//        .btn-end-meeting:hover .btn-icon svg {
//            transform: scale(1.15);
//        }
//
//        .btn-end-meeting .btn-text {
//            position: relative;
//            z-index: 2;
//            background: linear-gradient(135deg, #ef4444 0%, #f87171 100%);
//            -webkit-background-clip: text;
//            -webkit-text-fill-color: transparent;
//            background-clip: text;
//        }
//
//        /* Shimmer effect for End Meeting */
//        .btn-end-meeting .shimmer {
//            position: absolute;
//            top: 0;
//            left: -100%;
//            width: 100%;
//            height: 100%;
//            background: linear-gradient(
//                90deg,
//                transparent,
//                rgba(255, 255, 255, 0.2),
//                transparent
//            );
//            animation: analysis-shimmer 3s ease-in-out infinite;
//        }
//
//        .btn-end-meeting:hover .shimmer {
//            animation-duration: 1.5s;
//        }
//
//        /* Floating glow for End Meeting */
//        .btn-end-meeting .btn-glow {
//            position: absolute;
//            width: 60px;
//            height: 60px;
//            background: radial-gradient(circle, rgba(239, 68, 68, 0.6) 0%, transparent 70%);
//            border-radius: 50%;
//            filter: blur(15px);
//            animation: glow-float 4s ease-in-out infinite;
//            pointer-events: none;
//        }
//
//        .btn-end-meeting:hover .btn-glow {
//            animation-duration: 2s;
//        }
//
//        /* Pulse ring for End Meeting */
//        .btn-end-meeting .btn-pulse-ring {
//            position: absolute;
//            inset: -4px;
//            border-radius: 20px;
//            border: 2px solid rgba(239, 68, 68, 0.5);
//            animation: pulse-ring-analysis 2s ease-out infinite;
//            pointer-events: none;
//        }
//
//        /* Sparkles for End Meeting */
//        .btn-end-meeting .sparkle {
//            position: absolute;
//            width: 4px;
//            height: 4px;
//            background: white;
//            border-radius: 50%;
//            pointer-events: none;
//            opacity: 0;
//        }
//
//        .btn-end-meeting:hover .sparkle {
//            animation: sparkle-float 1.5s ease-out infinite;
//        }
//
//        /* Pulse indicator for live meeting */
//        .live-indicator {
//            position: relative;
//            display: flex;
//            align-items: center;
//            gap: 4px;
//            padding: 6px 10px;
//            background: rgba(16, 185, 129, 0.1);
//            border: 1px solid rgba(16, 185, 129, 0.3);
//            border-radius: 10px;
//            color: #10b981;
//            font-size: 10px;
//            font-weight: 600;
//            letter-spacing: 0.5px;
//            text-transform: uppercase;
//        }
//
//        @media (min-width: 640px) {
//            .live-indicator {
//                gap: 8px;
//                padding: 8px 14px;
//                border-radius: 12px;
//                font-size: 12px;
//            }
//        }
//
//        .live-dot {
//            width: 8px;
//            height: 8px;
//            background: #10b981;
//            border-radius: 50%;
//            animation: live-pulse 2s ease-in-out infinite;
//            box-shadow: 0 0 10px rgba(16, 185, 129, 0.5);
//        }
//
//        @keyframes live-pulse {
//            0%, 100% { 
//                opacity: 1;
//                transform: scale(1);
//            }
//            50% { 
//                opacity: 0.5;
//                transform: scale(1.2);
//            }
//        }
//
//        /* Divider */
//        .btn-divider {
//            width: 1px;
//            height: 32px;
//            background: linear-gradient(
//                to bottom,
//                transparent,
//                rgba(255, 255, 255, 0.1),
//                transparent
//            );
//        }
//
//        /* ============================================
//           NAVIGATION STYLES
//           ============================================ */
//        
//        /* Navigation Links */
//        .nav-link {
//            position: relative;
//            padding: 6px 12px;
//            color: rgba(255, 255, 255, 0.6);
//            font-size: 13px;
//            font-weight: 500;
//            border-radius: 10px;
//            transition: all 0.3s ease;
//            text-decoration: none;
//            white-space: nowrap;
//        }
//
//        @media (min-width: 640px) {
//            .nav-link {
//                padding: 8px 16px;
//                font-size: 14px;
//            }
//        }
//
//        .nav-link:hover {
//            color: rgba(255, 255, 255, 0.9);
//        }
//
//        .nav-link.active {
//            color: #a78bfa;
//        }
//
//        .nav-link::after {
//            content: '';
//            position: absolute;
//            bottom: 0;
//            left: 50%;
//            transform: translateX(-50%);
//            width: 0;
//            height: 2px;
//            background: linear-gradient(90deg, #8b5cf6, #ec4899);
//            border-radius: 1px;
//            transition: width 0.3s ease;
//        }
//
//        .nav-link:hover::after,
//        .nav-link.active::after {
//            width: 60%;
//        }
//
//        /* Analysis Button - Glass Card Style */
//        .analysis-btn {
//            position: relative;
//            display: flex;
//            align-items: center;
//            gap: 10px;
//            padding: 12px 24px;
//            background: rgba(139, 92, 246, 0.15);
//            backdrop-filter: blur(12px);
//            -webkit-backdrop-filter: blur(12px);
//            color: white;
//            font-size: 14px;
//            font-weight: 600;
//            border: 1px solid rgba(139, 92, 246, 0.4);
//            border-radius: 16px;
//            cursor: pointer;
//            overflow: hidden;
//            transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
//            box-shadow: 
//                0 8px 32px rgba(139, 92, 246, 0.2),
//                inset 0 1px 0 rgba(255, 255, 255, 0.3),
//                inset 0 -1px 0 rgba(255, 255, 255, 0.1),
//                inset 0 0 20px 5px rgba(139, 92, 246, 0.1);
//        }
//
//        /* Top highlight line */
//        .analysis-btn::before {
//            content: '';
//            position: absolute;
//            top: 0;
//            left: 0;
//            right: 0;
//            height: 1px;
//            background: linear-gradient(
//                90deg,
//                transparent,
//                rgba(255, 255, 255, 0.8),
//                transparent
//            );
//            opacity: 0.8;
//            transition: opacity 0.3s ease;
//        }
//
//        /* Left edge highlight */
//        .analysis-btn::after {
//            content: '';
//            position: absolute;
//            top: 0;
//            left: 0;
//            width: 1px;
//            height: 100%;
//            background: linear-gradient(
//                180deg,
//                rgba(255, 255, 255, 0.8),
//                transparent,
//                rgba(255, 255, 255, 0.3)
//            );
//            opacity: 0.6;
//        }
//
//        /* Shimmer sweep animation */
//        .analysis-btn .btn-shimmer {
//            position: absolute;
//            top: 0;
//            left: -100%;
//            width: 100%;
//            height: 100%;
//            background: linear-gradient(
//                90deg,
//                transparent,
//                rgba(255, 255, 255, 0.2),
//                transparent
//            );
//            animation: analysis-shimmer 3s ease-in-out infinite;
//        }
//
//        @keyframes analysis-shimmer {
//            0%, 100% { left: -100%; opacity: 0; }
//            50% { left: 100%; opacity: 1; }
//        }
//
//        /* Floating glow orb animation */
//        .analysis-btn .btn-glow {
//            position: absolute;
//            width: 60px;
//            height: 60px;
//            background: radial-gradient(circle, rgba(139, 92, 246, 0.6) 0%, transparent 70%);
//            border-radius: 50%;
//            filter: blur(15px);
//            animation: glow-float 4s ease-in-out infinite;
//            pointer-events: none;
//        }
//
//        @keyframes glow-float {
//            0%, 100% { 
//                transform: translate(-30px, -30px) scale(1);
//                opacity: 0.5;
//            }
//            25% { 
//                transform: translate(50px, -20px) scale(1.2);
//                opacity: 0.8;
//            }
//            50% { 
//                transform: translate(80px, 10px) scale(1);
//                opacity: 0.6;
//            }
//            75% { 
//                transform: translate(20px, 20px) scale(1.1);
//                opacity: 0.7;
//            }
//        }
//
//        /* Pulse ring animation */
//        .analysis-btn .btn-pulse-ring {
//            position: absolute;
//            inset: -4px;
//            border-radius: 20px;
//            border: 2px solid rgba(139, 92, 246, 0.5);
//            animation: pulse-ring-analysis 2s ease-out infinite;
//            pointer-events: none;
//        }
//
//        @keyframes pulse-ring-analysis {
//            0% { 
//                transform: scale(1);
//                opacity: 0.8;
//            }
//            100% { 
//                transform: scale(1.15);
//                opacity: 0;
//            }
//        }
//
//        /* Icon container */
//        .analysis-btn .btn-icon-wrap {
//            position: relative;
//            z-index: 2;
//            display: flex;
//            align-items: center;
//            justify-content: center;
//            width: 28px;
//            height: 28px;
//            background: rgba(139, 92, 246, 0.3);
//            border-radius: 8px;
//            transition: all 0.3s ease;
//        }
//
//        .analysis-btn .btn-icon-wrap svg {
//            transition: transform 0.3s ease;
//        }
//
//        /* Text */
//        .analysis-btn .btn-text {
//            position: relative;
//            z-index: 2;
//            background: linear-gradient(135deg, #ffffff 0%, #c4b5fd 100%);
//            -webkit-background-clip: text;
//            -webkit-text-fill-color: transparent;
//            background-clip: text;
//        }
//
//        /* Hover effects */
//        .analysis-btn:hover {
//            transform: translateY(-3px) scale(1.02);
//            border-color: rgba(139, 92, 246, 0.7);
//            box-shadow: 
//                0 15px 40px rgba(139, 92, 246, 0.4),
//                0 0 60px rgba(139, 92, 246, 0.2),
//                inset 0 1px 0 rgba(255, 255, 255, 0.5),
//                inset 0 0 30px 10px rgba(139, 92, 246, 0.2);
//        }
//
//        .analysis-btn:hover::before {
//            opacity: 1;
//            background: linear-gradient(
//                90deg,
//                transparent,
//                rgba(255, 255, 255, 1),
//                transparent
//            );
//        }
//
//        .analysis-btn:hover .btn-icon-wrap {
//            background: rgba(139, 92, 246, 0.5);
//            transform: rotate(10deg) scale(1.1);
//        }
//
//        .analysis-btn:hover .btn-icon-wrap svg {
//            transform: scale(1.15);
//        }
//
//        .analysis-btn:hover .btn-shimmer {
//            animation-duration: 1.5s;
//        }
//
//        .analysis-btn:hover .btn-glow {
//            animation-duration: 2s;
//        }
//
//        /* Active/pressed state */
//        .analysis-btn:active {
//            transform: translateY(-1px) scale(0.98);
//            box-shadow: 
//                0 5px 20px rgba(139, 92, 246, 0.3),
//                inset 0 1px 0 rgba(255, 255, 255, 0.3),
//                inset 0 0 20px 5px rgba(139, 92, 246, 0.3);
//        }
//
//        /* Sparkle particles */
//        .analysis-btn .sparkle {
//            position: absolute;
//            width: 4px;
//            height: 4px;
//            background: white;
//            border-radius: 50%;
//            pointer-events: none;
//            opacity: 0;
//        }
//
//        .analysis-btn:hover .sparkle {
//            animation: sparkle-float 1.5s ease-out infinite;
//        }
//
//        .sparkle-1 { top: 20%; left: 10%; animation-delay: 0s; }
//        .sparkle-2 { top: 60%; right: 15%; animation-delay: 0.3s; }
//        .sparkle-3 { bottom: 20%; left: 30%; animation-delay: 0.6s; }
//
//        @keyframes sparkle-float {
//            0% { 
//                transform: translateY(0) scale(0);
//                opacity: 0;
//            }
//            50% { 
//                opacity: 1;
//                transform: translateY(-10px) scale(1);
//            }
//            100% { 
//                transform: translateY(-20px) scale(0);
//                opacity: 0;
//            }
//        }
//
//        /* ============================================
//           AI FLOWING SPHERE STYLES
//           ============================================ */
//        .ai-sphere-container {
//            position: relative;
//            display: flex;
//            flex-direction: column;
//            align-items: center;
//            justify-content: center;
//            margin-bottom: 2rem;
//        }
//
//        .ai-sphere {
//            position: relative;
//            width: 180px;
//            height: 180px;
//            display: flex;
//            align-items: center;
//            justify-content: center;
//        }
//
//        /* Main glow behind the sphere */
//        .sphere-glow {
//            position: absolute;
//            width: 150px;
//            height: 150px;
//            border-radius: 50%;
//            background: radial-gradient(circle, rgba(139, 92, 246, 0.5) 0%, rgba(139, 92, 246, 0.2) 40%, transparent 70%);
//            filter: blur(30px);
//            animation: glow-breathe 4s ease-in-out infinite;
//            transition: all 0.5s ease;
//        }
//
//        .sphere-glow.processing {
//            background: radial-gradient(circle, rgba(251, 191, 36, 0.6) 0%, rgba(251, 191, 36, 0.3) 40%, transparent 70%);
//            animation: glow-breathe-fast 0.5s ease-in-out infinite;
//        }
//
//        .sphere-glow.speaking {
//            background: radial-gradient(circle, rgba(16, 185, 129, 0.5) 0%, rgba(16, 185, 129, 0.2) 40%, transparent 70%);
//            animation: glow-breathe 2s ease-in-out infinite;
//        }
//
//        @keyframes glow-breathe {
//            0%, 100% { transform: scale(1); opacity: 0.7; }
//            50% { transform: scale(1.2); opacity: 1; }
//        }
//
//        @keyframes glow-breathe-fast {
//            0%, 100% { transform: scale(1); opacity: 0.8; }
//            50% { transform: scale(1.3); opacity: 1; }
//        }
//
//        /* Flowing blob layers */
//        .blob-container {
//            position: relative;
//            width: 120px;
//            height: 120px;
//        }
//
//        .blob {
//            position: absolute;
//            inset: 0;
//            border-radius: 50%;
//            filter: blur(1px);
//        }
//
//        /* Outer flowing layer */
//        .blob-outer {
//            background: linear-gradient(135deg, 
//                rgba(139, 92, 246, 0.4) 0%, 
//                rgba(236, 72, 153, 0.4) 25%,
//                rgba(6, 182, 212, 0.4) 50%,
//                rgba(139, 92, 246, 0.4) 75%,
//                rgba(236, 72, 153, 0.4) 100%);
//            animation: morph 8s ease-in-out infinite, rotate 12s linear infinite, color-shift 6s ease-in-out infinite;
//            opacity: 0.8;
//        }
//
//        .blob-outer.processing {
//            background: linear-gradient(135deg, 
//                rgba(251, 191, 36, 0.5) 0%, 
//                rgba(249, 115, 22, 0.5) 25%,
//                rgba(234, 179, 8, 0.5) 50%,
//                rgba(251, 191, 36, 0.5) 75%,
//                rgba(249, 115, 22, 0.5) 100%);
//            animation: morph-fast 1s ease-in-out infinite, rotate-fast 1.5s linear infinite;
//        }
//
//        .blob-outer.speaking {
//            background: linear-gradient(135deg, 
//                rgba(16, 185, 129, 0.5) 0%, 
//                rgba(52, 211, 153, 0.5) 25%,
//                rgba(6, 182, 212, 0.5) 50%,
//                rgba(16, 185, 129, 0.5) 75%,
//                rgba(52, 211, 153, 0.5) 100%);
//            animation: morph 3s ease-in-out infinite, rotate 5s linear infinite;
//        }
//
//        /* Middle flowing layer */
//        .blob-middle {
//            inset: 10px;
//            background: linear-gradient(225deg, 
//                rgba(168, 85, 247, 0.5) 0%, 
//                rgba(217, 70, 239, 0.5) 25%,
//                rgba(139, 92, 246, 0.5) 50%,
//                rgba(168, 85, 247, 0.5) 75%,
//                rgba(217, 70, 239, 0.5) 100%);
//            animation: morph 6s ease-in-out infinite reverse, rotate 10s linear infinite reverse, color-shift 5s ease-in-out infinite reverse;
//            opacity: 0.9;
//        }
//
//        .blob-middle.processing {
//            background: linear-gradient(225deg, 
//                rgba(252, 211, 77, 0.6) 0%, 
//                rgba(251, 146, 60, 0.6) 25%,
//                rgba(251, 191, 36, 0.6) 50%,
//                rgba(252, 211, 77, 0.6) 75%,
//                rgba(251, 146, 60, 0.6) 100%);
//            animation: morph-fast 0.8s ease-in-out infinite reverse, rotate-fast 1.2s linear infinite reverse;
//        }
//
//        .blob-middle.speaking {
//            background: linear-gradient(225deg, 
//                rgba(52, 211, 153, 0.6) 0%, 
//                rgba(110, 231, 183, 0.6) 25%,
//                rgba(16, 185, 129, 0.6) 50%,
//                rgba(52, 211, 153, 0.6) 75%,
//                rgba(110, 231, 183, 0.6) 100%);
//            animation: morph 2.5s ease-in-out infinite reverse, rotate 4s linear infinite reverse;
//        }
//
//        /* Inner core layer */
//        .blob-inner {
//            inset: 25px;
//            background: linear-gradient(45deg, 
//                rgba(196, 181, 253, 0.7) 0%, 
//                rgba(251, 207, 232, 0.7) 25%,
//                rgba(165, 243, 252, 0.7) 50%,
//                rgba(196, 181, 253, 0.7) 75%,
//                rgba(251, 207, 232, 0.7) 100%);
//            animation: morph 4s ease-in-out infinite, rotate 8s linear infinite, color-shift 4s ease-in-out infinite;
//            filter: blur(0px);
//        }
//
//        .blob-inner.processing {
//            background: linear-gradient(45deg, 
//                rgba(254, 240, 138, 0.8) 0%, 
//                rgba(253, 224, 71, 0.8) 25%,
//                rgba(250, 204, 21, 0.8) 50%,
//                rgba(254, 240, 138, 0.8) 75%,
//                rgba(253, 224, 71, 0.8) 100%);
//            animation: morph-fast 0.6s ease-in-out infinite, rotate-fast 1s linear infinite;
//        }
//
//        .blob-inner.speaking {
//            background: linear-gradient(45deg, 
//                rgba(167, 243, 208, 0.8) 0%, 
//                rgba(110, 231, 183, 0.8) 25%,
//                rgba(52, 211, 153, 0.8) 50%,
//                rgba(167, 243, 208, 0.8) 75%,
//                rgba(110, 231, 183, 0.8) 100%);
//            animation: morph 2s ease-in-out infinite, rotate 3s linear infinite;
//        }
//
//        /* Core center - bright point */
//        .blob-core {
//            position: absolute;
//            inset: 40px;
//            border-radius: 50%;
//            background: radial-gradient(circle at 30% 30%, 
//                rgba(255, 255, 255, 0.95) 0%,
//                rgba(255, 255, 255, 0.7) 20%,
//                rgba(196, 181, 253, 0.8) 50%,
//                rgba(139, 92, 246, 0.9) 100%);
//            box-shadow: 
//                0 0 20px rgba(255, 255, 255, 0.5),
//                0 0 40px rgba(139, 92, 246, 0.5),
//                inset 0 0 20px rgba(255, 255, 255, 0.3);
//            animation: pulse-core 3s ease-in-out infinite;
//        }
//
//        .blob-core.processing {
//            background: radial-gradient(circle at 30% 30%, 
//                rgba(255, 255, 255, 0.95) 0%,
//                rgba(255, 255, 255, 0.8) 20%,
//                rgba(253, 224, 71, 0.9) 50%,
//                rgba(251, 191, 36, 1) 100%);
//            box-shadow: 
//                0 0 30px rgba(255, 255, 255, 0.6),
//                0 0 60px rgba(251, 191, 36, 0.7),
//                inset 0 0 20px rgba(255, 255, 255, 0.4);
//            animation: pulse-core-fast 0.4s ease-in-out infinite;
//        }
//
//        .blob-core.speaking {
//            background: radial-gradient(circle at 30% 30%, 
//                rgba(255, 255, 255, 0.95) 0%,
//                rgba(255, 255, 255, 0.7) 20%,
//                rgba(110, 231, 183, 0.8) 50%,
//                rgba(16, 185, 129, 0.9) 100%);
//            box-shadow: 
//                0 0 20px rgba(255, 255, 255, 0.5),
//                0 0 40px rgba(16, 185, 129, 0.5),
//                inset 0 0 20px rgba(255, 255, 255, 0.3);
//            animation: pulse-core 1.5s ease-in-out infinite;
//        }
//
//        /* Floating particles around the sphere */
//        .particle {
//            position: absolute;
//            width: 6px;
//            height: 6px;
//            border-radius: 50%;
//            background: rgba(255, 255, 255, 0.8);
//            box-shadow: 0 0 10px rgba(139, 92, 246, 0.8);
//            animation: float-particle 6s ease-in-out infinite;
//        }
//
//        .particle.processing {
//            box-shadow: 0 0 10px rgba(251, 191, 36, 0.9);
//            animation: float-particle-fast 1s ease-in-out infinite;
//        }
//
//        .particle.speaking {
//            box-shadow: 0 0 10px rgba(16, 185, 129, 0.8);
//            animation: float-particle 3s ease-in-out infinite;
//        }
//
//        .particle-1 { top: 10%; left: 50%; animation-delay: 0s; }
//        .particle-2 { top: 50%; right: 5%; animation-delay: -1s; width: 4px; height: 4px; }
//        .particle-3 { bottom: 10%; left: 50%; animation-delay: -2s; width: 5px; height: 5px; }
//        .particle-4 { top: 50%; left: 5%; animation-delay: -3s; width: 4px; height: 4px; }
//        .particle-5 { top: 20%; right: 15%; animation-delay: -4s; width: 3px; height: 3px; }
//        .particle-6 { bottom: 20%; left: 15%; animation-delay: -5s; width: 3px; height: 3px; }
//
//        @keyframes float-particle {
//            0%, 100% { transform: translateY(0) scale(1); opacity: 0.8; }
//            50% { transform: translateY(-10px) scale(1.2); opacity: 1; }
//        }
//
//        @keyframes float-particle-fast {
//            0%, 100% { transform: translateY(0) scale(1); opacity: 0.9; }
//            50% { transform: translateY(-15px) scale(1.4); opacity: 1; }
//        }
//
//        /* Morphing animation */
//        @keyframes morph {
//            0%, 100% { 
//                border-radius: 60% 40% 30% 70% / 60% 30% 70% 40%;
//                transform: rotate(0deg) scale(1);
//            }
//            25% { 
//                border-radius: 30% 60% 70% 40% / 50% 60% 30% 60%;
//            }
//            50% { 
//                border-radius: 50% 60% 30% 60% / 30% 40% 70% 50%;
//                transform: rotate(180deg) scale(1.05);
//            }
//            75% { 
//                border-radius: 60% 40% 60% 30% / 70% 30% 50% 60%;
//            }
//        }
//
//        @keyframes morph-fast {
//            0%, 100% { 
//                border-radius: 60% 40% 30% 70% / 60% 30% 70% 40%;
//                transform: rotate(0deg) scale(1);
//            }
//            25% { 
//                border-radius: 30% 60% 70% 40% / 50% 60% 30% 60%;
//            }
//            50% { 
//                border-radius: 50% 60% 30% 60% / 30% 40% 70% 50%;
//                transform: rotate(180deg) scale(1.1);
//            }
//            75% { 
//                border-radius: 60% 40% 60% 30% / 70% 30% 50% 60%;
//            }
//        }
//
//        @keyframes rotate {
//            from { transform: rotate(0deg); }
//            to { transform: rotate(360deg); }
//        }
//
//        @keyframes rotate-fast {
//            from { transform: rotate(0deg); }
//            to { transform: rotate(360deg); }
//        }
//
//        @keyframes color-shift {
//            0%, 100% { filter: hue-rotate(0deg) blur(1px); }
//            50% { filter: hue-rotate(30deg) blur(1px); }
//        }
//
//        @keyframes pulse-core {
//            0%, 100% { transform: scale(1); opacity: 0.9; }
//            50% { transform: scale(1.1); opacity: 1; }
//        }
//
//        @keyframes pulse-core-fast {
//            0%, 100% { transform: scale(1); opacity: 0.95; }
//            50% { transform: scale(1.15); opacity: 1; }
//        }
//
//        /* Sphere label */
//        .sphere-label {
//            margin-top: 1.5rem;
//            font-size: 1rem;
//            font-weight: 600;
//            color: rgba(255, 255, 255, 0.7);
//            text-transform: uppercase;
//            letter-spacing: 2px;
//            transition: color 0.5s ease;
//        }
//
//        .sphere-label.processing {
//            color: rgba(251, 191, 36, 0.9);
//        }
//
//        .sphere-label.speaking {
//            color: rgba(16, 185, 129, 0.9);
//        }
//
//        /* ============================================
//           RECORD BUTTON & OTHER STYLES
//           ============================================ */
//        @keyframes pulse-ring {
//            0% { transform: scale(0.8); opacity: 1; }
//            100% { transform: scale(1.5); opacity: 0; }
//        }
//
//        @keyframes wave {
//            0%, 100% { height: 10px; }
//            50% { height: 30px; }
//        }
//
//        @keyframes glow-pulse {
//            0%, 100% { box-shadow: 0 0 30px rgba(139, 92, 246, 0.5), 0 0 60px rgba(139, 92, 246, 0.3), inset 0 0 20px rgba(255,255,255,0.1); }
//            50% { box-shadow: 0 0 50px rgba(139, 92, 246, 0.7), 0 0 100px rgba(139, 92, 246, 0.5), inset 0 0 30px rgba(255,255,255,0.15); }
//        }
//
//        @keyframes glow-pulse-recording {
//            0%, 100% { box-shadow: 0 0 30px rgba(239, 68, 68, 0.6), 0 0 60px rgba(239, 68, 68, 0.4), inset 0 0 20px rgba(255,255,255,0.1); }
//            50% { box-shadow: 0 0 60px rgba(239, 68, 68, 0.8), 0 0 120px rgba(239, 68, 68, 0.6), inset 0 0 30px rgba(255,255,255,0.2); }
//        }
//
//        @keyframes mic-bounce {
//            0%, 100% { transform: scale(1); }
//            50% { transform: scale(1.15); }
//        }
//
//        @keyframes sound-wave-expand {
//            0% { transform: scale(1); opacity: 0.8; }
//            100% { transform: scale(2.2); opacity: 0; }
//        }
//
//        @keyframes ring-spin {
//            0% { transform: rotate(0deg); }
//            100% { transform: rotate(360deg); }
//        }
//
//        .pulse-ring {
//            animation: pulse-ring 1.5s infinite;
//        }
//
//        .wave-bar {
//            animation: wave 0.5s ease-in-out infinite;
//        }
//        .wave-bar:nth-child(2) { animation-delay: 0.1s; }
//        .wave-bar:nth-child(3) { animation-delay: 0.2s; }
//        .wave-bar:nth-child(4) { animation-delay: 0.3s; }
//        .wave-bar:nth-child(5) { animation-delay: 0.4s; }
//
//        /* Main record button styles */
//        .record-btn-wrapper {
//            position: relative;
//            display: inline-flex;
//            align-items: center;
//            justify-content: center;
//            flex-shrink: 0;
//        }
//
//        .record-btn-outer-ring {
//            position: absolute;
//            width: 58px;
//            height: 58px;
//            border-radius: 50%;
//            border: 2px solid transparent;
//            background: linear-gradient(#0a0a0f, #0a0a0f) padding-box,
//                        linear-gradient(135deg, #8b5cf6, #ec4899, #06b6d4) border-box;
//            opacity: 0.5;
//            transition: opacity 0.3s ease;
//        }
//
//        .record-btn-wrapper:hover .record-btn-outer-ring {
//            opacity: 1;
//            animation: ring-spin 4s linear infinite;
//        }
//
//        .record-btn-wrapper.recording .record-btn-outer-ring {
//            opacity: 1;
//            background: linear-gradient(#0a0a0f, #0a0a0f) padding-box,
//                        linear-gradient(135deg, #ef4444, #f97316, #ef4444) border-box;
//            animation: ring-spin 2s linear infinite;
//        }
//
//        .record-btn {
//            position: relative;
//            width: 50px;
//            height: 50px;
//            border-radius: 50%;
//            background: linear-gradient(135deg, #1a1a2e 0%, #16213e 100%);
//            border: 1px solid rgba(255,255,255,0.1);
//            cursor: pointer;
//            transition: all 0.3s ease;
//            animation: glow-pulse 3s ease-in-out infinite;
//        }
//
//        .record-btn:hover {
//            transform: scale(1.05);
//        }
//
//        .record-btn:disabled {
//            opacity: 0.5;
//            cursor: not-allowed;
//            animation: none;
//        }
//
//        .record-btn.recording {
//            background: linear-gradient(135deg, #2d1f1f 0%, #3d1f1f 100%);
//            animation: glow-pulse-recording 1s ease-in-out infinite;
//        }
//
//        .record-btn.recording .mic-icon {
//            animation: mic-bounce 0.6s ease-in-out infinite;
//        }
//
//        .sound-wave-ring {
//            position: absolute;
//            inset: -6px;
//            border-radius: 50%;
//            border: 2px solid rgba(239, 68, 68, 0.6);
//            animation: sound-wave-expand 1.5s ease-out infinite;
//        }
//        .sound-wave-ring:nth-child(2) { animation-delay: 0.5s; }
//        .sound-wave-ring:nth-child(3) { animation-delay: 1s; }
//
//        /* Sound level indicator */
//        .sound-level-container {
//            display: flex;
//            align-items: center;
//            justify-content: center;
//            gap: 2px;
//            height: 40px;
//        }
//
//        .sound-level-bar {
//            width: 3px;
//            background: linear-gradient(to top, #8b5cf6, #ec4899);
//            border-radius: 2px;
//            transition: height 0.05s ease;
//            min-height: 4px;
//        }
//
//        .sound-level-bar.recording {
//            background: linear-gradient(to top, #ef4444, #f97316);
//        }
//
//        /* Glass card effect */
//        .glass-card {
//            background: rgba(255, 255, 255, 0.03);
//            backdrop-filter: blur(20px);
//            border: 1px solid rgba(255, 255, 255, 0.08);
//            border-radius: 24px;
//        }
//    </style>
//</head>
//<body>
//    <!-- Animated Background -->
//    <div class="bg-gradient-mesh">
//        <div class="gradient-orb orb-1"></div>
//        <div class="gradient-orb orb-2"></div>
//        <div class="gradient-orb orb-3"></div>
//        <div class="gradient-orb orb-4"></div>
//        <div class="gradient-orb orb-5"></div>
//    </div>
//    <div class="grid-overlay"></div>
//    <div class="noise-overlay"></div>
//
//    <!-- Content -->
//    <div class="content-wrapper min-h-screen flex flex-col">
//        <!-- Header -->
//        <header class="px-4 py-4">
//            <div class="max-w-7xl mx-auto">
//                <!-- Top Row: Logo, Nav, Live + Analysis -->
//                <div class="flex justify-between items-center">
//                    <!-- Logo -->
//                    <div class="flex items-center gap-3">
//                        <div class="w-10 h-10 sm:w-12 sm:h-12 rounded-2xl bg-gradient-to-br from-violet-500 via-purple-500 to-pink-500 flex items-center justify-center shadow-lg shadow-purple-500/30">
//                            <svg class="w-5 h-5 sm:w-7 sm:h-7 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
//                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 11a7 7 0 01-7 7m0 0a7 7 0 01-7-7m7 7v4m0 0H8m4 0h4m-4-8a3 3 0 01-3-3V5a3 3 0 116 0v6a3 3 0 01-3 3z"></path>
//                            </svg>
//                        </div>
//                        <div class="hidden sm:block">
//                            <h1 class="text-xl font-bold text-white">AI Interview Assistant</h1>
//                            <p class="text-xs text-white/40">Powered by AI</p>
//                        </div>
//                    </div>
//                    
//                    <!-- Center - Navigation Links (Always visible and centered) -->
//                    <div class="absolute left-1/2 transform -translate-x-1/2 flex items-center gap-3 sm:gap-5">
//                        <a href="#" class="nav-link active">Home</a>
//                        <a href="#" class="nav-link">Analysis</a>
//                        <a href="#" class="nav-link">About</a>
//                    </div>
//                    
//                    <!-- Right Side - Live Indicator -->
//                    <div class="flex items-center gap-2 sm:gap-3">
//                        <!-- Live Indicator -->
//                        <div class="live-indicator" id="liveIndicator">
//                            <span class="live-dot"></span>
//                            <span class="hidden sm:inline">Live</span>
//                        </div>
//                    </div>
//                </div>
//
//                <!-- Second Row: New Meeting and End Meeting Buttons -->
//                <div class="flex justify-end mt-6">
//                    <div class="flex items-center gap-3 sm:gap-4">
//                        <!-- New Meeting Button -->
//                        <button id="newMeetingBtn" class="header-btn btn-new-meeting">
//                            <!-- Animated background elements -->
//                            <span class="shimmer"></span>
//                            <span class="btn-glow"></span>
//                            <span class="btn-pulse-ring"></span>
//                            
//                            <!-- Sparkle particles -->
//                            <span class="sparkle sparkle-1"></span>
//                            <span class="sparkle sparkle-2"></span>
//                            <span class="sparkle sparkle-3"></span>
//                            
//                            <span class="btn-icon">
//                                <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
//                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M12 4v16m8-8H4"></path>
//                                </svg>
//                            </span>
//                            <span class="btn-text">New Meeting</span>
//                        </button>
//                        
//                        <!-- End Meeting Button -->
//                        <button id="endMeetingBtn" class="header-btn btn-end-meeting">
//                            <!-- Animated background elements -->
//                            <span class="shimmer"></span>
//                            <span class="btn-glow"></span>
//                            <span class="btn-pulse-ring"></span>
//                            
//                            <!-- Sparkle particles -->
//                            <span class="sparkle sparkle-1"></span>
//                            <span class="sparkle sparkle-2"></span>
//                            <span class="sparkle sparkle-3"></span>
//                            
//                            <span class="btn-icon">
//                                <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
//                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M6 18L18 6M6 6l12 12"></path>
//                                </svg>
//                            </span>
//                            <span class="btn-text">End Meeting</span>
//                        </button>
//                    </div>
//                </div>
//            </div>
//        </header>
//
//        <!-- Main Content -->
//        <main class="flex-1 flex flex-col items-center justify-center p-6">
//            
//            <!-- AI Interviewer Sphere (Outside glass card) -->
//            <div class="ai-sphere-container" id="aiSphereContainer">
//                <div class="ai-sphere">
//                    <!-- Glow effect -->
//                    <div class="sphere-glow" id="sphereGlow"></div>
//                    
//                    <!-- Floating particles -->
//                    <div class="particle particle-1" id="particle1"></div>
//                    <div class="particle particle-2" id="particle2"></div>
//                    <div class="particle particle-3" id="particle3"></div>
//                    <div class="particle particle-4" id="particle4"></div>
//                    <div class="particle particle-5" id="particle5"></div>
//                    <div class="particle particle-6" id="particle6"></div>
//                    
//                    <!-- Flowing blob layers -->
//                    <div class="blob-container">
//                        <div class="blob blob-outer" id="blobOuter"></div>
//                        <div class="blob blob-middle" id="blobMiddle"></div>
//                        <div class="blob blob-inner" id="blobInner"></div>
//                        <div class="blob-core" id="blobCore"></div>
//                    </div>
//                </div>
//                <p class="sphere-label" id="sphereLabel">AI Interviewer</p>
//            </div>
//
//            <!-- Glass Card Section - Centered Recording Controls -->
//            <div class="glass-card px-5 py-4 w-auto inline-flex flex-col items-center">
//                <!-- Record Button with Status -->
//                <div class="flex items-center gap-5">
//                    <!-- Record Button -->
//                    <div class="record-btn-wrapper" id="recordBtnWrapper">
//                        <!-- Outer animated ring -->
//                        <div class="record-btn-outer-ring"></div>
//                        
//                        <!-- Sound wave rings (visible when recording) -->
//                        <div id="soundWaves" class="hidden">
//                            <div class="sound-wave-ring"></div>
//                            <div class="sound-wave-ring"></div>
//                            <div class="sound-wave-ring"></div>
//                        </div>
//                        
//                        <button 
//                            id="recordBtn" 
//                            class="record-btn flex items-center justify-center"
//                        >
//                            <div class="mic-icon flex flex-col items-center">
//                                <svg id="micIcon" class="w-6 h-6 text-white/80 transition-all duration-300" fill="currentColor" viewBox="0 0 24 24">
//                                    <path d="M12 14c1.66 0 3-1.34 3-3V5c0-1.66-1.34-3-3-3S9 3.34 9 5v6c0 1.66 1.34 3 3 3z"></path>
//                                    <path d="M17 11c0 2.76-2.24 5-5 5s-5-2.24-5-5H5c0 3.53 2.61 6.43 6 6.92V21h2v-3.08c3.39-.49 6-3.39 6-6.92h-2z"></path>
//                                </svg>
//                                <svg id="stopIcon" class="w-6 h-6 text-red-400 hidden transition-all duration-300" fill="currentColor" viewBox="0 0 24 24">
//                                    <rect x="6" y="6" width="12" height="12" rx="2"></rect>
//                                </svg>
//                            </div>
//                        </button>
//                    </div>
//
//                    <!-- Sound Level Indicator - Vertical style -->
//                    <div class="sound-level-container" id="soundLevelContainer">
//                        <div class="sound-level-bar" id="bar1"></div>
//                        <div class="sound-level-bar" id="bar2"></div>
//                        <div class="sound-level-bar" id="bar3"></div>
//                        <div class="sound-level-bar" id="bar4"></div>
//                        <div class="sound-level-bar" id="bar5"></div>
//                        <div class="sound-level-bar" id="bar6"></div>
//                        <div class="sound-level-bar" id="bar7"></div>
//                        <div class="sound-level-bar" id="bar8"></div>
//                        <div class="sound-level-bar" id="bar9"></div>
//                    </div>
//
//                    <!-- Status Display -->
//                    <div class="min-w-[100px]">
//                        <!-- Idle Status -->
//                        <div id="idleStatus" class="flex flex-col">
//                            <p class="text-white/70 font-medium text-sm">Ready</p>
//                            <p class="text-white/40 text-xs">Tap to speak</p>
//                        </div>
//
//                        <!-- Recording Status -->
//                        <div id="recordingStatus" class="hidden flex flex-col">
//                            <p class="text-red-400 font-medium text-sm flex items-center gap-1.5">
//                                <span class="w-1.5 h-1.5 bg-red-500 rounded-full animate-pulse"></span>
//                                Recording
//                            </p>
//                            <p class="text-white/40 text-xs">Tap to stop</p>
//                        </div>
//
//                        <!-- Processing Status -->
//                        <div id="processingStatus" class="hidden flex flex-col">
//                            <p class="text-amber-400 font-medium text-sm flex items-center gap-1.5">
//                                <span class="w-1.5 h-1.5 bg-amber-500 rounded-full animate-pulse"></span>
//                                Thinking
//                            </p>
//                            <p class="text-white/40 text-xs">Please wait</p>
//                        </div>
//
//                        <!-- Playing Status (hidden - no text display) -->
//                        <div id="playingStatus" class="hidden"></div>
//
//                        <!-- Ended Status -->
//                        <div id="endedStatus" class="hidden flex flex-col">
//                            <p class="text-white/40 font-medium text-sm">Ended</p>
//                            <p class="text-white/30 text-xs">Start new</p>
//                        </div>
//                    </div>
//                </div>
//
//                <!-- Hidden elements for JS compatibility -->
//                <p id="recordHint" class="hidden">Tap the microphone to start speaking</p>
//
//                <!-- Error Message -->
//                <div id="errorMessage" class="hidden mt-3 p-2 bg-red-500/10 border border-red-500/30 rounded-lg text-red-400 text-center text-xs">
//                    <p id="errorText"></p>
//                </div>
//            </div>
//        </main>
//
//        <!-- Footer -->
//        <footer class="fixed bottom-0 left-0 right-0 px-6 py-4 z-10">
//            <div class="w-full flex justify-center">
//                <p class="text-white/30 text-sm text-center">AI Interview Assistant • Real-time voice conversation</p>
//            </div>
//        </footer>
//    </div>
//
//    <!-- Audio Player (hidden) -->
//    <audio id="audioPlayer" class="hidden"></audio>
//
//    <script>
//        // API Endpoints
//        const API_ENDPOINTS = {
//            conversation: '/api/llm-conversation',
//            clearFiles: '/api/files/clear'
//        };
//
//        // DOM Elements
//        const recordBtn = document.getElementById('recordBtn');
//        const recordBtnWrapper = document.getElementById('recordBtnWrapper');
//        const newMeetingBtn = document.getElementById('newMeetingBtn');
//        const endMeetingBtn = document.getElementById('endMeetingBtn');
//        const liveIndicator = document.getElementById('liveIndicator');
//        const audioPlayer = document.getElementById('audioPlayer');
//        const errorMessage = document.getElementById('errorMessage');
//        const errorText = document.getElementById('errorText');
//        const soundWaves = document.getElementById('soundWaves');
//        const micIcon = document.getElementById('micIcon');
//        const stopIcon = document.getElementById('stopIcon');
//        const soundLevelContainer = document.getElementById('soundLevelContainer');
//        const recordHint = document.getElementById('recordHint');
//
//        // Status elements
//        const idleStatus = document.getElementById('idleStatus');
//        const recordingStatus = document.getElementById('recordingStatus');
//        const processingStatus = document.getElementById('processingStatus');
//        const playingStatus = document.getElementById('playingStatus');
//        const endedStatus = document.getElementById('endedStatus');
//
//        // AI Sphere elements
//        const sphereGlow = document.getElementById('sphereGlow');
//        const sphereLabel = document.getElementById('sphereLabel');
//        const blobOuter = document.getElementById('blobOuter');
//        const blobMiddle = document.getElementById('blobMiddle');
//        const blobInner = document.getElementById('blobInner');
//        const blobCore = document.getElementById('blobCore');
//        const particles = [
//            document.getElementById('particle1'),
//            document.getElementById('particle2'),
//            document.getElementById('particle3'),
//            document.getElementById('particle4'),
//            document.getElementById('particle5'),
//            document.getElementById('particle6')
//        ];
//
//        // Sound level bars
//        const soundBars = [];
//        for (let i = 1; i <= 9; i++) {
//            soundBars.push(document.getElementById('bar' + i));
//        }
//
//        // State
//        let mediaRecorder = null;
//        let audioChunks = [];
//        let isRecording = false;
//        let isMeetingEnded = false;
//        let isMeetingActive = false;
//        let audioContext = null;
//        let analyser = null;
//        let microphone = null;
//        let animationId = null;
//
//        // Update AI Sphere animation based on state
//        function updateSphereState(state) {
//            const allBlobElements = [blobOuter, blobMiddle, blobInner, blobCore];
//            const allElements = [sphereGlow, sphereLabel, ...allBlobElements, ...particles];
//            
//            // Remove all state classes
//            allElements.forEach(el => {
//                if (el) {
//                    el.classList.remove('processing', 'speaking');
//                }
//            });
//
//            switch(state) {
//                case 'idle':
//                    sphereLabel.textContent = 'AI Interviewer';
//                    break;
//                case 'recording':
//                    sphereLabel.textContent = 'Listening...';
//                    break;
//                case 'processing':
//                    allElements.forEach(el => {
//                        if (el) el.classList.add('processing');
//                    });
//                    sphereLabel.textContent = 'Thinking...';
//                    break;
//                case 'playing':
//                    allElements.forEach(el => {
//                        if (el) el.classList.add('speaking');
//                    });
//                    sphereLabel.textContent = 'Speaking...';
//                    break;
//                case 'ended':
//                    sphereLabel.textContent = 'Interview Ended';
//                    break;
//            }
//        }
//
//        // Update live indicator
//        function updateLiveIndicator(isLive) {
//            if (isLive) {
//                liveIndicator.style.opacity = '1';
//                liveIndicator.querySelector('.live-dot').style.animationPlayState = 'running';
//            } else {
//                liveIndicator.style.opacity = '0.4';
//                liveIndicator.querySelector('.live-dot').style.animationPlayState = 'paused';
//            }
//        }
//
//        // Helper function to show status
//        function showStatus(status) {
//            idleStatus.classList.add('hidden');
//            recordingStatus.classList.add('hidden');
//            processingStatus.classList.add('hidden');
//            playingStatus.classList.add('hidden');
//            endedStatus.classList.add('hidden');
//
//            // Update sphere animation
//            updateSphereState(status);
//
//            // Update live indicator
//            updateLiveIndicator(status !== 'ended');
//
//            switch(status) {
//                case 'idle':
//                    idleStatus.classList.remove('hidden');
//                    break;
//                case 'recording':
//                    recordingStatus.classList.remove('hidden');
//                    break;
//                case 'processing':
//                    processingStatus.classList.remove('hidden');
//                    break;
//                case 'playing':
//                    // Show idle status instead of playing status text
//                    idleStatus.classList.remove('hidden');
//                    break;
//                case 'ended':
//                    endedStatus.classList.remove('hidden');
//                    break;
//            }
//        }
//
//        // Show error
//        function showError(message) {
//            errorText.textContent = message;
//            errorMessage.classList.remove('hidden');
//            setTimeout(() => {
//                errorMessage.classList.add('hidden');
//            }, 5000);
//        }
//
//        // Initialize audio recording
//        async function initRecording() {
//            try {
//                const stream = await navigator.mediaDevices.getUserMedia({ audio: true });
//                
//                // Setup audio context for visualization
//                audioContext = new (window.AudioContext || window.webkitAudioContext)();
//                analyser = audioContext.createAnalyser();
//                microphone = audioContext.createMediaStreamSource(stream);
//                microphone.connect(analyser);
//                analyser.fftSize = 256;
//                
//                mediaRecorder = new MediaRecorder(stream, { mimeType: 'audio/webm' });
//
//                mediaRecorder.ondataavailable = (event) => {
//                    if (event.data.size > 0) {
//                        audioChunks.push(event.data);
//                    }
//                };
//
//                mediaRecorder.onstop = async () => {
//                    if (audioChunks.length > 0 && !isMeetingEnded) {
//                        const audioBlob = new Blob(audioChunks, { type: 'audio/webm' });
//                        await sendAudioToAPI(audioBlob);
//                    }
//                    audioChunks = [];
//                };
//
//                return true;
//            } catch (error) {
//                console.error('Error accessing microphone:', error);
//                showError('Microphone access denied. Please allow microphone access to use this app.');
//                return false;
//            }
//        }
//
//        // Visualize sound levels
//        function visualizeSoundLevel() {
//            if (!analyser || !isRecording) return;
//
//            const dataArray = new Uint8Array(analyser.frequencyBinCount);
//            analyser.getByteFrequencyData(dataArray);
//
//            // Calculate average and distribute across bars
//            const barCount = soundBars.length;
//            const segmentSize = Math.floor(dataArray.length / barCount);
//
//            for (let i = 0; i < barCount; i++) {
//                let sum = 0;
//                for (let j = 0; j < segmentSize; j++) {
//                    sum += dataArray[i * segmentSize + j];
//                }
//                const average = sum / segmentSize;
//                const height = Math.max(4, (average / 255) * 40);
//                soundBars[i].style.height = height + 'px';
//            }
//
//            animationId = requestAnimationFrame(visualizeSoundLevel);
//        }
//
//        // Reset sound level bars
//        function resetSoundLevelBars() {
//            soundBars.forEach(bar => {
//                bar.style.height = '4px';
//                bar.classList.remove('recording');
//            });
//        }
//
//        // Toggle recording
//        async function toggleRecording() {
//            if (isMeetingEnded) return;
//
//            if (isRecording) {
//                stopRecording();
//            } else {
//                await startRecording();
//            }
//        }
//
//        // Start recording
//        async function startRecording() {
//            if (isMeetingEnded) return;
//
//            if (!mediaRecorder) {
//                const initialized = await initRecording();
//                if (!initialized) return;
//            }
//
//            audioChunks = [];
//            isRecording = true;
//            mediaRecorder.start();
//            showStatus('recording');
//            
//            // Update button appearance
//            recordBtn.classList.add('recording');
//            recordBtnWrapper.classList.add('recording');
//            soundWaves.classList.remove('hidden');
//            micIcon.classList.add('hidden');
//            stopIcon.classList.remove('hidden');
//            
//            // Hide hint text
//            recordHint.classList.add('hidden');
//            
//            // Update sound level bars style
//            soundBars.forEach(bar => bar.classList.add('recording'));
//            
//            // Start visualization
//            visualizeSoundLevel();
//        }
//
//        // Stop recording
//        function stopRecording() {
//            if (!isRecording || !mediaRecorder) return;
//
//            isRecording = false;
//            mediaRecorder.stop();
//            
//            // Stop visualization
//            if (animationId) {
//                cancelAnimationFrame(animationId);
//                animationId = null;
//            }
//            
//            // Reset button appearance
//            recordBtn.classList.remove('recording');
//            recordBtnWrapper.classList.remove('recording');
//            soundWaves.classList.add('hidden');
//            micIcon.classList.remove('hidden');
//            stopIcon.classList.add('hidden');
//            
//            // Reset sound level bars
//            resetSoundLevelBars();
//            
//            showStatus('processing');
//        }
//
//        // Send audio to API
//        async function sendAudioToAPI(audioBlob) {
//            try {
//                const formData = new FormData();
//                formData.append('user-audio', audioBlob, 'recording.mp3');
//
//                const response = await fetch(API_ENDPOINTS.conversation, {
//                    method: 'POST',
//                    body: formData
//                });
//
//                if (!response.ok) {
//                    throw new Error(`Server error: ${response.status}`);
//                }
//
//                const audioResponseBlob = await response.blob();
//                playAudioResponse(audioResponseBlob);
//
//            } catch (error) {
//                console.error('Error sending audio:', error);
//                showError('Failed to communicate with the AI. Please try again.');
//                showStatus('idle');
//            }
//        }
//
//        // Play audio response
//        function playAudioResponse(audioBlob) {
//            const audioUrl = URL.createObjectURL(audioBlob);
//            audioPlayer.src = audioUrl;
//            showStatus('playing');
//            recordBtn.disabled = true;
//
//            audioPlayer.play().catch(error => {
//                console.error('Error playing audio:', error);
//                showError('Failed to play audio response.');
//                showStatus('idle');
//                recordBtn.disabled = false;
//            });
//
//            audioPlayer.onended = () => {
//                URL.revokeObjectURL(audioUrl);
//                showStatus('idle');
//                recordBtn.disabled = false;
//            };
//        }
//
//        // Update button and nav visibility
//        function updateButtonVisibility() {
//            const navLinks = document.querySelectorAll('.nav-link');
//            
//            if (isMeetingActive) {
//                // Meeting is active
//                newMeetingBtn.classList.add('hidden');
//                endMeetingBtn.classList.remove('hidden');
//                recordBtn.disabled = false;
//                
//                // Disable all nav links during meeting
//                navLinks.forEach(link => {
//                    link.style.pointerEvents = 'none';
//                    link.style.opacity = '0.3';
//                });
//            } else {
//                // No meeting / Meeting ended
//                newMeetingBtn.classList.remove('hidden');
//                endMeetingBtn.classList.add('hidden');
//                recordBtn.disabled = true;
//                
//                // Enable all nav links
//                navLinks.forEach(link => {
//                    link.style.pointerEvents = 'auto';
//                    link.style.opacity = '1';
//                });
//            }
//        }
//
//        // New Meeting
//        async function newMeeting() {
//            try {
//                await fetch(API_ENDPOINTS.clearFiles, { method: 'GET' });
//            } catch (error) {
//                console.error('Error clearing files:', error);
//            }
//            
//            // Reset state
//            isMeetingEnded = false;
//            isMeetingActive = true;
//            
//            // Update UI
//            updateButtonVisibility();
//            showStatus('idle');
//            resetSoundLevelBars();
//        }
//
//        // End Meeting
//        function endMeeting() {
//            isMeetingEnded = true;
//            isMeetingActive = false;
//            
//            if (isRecording && mediaRecorder) {
//                mediaRecorder.stop();
//            }
//            if (animationId) {
//                cancelAnimationFrame(animationId);
//            }
//            if (audioPlayer) {
//                audioPlayer.pause();
//                audioPlayer.src = '';
//            }
//            speechSynthesis.cancel();
//            
//            resetSoundLevelBars();
//            showStatus('ended');
//            updateButtonVisibility();
//        }
//
//        // Event Listeners
//        recordBtn.addEventListener('click', toggleRecording);
//        newMeetingBtn.addEventListener('click', newMeeting);
//        endMeetingBtn.addEventListener('click', endMeeting);
//
//        // Initialize
//        showStatus('idle');
//        resetSoundLevelBars();
//        updateButtonVisibility();
//    </script>
//</body>
//</html>