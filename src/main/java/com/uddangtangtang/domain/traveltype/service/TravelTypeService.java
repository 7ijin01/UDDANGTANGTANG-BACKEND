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
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    public TypeResponse generateTravelType(TypeRequest request)
    {
        String prompt= AiTypePromptBuilder.buildPromptFromRequest(request);
        String aiRawResponse = aiService.askChatGPT(prompt).block();
        log.info(aiRawResponse);
        String code = "";
        String reason = "";
        String image = "";
        String description = "";
        String name = "";
        List<String> recommendations=new ArrayList<>();
        String uuid = UUID.randomUUID().toString();
        try {
            JsonNode jsonNode = objectMapper.readTree(aiRawResponse);
            code = jsonNode.path("code").asText();
            reason = jsonNode.has("reason") ? jsonNode.get("reason").asText() : jsonNode.path("reson").asText(); // 오타 대응
            TravelType travelType = travelTypeRepository.findTravelTypeByCode(code)
                    .orElseThrow(()->new GeneralException(ErrorStatus.TYPE_NOT_FOUND));
            description=travelType.getTypeDescription();
            name=travelType.getTypeName();

            image=travelType.getImage();

            recommendations = tourSpotRepository.findByTravelType(travelType)
                    .stream()
                    .map(spot -> spot.getName() + ": " + spot.getDescription())
                    .toList();
            travelTypeTestLogRepository.save(new TravelTypeTestLog());
            TravelTypeTestResult result = new TravelTypeTestResult(uuid, travelType, reason, LocalDateTime.now());
            travelTypeTestResultRepository.save(result);

            return new TypeResponse(
                    code,
                    reason,
                    image,
                    travelType.getTypeDescription(),
                    travelType.getTypeName(),
                    recommendations,
                    uuid);
        } catch (Exception e) {
            log.error("AI 응답 파싱 실패", e);
// \          throw new GeneralException(ErrorStatus.AI_PARSE_ERROR);

            return new TypeResponse(code,reason,image,description,name, recommendations,uuid);
        }


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



}
