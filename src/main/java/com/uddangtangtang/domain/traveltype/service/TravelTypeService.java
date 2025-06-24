package com.uddangtangtang.domain.traveltype.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uddangtangtang.domain.traveltype.domain.TravelType;
import com.uddangtangtang.domain.traveltype.domain.TravelTypeTestLog;
import com.uddangtangtang.domain.traveltype.domain.TravelTypeTestResult;
import com.uddangtangtang.domain.traveltype.dto.request.TypeRequest;
import com.uddangtangtang.domain.traveltype.dto.response.TravelScheduleResponse;
import com.uddangtangtang.domain.traveltype.dto.response.TypeResponse;
import com.uddangtangtang.domain.traveltype.repository.TourSpotRepository;
import com.uddangtangtang.domain.traveltype.repository.TravelTypeRepository;
import com.uddangtangtang.domain.traveltype.repository.TravelTypeTestLogRepository;
import com.uddangtangtang.domain.traveltype.repository.TravelTypeTestResultRepository;
import com.uddangtangtang.global.ai.service.AiService;
import com.uddangtangtang.global.apiPayload.code.status.ErrorStatus;
import com.uddangtangtang.global.apiPayload.exception.GeneralException;
import com.uddangtangtang.global.util.AiTravelTypeRecommendPromptBuilder;
import com.uddangtangtang.global.util.AiTypeReasonPromptBuilder;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;
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
        String scheduleJson =travelType.getTravelScheduleJson();
        String typePrompt = AiTypeReasonPromptBuilder.buildPromptFromRequest(description);

        Mono<String> reasonMono = aiService.askChatGPT(typePrompt);// 비동기 처리
        Mono<TravelScheduleResponse> scheduleMono = Mono.fromCallable(() ->
                objectMapper.readValue(scheduleJson, TravelScheduleResponse.class)
        );
        return Mono.zip(reasonMono,scheduleMono)
                .map(tuple -> {
                    String reason = tuple.getT1().trim();
                    TravelScheduleResponse schedule = tuple.getT2();

                    travelTypeTestLogRepository.save(new TravelTypeTestLog());
                    travelTypeTestResultRepository.save(
                            new TravelTypeTestResult(uuid, travelType, reason, LocalDateTime.now())
                    );

                    return new TypeResponse(
                            code, reason, image, description, name, schedule, uuid
                    );
                })
                .block(); // 최종 조합된 결과 기다림
    }




    @Scheduled(fixedRate = 5 * 60 * 1000)
    public void updateCachedCount() {
        cachedCount = travelTypeTestLogRepository.count();
    }

    @PostConstruct//@Scheduled 이거랑 같이 못 씀 분리해야 제대로 동작합
    public void init() {
        updateCachedCount();
        updateTripRecommendation();
    }
    public Long getTestCount()
    {
        return cachedCount;
    }

    @Scheduled(fixedRate = 7L * 24 * 60 * 60 * 1000)
    public void updateTripRecommendation() {
        List<TravelType> travelTypes = travelTypeRepository.findAll();

        for (TravelType travelType : travelTypes) {
            String description = travelType.getTypeDescription();
            String request = AiTravelTypeRecommendPromptBuilder.buildPromptFromTypeResult(description);

            aiService.askChatGPT(request)
                    .doOnNext(response -> {
                        travelType.setTravelScheduleJson(response);
                        travelTypeRepository.save(travelType);
                        log.info("Updated schedule for type {}", travelType.getCode());
                    })
                    .doOnError(e -> log.error("여행지 추천 업데이트 실패 {}", travelType.getCode(), e))
                    .subscribe();
        }
    }

    public TypeResponse getShareResult(String uuid)
    {
        TravelTypeTestResult travelTypeTestResult= travelTypeTestResultRepository.findTravelTypeTestResultById(uuid)
                .orElseThrow(() -> new GeneralException(ErrorStatus.RESULT_NOT_FOUND));
        TravelType travelType = travelTypeTestResult.getTravelType();

        try {
            TravelScheduleResponse recommendations = objectMapper.readValue(
                    travelType.getTravelScheduleJson(),
                    TravelScheduleResponse.class
            );

            return new TypeResponse(
                    travelType.getCode(),
                    travelTypeTestResult.getReason(),
                    travelType.getImage(),
                    travelType.getTypeDescription(),
                    travelType.getTypeName(),
                    recommendations,
                    travelTypeTestResult.getId()
            );
        } catch (Exception e) {
            throw new GeneralException(ErrorStatus._BAD_REQUEST);
        }

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
        log.info("결과{},{}\n",aScore,bScore);

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
