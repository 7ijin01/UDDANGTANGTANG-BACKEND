package com.uddangtangtang.domain.compatibility.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uddangtangtang.domain.compatibility.domain.Compatibility;
import com.uddangtangtang.domain.compatibility.domain.CompatibilityTestResult;
import com.uddangtangtang.domain.compatibility.dto.request.CompatibilityRequest;
import com.uddangtangtang.domain.compatibility.dto.response.CompatibilityResponse;
import com.uddangtangtang.domain.compatibility.repository.CompatibilityRepository;
import com.uddangtangtang.domain.compatibility.repository.CompatibilityResultRepository;
import com.uddangtangtang.domain.traveltype.domain.TravelTypeTestResult;
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

    @Transactional
    public CompatibilityResponse computeCompatibility(CompatibilityRequest request) {
        String t1 = request.myType() == null || request.myType().isBlank() ? "?" : request.myType();
        String t2 = request.otherType();
        String typeA = t1.compareTo(t2) <= 0 ? t1 : t2;
        String typeB = t1.compareTo(t2) <= 0 ? t2 : t1;

        Compatibility compatibility;
        CompatibilityResponse resp;

        Optional<Compatibility> cached = compatibilityRepo.findByTypeAAndTypeB(typeA, typeB);
        if (cached.isPresent()) {
            compatibility = cached.get();
            try {
                JsonNode node = objectMapper.readTree(compatibility.getResponseJson());
                resp = parseJsonWithNewUUID(node);
            } catch (Exception e) {
                log.warn("Cache parse error, regenerating AI response", e);
                throw new GeneralException(ErrorStatus.AI_PARSE_ERROR);
            }
        } else {
            String prompt = AiCompatibilityPromptBuilder.buildPrompt(request);
            String aiRaw = aiService.askChatGPT(prompt).block();
            log.info("AI compatibility raw: {}", aiRaw);
            resp = parseRaw(aiRaw);

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
        CompatibilityTestResult result = new CompatibilityTestResult(resp.shareId(), compatibility, LocalDateTime.now());
        compatibilityResultRepo.save(result);

        return resp;
    }
    private CompatibilityResponse parseJsonWithNewUUID(JsonNode node)
    {
        String result = node.path("result").asText();
        String tips = node.path("tips").asText();
        String conflict = node.path("conflictPoints").asText();
        List<String> recs = new ArrayList<>();
        node.withArray("recommendations").forEach(n -> recs.add(n.asText()));
        String uuid = UUID.randomUUID().toString();
        return new CompatibilityResponse(result, tips, conflict, recs, uuid);
    }

    private CompatibilityResponse parseRaw(String raw) {
        try {
            JsonNode node = objectMapper.readTree(raw);
            return parseJson(node);
        } catch (Exception e) {
            log.error("AI parse error in compatibility", e);
            throw new GeneralException(ErrorStatus.AI_PARSE_ERROR);
        }
    }

    private CompatibilityResponse parseJson(JsonNode node) {
        String result = node.path("result").asText();
        String tips = node.path("tips").asText();
        String conflict = node.path("conflictPoints").asText();
        List<String> recs = new ArrayList<>();
        node.withArray("recommendations").forEach(n -> recs.add(n.asText()));
        String uuid= UUID.randomUUID().toString();
        return new CompatibilityResponse(result, tips, conflict, recs,uuid);
    }

    private CompatibilityResponse parseJsonWithFixedUUID(JsonNode node, String uuid) {
        String result = node.path("result").asText();
        String tips = node.path("tips").asText();
        String conflict = node.path("conflictPoints").asText();
        List<String> recs = new ArrayList<>();
        node.withArray("recommendations").forEach(n -> recs.add(n.asText()));
        return new CompatibilityResponse(result, tips, conflict, recs, uuid);
    }

    public CompatibilityResponse getShareResult(String id)
    {
        CompatibilityTestResult testResult = compatibilityResultRepo.findTravelCompatibilityById(id)
                .orElseThrow(()->new GeneralException(RESULT_NOT_FOUND));
        Compatibility compatibility=testResult.getCompatibility();
        try {
            JsonNode node = objectMapper.readTree(compatibility.getResponseJson());
            return parseJsonWithFixedUUID(node, testResult.getId());
        } catch (Exception e) {
            log.error("Failed to parse compatibility result from DB", e);
            throw new GeneralException(ErrorStatus.AI_PARSE_ERROR);
        }
    }
}
