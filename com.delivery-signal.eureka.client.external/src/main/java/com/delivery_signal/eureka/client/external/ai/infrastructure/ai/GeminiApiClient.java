package com.delivery_signal.eureka.client.external.ai.infrastructure.ai;

import com.delivery_signal.eureka.client.external.ai.application.GeminiAiCallable;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GeminiApiClient implements GeminiAiCallable {

    public static final String GEMINI_MODEL = "gemini-2.5-flash";

    @Value("${google.api.key}")
    private String apiKey;

    public String getResponse(String prompt){
        Client client = Client.builder().apiKey(apiKey).build();
        GenerateContentResponse response =
                client.models.generateContent(
                        GEMINI_MODEL,
                        prompt,
                        null);
        return response.text();
    }
}
