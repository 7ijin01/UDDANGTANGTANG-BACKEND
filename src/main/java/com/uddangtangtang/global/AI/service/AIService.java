package com.uddangtangtang.global.AI.service;

import com.uddangtangtang.global.AI.dto.response.ChatGptResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AIService
{
    @Value("${openai.api.serviceKey}")
    private String apiKey;

    private final WebClient chatGptWebClient;

    public Mono<String> askChatGPT(String prompt) {
        Map<String, Object> requestBody = Map.of(
                "model", "gpt-4.1-nano", // 4.1-nano가 저렴함
                "messages", List.of(
                        Map.of("role", "user", "content", prompt)
                )
        );

        return chatGptWebClient.post()
                .uri("/v1/chat/completions")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + apiKey)
                .bodyValue(requestBody)
                .retrieve()//요청 및 응답 대기
                .bodyToMono(ChatGptResponse.class)
                .map(response -> Optional.ofNullable(response)
                        .map(ChatGptResponse::choices)//객체에서 초이스 추출
                        .filter(choices -> !choices.isEmpty())
                        .map(choices -> choices.get(0).message().content())// gpt 응답 추출
                        .orElse("no result"))
                .doOnError(e -> log.error("ChatGPT API 호출 실패", e))
                .onErrorResume(e->Mono.just(e.getMessage()));
    }

}
