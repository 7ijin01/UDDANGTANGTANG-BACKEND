package com.uddangtangtang.domain.travelType.controller;

import com.uddangtangtang.domain.travelType.dto.request.TypeRequest;
import com.uddangtangtang.domain.travelType.dto.response.TypeResponse;
import com.uddangtangtang.domain.travelType.service.TravelTypeService;
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
public class TravelTypeController
{
    private final TravelTypeService travelTypeService;

    @PostMapping("/test")
    public ResponseEntity<ApiResponse<TypeResponse>> requestTravelTypeTest(@RequestBody TypeRequest typeRequest)
    {
        return ResponseEntity.ok(ApiResponse.onSuccess(SuccessStatus._OK,travelTypeService.generateTravelType(typeRequest)));
    }
}
