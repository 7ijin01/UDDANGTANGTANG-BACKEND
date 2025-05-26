package com.uddangtangtang.domain.traveltype.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uddangtangtang.domain.traveltype.domain.TravelType;
import com.uddangtangtang.domain.traveltype.dto.request.TypeRequest;
import com.uddangtangtang.domain.traveltype.dto.response.TypeResponse;
import com.uddangtangtang.domain.traveltype.repository.TravelTypeRepository;
import com.uddangtangtang.global.ai.service.AiService;
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
    private final AiService aiService;
    private final ObjectMapper objectMapper;

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
        String recommand = "";
        //에러 났을 경우 하드 코딩
        
        try {
            JsonNode jsonNode = objectMapper.readTree(aiRawResponse);
            code = jsonNode.path("code").asText();
            reason = jsonNode.has("reason") ? jsonNode.get("reason").asText() : jsonNode.path("reson").asText(); // 오타 대응

            TravelType travelType = travelTypeRepository.findTravelTypeByCode(code)
                    .orElseThrow(()->new GeneralException(ErrorStatus.TYPE_NOT_FOUND));
            image=travelType.getImage();
            description=travelType.getTypeDescription();
            name=travelType.getTypeName();
            recommand=travelType.getTripRecommand();
            return new TypeResponse(
                    code,
                    reason,
                    travelType.getImage(),
                    travelType.getTypeDescription(),
                    travelType.getTypeName(),
                    travelType.getTripRecommand());
        } catch (Exception e) {
            log.error("AI 응답 파싱 실패", e);
// \          throw new GeneralException(ErrorStatus.AI_PARSE_ERROR);

            return new TypeResponse(code,reason,image,description,name,recommand);
        }


    }

}
