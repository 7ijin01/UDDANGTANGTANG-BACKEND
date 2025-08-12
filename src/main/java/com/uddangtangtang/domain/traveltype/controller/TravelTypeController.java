package com.uddangtangtang.domain.traveltype.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.uddangtangtang.docs.TravelTypeControllerDocs;
import com.uddangtangtang.domain.traveltype.domain.TravelType;
import com.uddangtangtang.domain.traveltype.domain.TravelTypeTestResult;
import com.uddangtangtang.domain.traveltype.dto.request.TypeRequest;
import com.uddangtangtang.domain.traveltype.dto.response.TravelScheduleResponse;
import com.uddangtangtang.domain.traveltype.dto.response.TypeResponse;
import com.uddangtangtang.domain.traveltype.repository.TravelTypeRepository;
import com.uddangtangtang.domain.traveltype.repository.TravelTypeTestResultRepository;
import com.uddangtangtang.domain.traveltype.service.TravelTypeService;
import com.uddangtangtang.global.apiPayload.ApiResponse;
import com.uddangtangtang.global.apiPayload.code.status.ErrorStatus;
import com.uddangtangtang.global.apiPayload.code.status.SuccessStatus;
import com.uddangtangtang.global.apiPayload.exception.GeneralException;
import com.uddangtangtang.global.util.AiTypeReasonPromptBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ai/type")
public class TravelTypeController implements TravelTypeControllerDocs
{
    private final TravelTypeService travelTypeService;
    private final TravelTypeRepository travelTypeRepository;
    private final TravelTypeTestResultRepository travelTypeTestResultRepository;

    @PostMapping("/test")
    public Mono<ResponseEntity<ApiResponse<TypeResponse>>> requestTravelTypeTest(@RequestBody TypeRequest typeRequest) {
        return travelTypeService.generateTravelType(typeRequest)
                .map(response -> ResponseEntity.ok(ApiResponse.onSuccess(SuccessStatus._OK, response)));
    }


    @GetMapping("/test-count")
    public ResponseEntity<ApiResponse<Long>> getTestCount() {
        long count = travelTypeService.getTestCount()+580;
        return ResponseEntity.ok(ApiResponse.onSuccess(SuccessStatus._OK, count));
    }

    @GetMapping("/share/{id}")
    public ResponseEntity<ApiResponse<TypeResponse>> requestTravelTypeTest(@PathVariable String id)
    {
        return ResponseEntity.ok(ApiResponse.onSuccess(SuccessStatus._OK,travelTypeService.getShareResult(id)));
    }

//    @PostMapping("/upload")
//    public ResponseEntity<ApiResponse<TravelType>> uploadImage(@RequestParam("file") MultipartFile file, @RequestParam Long id) throws IOException
//    {
//        TravelType response = travelTypeService.uploadImage(file, id);
//        return ResponseEntity.ok(ApiResponse.onSuccess(SuccessStatus._OK,response));
//    }
//    private final RestTemplate restTemplate = new RestTemplate();
//
//    @Value("${openai.api.serviceKey}")
//    private String apiKey;
//
//    @PostMapping
//    public ResponseEntity<?> test() {
//        // 1. 코드 계산
//        String code = "A-A-A";// 너가 만든 함수라고 가정
//
//
//        // 2. DB 조회
//        TravelType travelType = travelTypeRepository.findTravelTypeByCode(code)
//                .orElseThrow(() -> new GeneralException(ErrorStatus.TYPE_NOT_FOUND));
//
//        String description = travelType.getTypeDescription();
//        String name = travelType.getTypeName();
//        String image = travelType.getImage();
//        String scheduleJson = travelType.getTravelScheduleJson();
//
//        // 3. 프롬프트 생성
//        String prompt = AiTypeReasonPromptBuilder.buildPromptFromRequest("A-A-A-A-A-A-A-A-A-A-A-A");
//
//        // 4. ChatGPT API 호출 준비
//        Map<String, Object> requestBody = Map.of(
//                "model", "gpt-4o",
//                "temperature", 0.7,
//                "messages", List.of(Map.of("role", "user", "content", prompt))
//        );
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.set("Authorization", "Bearer " + apiKey);
//
//        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
//
//        // 5. 호출 + 시간 측정
//        long start = System.currentTimeMillis();
//        ResponseEntity<Map> response = restTemplate.exchange(
//                "https://api.openai.com/v1/chat/completions",
//                HttpMethod.POST,
//                entity,
//                Map.class
//        );
//        long end = System.currentTimeMillis();
//
//        // 6. 응답 파싱
//        Map<String, Object> body = response.getBody();
//        Map<String, Object> choice = ((List<Map<String, Object>>) body.get("choices")).get(0);
//        Map<String, Object> message = (Map<String, Object>) choice.get("message");
//        String reason = ((String) message.get("content")).trim();
//
//        // 7. scheduleJson → 객체로 변환
//        TravelScheduleResponse schedule;
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            schedule = mapper.readValue(scheduleJson, TravelScheduleResponse.class);
//        } catch (Exception e) {
//            throw new RuntimeException("스케줄 파싱 실패", e);
//        }
//
//        // 8. 결과 저장
//        travelTypeTestResultRepository.save(
//                new TravelTypeTestResult("qwe", travelType, reason, LocalDateTime.now())
//        );
//
//        // 9. 결과 리턴 (원래 TypeResponse 구조 그대로)
//        Map<String, Object> result = new LinkedHashMap<>();
//        result.put("code", code);
//        result.put("reason", reason);
//        result.put("image", image);
//        result.put("description", description);
//        result.put("name", name);
//        result.put("schedule", schedule);
//        result.put("uuid", "123");
//        result.put("executionTimeMs", end - start);
//
//        return ResponseEntity.ok(result);
//    }

}
