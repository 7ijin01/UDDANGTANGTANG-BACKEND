package com.uddangtangtang.domain.compatibility.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uddangtangtang.domain.compatibility.domain.Compatibility;
import com.uddangtangtang.domain.compatibility.dto.request.CompatibilityRequest;
import com.uddangtangtang.domain.compatibility.dto.response.CompatibilityResponse;
import com.uddangtangtang.domain.compatibility.repository.CompatibilityRepository;
import com.uddangtangtang.global.ai.service.AiService;
import com.uddangtangtang.global.apiPayload.code.status.ErrorStatus;
import com.uddangtangtang.global.apiPayload.exception.GeneralException;
import com.uddangtangtang.global.util.AiCompatibilityPromptBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TravelCompatibilityService {
    private final AiService aiService;
    private final ObjectMapper objectMapper;
    private final CompatibilityRepository compatibilityRepo;

    public CompatibilityResponse computeCompatibility(CompatibilityRequest request) {
        String t1 = request.myType() == null || request.myType().isBlank()
                ? "?" : request.myType();
        String t2 = request.otherType();
        String typeA = t1.compareTo(t2) <= 0 ? t1 : t2;
        String typeB = t1.compareTo(t2) <= 0 ? t2 : t1;

        Optional<Compatibility> cached = compatibilityRepo.findByTypeAAndTypeB(typeA, typeB);
        if (cached.isPresent()) {
            try {
                String json = cached.get().getResponseJson();
                JsonNode node = objectMapper.readTree(json);
                return parseJson(node);
            } catch (Exception e) {
                log.warn("Cache parse error, regenerating AI response", e);
            }
        }

        String prompt = AiCompatibilityPromptBuilder.buildPrompt(request);
        String aiRaw = aiService.askChatGPT(prompt).block();
        log.info("AI compatibility raw: {}", aiRaw);
        CompatibilityResponse resp = parseRaw(aiRaw);

        try {
            String jsonResp = objectMapper.writeValueAsString(resp);
            Compatibility entry = Compatibility.builder()
                    .typeA(typeA)
                    .typeB(typeB)
                    .responseJson(jsonResp)
                    .build();
            compatibilityRepo.save(entry);
        } catch (Exception e) {
            log.warn("Failed to save compatibility cache", e);
        }
        return resp;
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
        return new CompatibilityResponse(result, tips, conflict, recs);
    }
}
