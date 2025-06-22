package com.uddangtangtang.domain.traveltype.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uddangtangtang.domain.compatibility.dto.response.CompatibilityResponse;
import com.uddangtangtang.domain.traveltype.domain.TourSpot;
import com.uddangtangtang.domain.traveltype.domain.TravelType;
import com.uddangtangtang.domain.traveltype.domain.TravelTypeTestLog;
import com.uddangtangtang.domain.traveltype.domain.TravelTypeTestResult;
import com.uddangtangtang.domain.traveltype.dto.request.TypeRequest;
import com.uddangtangtang.domain.traveltype.dto.response.TourSpotSimpleDto;
import com.uddangtangtang.domain.traveltype.dto.response.TypeResponse;
import com.uddangtangtang.domain.traveltype.repository.TourSpotRepository;
import com.uddangtangtang.domain.traveltype.repository.TravelTypeRepository;
import com.uddangtangtang.domain.traveltype.repository.TravelTypeTestLogRepository;
import com.uddangtangtang.domain.traveltype.repository.TravelTypeTestResultRepository;
import com.uddangtangtang.global.ai.service.AiService;
import com.uddangtangtang.global.apiPayload.code.status.ErrorStatus;
import com.uddangtangtang.global.apiPayload.exception.GeneralException;
import com.uddangtangtang.global.util.AiTypePromptBuilder;
import com.uddangtangtang.global.util.AiTypeReasonPromptBuilder;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class TravelTypeService
{
    private final TravelTypeRepository travelTypeRepository;
    private final TravelTypeTestLogRepository travelTypeTestLogRepository;
    private final TravelTypeTestResultRepository travelTypeTestResultRepository;
    private final TourSpotRepository  tourSpotRepository;
    private final AiService aiService;
    private final ObjectMapper objectMapper;

    private Long cachedCount = 0L;

    public TypeResponse generateTravelType(TypeRequest request) {
        String rawAnswer = request.answer();
        String code = calculateTypeCode(rawAnswer);
        String uuid = UUID.randomUUID().toString();

        TravelType travelType = travelTypeRepository.findTravelTypeByCode(code)
                .orElseThrow(() -> new GeneralException(ErrorStatus.TYPE_NOT_FOUND));

        String description = travelType.getTypeDescription();
        String name = travelType.getTypeName();
        String image = travelType.getImage();

        String prompt = AiTypeReasonPromptBuilder.buildPromptFromRequest(description);
        Mono<String> reasonMono = aiService.askChatGPT(prompt); // 비동기 처리

        Mono<List<String>> recommendationsMono = Mono.fromCallable(() ->
                tourSpotRepository.findByTravelType(travelType)
                        .stream()
                        .map(spot -> spot.getName() + ": " + spot.getDescription())
                        .toList()
        );

        return Mono.zip(reasonMono, recommendationsMono)
                .map(tuple -> {
                    String reason = tuple.getT1().trim();
                    List<String> recommendations = tuple.getT2();

                    travelTypeTestLogRepository.save(new TravelTypeTestLog());
                    travelTypeTestResultRepository.save(
                            new TravelTypeTestResult(uuid, travelType, reason, LocalDateTime.now())
                    );

                    return new TypeResponse(
                            code, reason, image, description, name, recommendations, uuid
                    );
                })
                .block(); // 최종 조합된 결과 기다림
    }



    @PostConstruct
    @Scheduled(fixedRate = 5 * 60 * 1000)
    public void updateCachedCount() {
        cachedCount = travelTypeTestLogRepository.count();
    }

    public Long getTestCount()
    {
        return cachedCount;
    }

    public TypeResponse getShareResult(String uuid)
    {
        TravelTypeTestResult travelTypeTestResult= travelTypeTestResultRepository.findTravelTypeTestResultById(uuid)
                .orElseThrow(() -> new GeneralException(ErrorStatus.RESULT_NOT_FOUND));
        TravelType travelType = travelTypeTestResult.getTravelType();
        List<String> recommendations = tourSpotRepository.findByTravelType(travelType)
                .stream()
                .map(spot -> spot.getName() + ": " + spot.getDescription())
                .toList();
        return new TypeResponse(
                travelType.getCode(),
                travelTypeTestResult.getReason(),
                travelType.getImage(),
                travelType.getTypeDescription(),
                travelType.getTypeName(),
                recommendations,
                travelTypeTestResult.getId()
        );
    }

    private String[] splitAnswers(String raw) {
        return raw.split("-");
    }

    private String calculateAxis(String[] answers, int[] indexes, int priorityIndex) {
        int aScore = 0;
        int bScore = 0;

        for (int i = 0; i < indexes.length; i++) {
            String answer = answers[indexes[i]];
            int score = (i == priorityIndex) ? 2 : 1;

            if ("A".equalsIgnoreCase(answer)) {
                aScore += score;
            } else if ("B".equalsIgnoreCase(answer)) {
                bScore += score;
            }
        }

        if (aScore > bScore) return "A";
        else if (bScore > aScore) return "B";
        else {
            String priorityAnswer = answers[indexes[priorityIndex]];
            return priorityAnswer.toUpperCase();
        }
    }
    public String calculateTypeCode(String rawAnswer) {
        String[] answers = splitAnswers(rawAnswer);

        String plan = calculateAxis(answers, new int[]{1, 6, 0, 4}, 2);  // 계획
        String energy = calculateAxis(answers, new int[]{2, 10, 9, 11}, 2); // 에너지
        String spending = calculateAxis(answers, new int[]{3, 7, 8, 5}, 3); // 소비

        return String.join("-", plan, energy, spending); // 예: A-B-A
    }




}
