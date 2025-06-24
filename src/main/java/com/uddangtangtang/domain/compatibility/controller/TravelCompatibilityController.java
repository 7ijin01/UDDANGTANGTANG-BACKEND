package com.uddangtangtang.domain.compatibility.controller;

import com.uddangtangtang.docs.TravelCompatibilityControllerDocs;
import com.uddangtangtang.domain.compatibility.dto.request.CompatibilityRequest;
import com.uddangtangtang.domain.compatibility.dto.response.Compatibility4CutShareResponse;
import com.uddangtangtang.domain.compatibility.dto.response.CompatibilityResponse;
import com.uddangtangtang.domain.compatibility.dto.response.CompatibilityShareResponse;
import com.uddangtangtang.domain.compatibility.service.TravelCompatibilityService;
import com.uddangtangtang.domain.traveltype.dto.response.TypeResponse;
import com.uddangtangtang.global.apiPayload.ApiResponse;
import com.uddangtangtang.global.apiPayload.code.status.SuccessStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ai/type")
public class TravelCompatibilityController implements TravelCompatibilityControllerDocs {
    private final TravelCompatibilityService compatibilityService;

    @PostMapping("/compatibility")
    public ResponseEntity<ApiResponse<CompatibilityResponse>> getCompatibility(
            @RequestBody CompatibilityRequest request) {
        CompatibilityResponse response = compatibilityService.computeCompatibility(request);

        compatibilityService.saveCompatibilityTestResult(request, response);

        return ResponseEntity.ok(
                ApiResponse.onSuccess(SuccessStatus._OK,
                        response)
        );
    }
    @GetMapping("/compatibility/share/{id}")
    public ResponseEntity<ApiResponse<CompatibilityShareResponse>> requestCompatibilityTest(@PathVariable String id)
    {
        return ResponseEntity.ok(ApiResponse.onSuccess(SuccessStatus._OK,compatibilityService.getShareResult(id)));
    }


    @GetMapping("/4cut/share/{id}")
    public ResponseEntity<ApiResponse<Compatibility4CutShareResponse>> get4CutShare(
            @PathVariable("id") String shareId) {


        Compatibility4CutShareResponse dto = compatibilityService.get4CutShare(shareId);
        return ResponseEntity.ok(
                ApiResponse.onSuccess(SuccessStatus._OK, dto)
        );
    }
}
