package com.uddangtangtang.domain.travelType.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uddangtangtang.domain.travelType.domain.TravelType;
import com.uddangtangtang.domain.travelType.dto.request.TypeRequest;
import com.uddangtangtang.domain.travelType.dto.response.TypeResponse;
import com.uddangtangtang.domain.travelType.repository.TravelTypeRepository;
import com.uddangtangtang.global.AI.service.AIService;
import com.uddangtangtang.global.apiPayload.code.status.ErrorStatus;
import com.uddangtangtang.global.apiPayload.exception.GeneralException;
import com.uddangtangtang.global.util.AiTypePromptBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TravelTypeService
{
    private final TravelTypeRepository travelTypeRepository;
    private final AIService aiService;
    private final ObjectMapper objectMapper;

    public TypeResponse generateTravelType(TypeRequest request)
    {
        String prompt=AiTypePromptBuilder.buildPromptFromRequest(request);
        String aiRawResponse = aiService.askChatGPT(prompt).block();
        try {
            JsonNode jsonNode = objectMapper.readTree(aiRawResponse);

            String code = jsonNode.path("code").asText();
            String reason = jsonNode.has("reason") ? jsonNode.get("reason").asText() : jsonNode.path("reson").asText(); // 오타 대응
            String keyWord = jsonNode.has("keyWord") ? jsonNode.get("keyWord").asText() : "";

            TravelType travelType = travelTypeRepository.findTravelTypeByCode(code)
                    .orElseThrow(()->new GeneralException(ErrorStatus.CODE_NOT_FOUND));

            return new TypeResponse(code,
                    reason,
                    keyWord,
                    travelType.getImage(),
                    travelType.getType_description(),
                    travelType.getType_name(),
                    travelType.getTrip_recommand());
        } catch (Exception e) {
            log.error("AI 응답 파싱 실패", e);
            throw new GeneralException(ErrorStatus.AI_PARSE_ERROR);
        }


    }

}
