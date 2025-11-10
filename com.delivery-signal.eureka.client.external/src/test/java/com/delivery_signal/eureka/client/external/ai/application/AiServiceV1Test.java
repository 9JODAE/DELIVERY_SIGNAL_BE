package com.delivery_signal.eureka.client.external.ai.application;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AiServiceV1Test {

    @Autowired
    AiServiceV1 aiServiceV1;

    @Test
    @Disabled
    void getResponse() {
        String prompt = "한국이란 어떤나라야?";
        String response = aiServiceV1.getResponse(prompt);
        System.out.println(response);
    }
}