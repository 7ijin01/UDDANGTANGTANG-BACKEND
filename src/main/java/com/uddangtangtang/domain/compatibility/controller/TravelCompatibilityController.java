package com.uddangtangtang.domain.compatibility.controller;

import com.uddangtangtang.domain.compatibility.dto.request.CompatibilityRequest;
import com.uddangtangtang.domain.compatibility.dto.response.CompatibilityResponse;
import com.uddangtangtang.domain.compatibility.service.TravelCompatibilityService;
import com.uddangtangtang.global.apiPayload.ApiResponse;
import com.uddangtangtang.global.apiPayload.code.status.SuccessStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ai/type")
public class TravelCompatibilityController {
    private final TravelCompatibilityService compatibilityService;

    @PostMapping("/compatibility")
    public ResponseEntity<ApiResponse<CompatibilityResponse>> getCompatibility(
            @RequestBody CompatibilityRequest request) {
        return ResponseEntity.ok(
                ApiResponse.onSuccess(SuccessStatus._OK,
                        compatibilityService.computeCompatibility(request))
        );
    }
}
