package com.uddangtangtang.domain.compatibility.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uddangtangtang.domain.compatibility.domain.Compatibility;
import com.uddangtangtang.domain.compatibility.domain.CompatibilityTestResult;
import com.uddangtangtang.domain.compatibility.domain.CompatibilityTripRecommend;
import com.uddangtangtang.domain.compatibility.dto.request.CompatibilityRequest;
import com.uddangtangtang.domain.compatibility.dto.response.Compatibility4CutShareResponse;
import com.uddangtangtang.domain.compatibility.dto.response.CompatibilityResponse;
import com.uddangtangtang.domain.compatibility.dto.response.CompatibilityShareResponse;
import com.uddangtangtang.domain.compatibility.repository.CompatibilityRepository;
import com.uddangtangtang.domain.compatibility.repository.CompatibilityResultRepository;
import com.uddangtangtang.domain.compatibility.repository.CompatibilityTripRecommendRepository;
import com.uddangtangtang.domain.traveltype.domain.TravelType;
import com.uddangtangtang.domain.traveltype.domain.TravelTypeTestResult;
import com.uddangtangtang.domain.traveltype.dto.response.TravelScheduleResponse;
import com.uddangtangtang.domain.traveltype.repository.TravelTypeRepository;
import com.uddangtangtang.global.ai.service.AiService;
import com.uddangtangtang.global.apiPayload.code.status.ErrorStatus;
import com.uddangtangtang.global.apiPayload.exception.GeneralException;
import com.uddangtangtang.global.util.AiCompatibilityPromptBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.uddangtangtang.global.apiPayload.code.status.ErrorStatus.RESULT_NOT_FOUND;

@Service
@Slf4j
@RequiredArgsConstructor
public class TravelCompatibilityService {
    private final AiService aiService;
    private final ObjectMapper objectMapper;
    private final CompatibilityRepository compatibilityRepo;
    private final CompatibilityResultRepository compatibilityResultRepo;
    private final CompatibilityTripRecommendRepository compatibilityTripRecommendRepo;
    private final TravelTypeRepository travelTypeRepository;


//    @Transactional
    public CompatibilityResponse computeCompatibility(CompatibilityRequest request)  {

        String t1 = request.myType() == null || request.myType().isBlank() ? "?" : request.myType();
        String t2 = request.otherType();
        String typeA = t1.compareTo(t2) <= 0 ? t1 : t2;
        String typeB = t1.compareTo(t2) <= 0 ? t2 : t1;
        String typeAName=request.myType();
        String typeBName = request.otherType();
        Compatibility compatibility;
        CompatibilityResponse resp;

        Optional<Compatibility> cached = compatibilityRepo.findByTypeAAndTypeB(typeA, typeB);
        if (cached.isPresent()) {
            compatibility = cached.get();

            try {
                JsonNode node = objectMapper.readTree(compatibility.getResponseJson());
                resp = parseJsonWithNewUUID(node,typeAName, typeBName);

            } catch (Exception e) {
                log.warn("Cache parse error, regenerating AI response", e);
                throw new GeneralException(ErrorStatus.AI_PARSE_ERROR);
            }
        } else {
            String prompt = AiCompatibilityPromptBuilder.buildPrompt(request);
            String aiRaw = aiService.askChatGPT(prompt).block();
            log.info("AI compatibility raw: {}", aiRaw);
            try {
                resp = parseRaw(aiRaw, typeAName, typeBName);
            } catch (JsonProcessingException e) {
                throw new GeneralException(ErrorStatus.AI_PARSE_ERROR);
            }


            try {
                String jsonResp = objectMapper.writeValueAsString(resp);
                compatibility = Compatibility.builder()
                        .typeA(typeA)
                        .typeB(typeB)
                        .responseJson(jsonResp)
                        .build();
                compatibilityRepo.save(compatibility);


            } catch (Exception e) {
                log.warn("Failed to save compatibility cache", e);
                throw new GeneralException(ErrorStatus.AI_PARSE_ERROR);
            }
        }


        return resp;
    }

    public CompatibilityResponse parseRecommendResponse(CompatibilityResponse response,String typeA, String typeB)  {
        CompatibilityTripRecommend compatibilityTripRecommend =
                compatibilityTripRecommendRepo.findByTypeAAndTypeB(typeA, typeB);

        if (compatibilityTripRecommend == null) {
            compatibilityTripRecommend =
                    compatibilityTripRecommendRepo.findByTypeAAndTypeB(typeB, typeA);
        }

        // 여기가 핵심: 여전히 null이면 에러 던져야 함
        if (compatibilityTripRecommend == null) {

            throw new GeneralException(RESULT_NOT_FOUND);
        }

        String recommendTripJson = compatibilityTripRecommend.getTravelScheduleJson();

        try {
            TravelScheduleResponse recommendTrip=objectMapper.readValue(recommendTripJson, TravelScheduleResponse.class);
            CompatibilityResponse compatibilityResponse = new CompatibilityResponse(response.result(),response.tips(),response.conflictPoints(),recommendTrip,response.shareId());
            return compatibilityResponse;
        }
        catch (Exception e) {
            throw new GeneralException(ErrorStatus._INTERNAL_SERVER_ERROR);
        }


    }


