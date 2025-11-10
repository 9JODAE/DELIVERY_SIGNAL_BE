package com.delivery_signal.eureka.client.external.ai.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AiServiceV1 {

    private final GeminiAiCallable geminiAiCallable;

    /**
     * <p>질문에 대한 Ai 답변 생성</p>
     * @param prompt 질문내용
     * @return response 질문에 대한 답변
     */
    public String getResponse(String prompt){
        return geminiAiCallable.getResponse(prompt);
    }
}