    private CompatibilityResponse parseJsonWithNewUUID(JsonNode node, String typeA, String typeB) throws JsonProcessingException {
        if (node.has("error")) {
            log.error("❗ 캐시된 응답이 에러 메시지입니다: {}", node.get("error").asText());
            throw new GeneralException(ErrorStatus.AI_PARSE_ERROR);
        }

        String result = node.path("result").asText();
        String tips = node.path("tips").asText();
        String conflict = node.path("conflictPoints").asText();


        String uuid = UUID.randomUUID().toString();
        return new CompatibilityResponse(result, tips, conflict, null, uuid);
    }


    private CompatibilityResponse parseRaw(String raw,String typeA, String typeB) throws JsonProcessingException {
        try {
            JsonNode node = objectMapper.readTree(raw);
            return parseJson(node,typeA, typeB);
        } catch (Exception e) {
            log.error("AI parse error in compatibility", e);
            throw new GeneralException(ErrorStatus.AI_PARSE_ERROR);
        }
    }

    private CompatibilityResponse parseJson(JsonNode node,String typeA, String typeB)  {
        String result = node.path("result").asText();
        String tips = node.path("tips").asText();
        String conflict = node.path("conflictPoints").asText();
        String uuid= UUID.randomUUID().toString();
        return new CompatibilityResponse(result, tips, conflict, null,uuid);
    }

    private CompatibilityShareResponse parseJsonWithFixedUUID(JsonNode node, String uuid,String myImage,String otherImage,String typeA, String typeB) throws JsonProcessingException {
        String result = node.path("result").asText();
        String tips = node.path("tips").asText();
        String conflict = node.path("conflictPoints").asText();
        CompatibilityTripRecommend compatibilityTripRecommend =
                compatibilityTripRecommendRepo.findByTypeAAndTypeB(typeA, typeB);

        if (compatibilityTripRecommend == null) {
            compatibilityTripRecommend =
                    compatibilityTripRecommendRepo.findByTypeAAndTypeB(typeB, typeA);
        }

        if (compatibilityTripRecommend == null) {

            throw new GeneralException(RESULT_NOT_FOUND);
        }
        try {
            TravelScheduleResponse recommendTrip=objectMapper.readValue(compatibilityTripRecommend.getTravelScheduleJson(),TravelScheduleResponse.class);
            return new CompatibilityShareResponse(result, tips, conflict, recommendTrip, uuid,myImage,otherImage);
        }
        catch (Exception e) {
            throw new GeneralException(ErrorStatus._INTERNAL_SERVER_ERROR);
        }





    }

    public CompatibilityShareResponse getShareResult(String id)
    {

        CompatibilityTestResult compatibilityTestResult=compatibilityResultRepo.findCompatibilityTestResultById(id)
                .orElseThrow(()->new GeneralException(ErrorStatus.RESULT_NOT_FOUND));


        TravelType travelTypeA=travelTypeRepository.findTravelTypeByTypeName(compatibilityTestResult.getTypeA())
                .orElseThrow(()->new GeneralException(ErrorStatus.TYPE_NOT_FOUND));
        TravelType travelTypeB=travelTypeRepository.findTravelTypeByTypeName(compatibilityTestResult.getTypeB())
                .orElseThrow(()->new GeneralException(ErrorStatus.TYPE_NOT_FOUND));


        try {
            JsonNode node = objectMapper.readTree(compatibilityTestResult.getResponseJson());
            return parseJsonWithFixedUUID(node, id, travelTypeA.getImage(), travelTypeB.getImage(),travelTypeA.getTypeName(),travelTypeB.getTypeName());
        } catch (Exception e) {
            log.error("Failed to parse responseJson for share result", e);
            throw new GeneralException(ErrorStatus.AI_PARSE_ERROR);
        }

    }
    @Transactional
    public void saveCompatibilityTestResult(CompatibilityRequest request, CompatibilityResponse resp) {
        String t1 = request.myType() == null || request.myType().isBlank() ? "?" : request.myType();
        String t2 = request.otherType();
        String typeA = t1.compareTo(t2) <= 0 ? t1 : t2;
        String typeB = t1.compareTo(t2) <= 0 ? t2 : t1;

        try {
            String jsonResp = objectMapper.writeValueAsString(resp);

            CompatibilityTestResult result = new CompatibilityTestResult(
                    resp.shareId(), typeA, typeB, jsonResp, LocalDateTime.now()
            );
            compatibilityResultRepo.save(result);
        } catch (Exception e) {
            throw new GeneralException(ErrorStatus.AI_PARSE_ERROR);
        }
    }

    @Transactional(readOnly = true)
    public Compatibility4CutShareResponse get4CutShare(String shareId) {
        CompatibilityTestResult testResult = compatibilityResultRepo
                .findCompatibilityTestResultById(shareId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.RESULT_NOT_FOUND));

        // 여기에 추가 검증 로직(예: 만화 생성 여부 확인)이 필요하다면 넣을 수 있습니다.

        return new Compatibility4CutShareResponse(testResult.getId());
    }
}
