package com.uddangtangtang.domain.traveltype.controller;


import com.uddangtangtang.docs.TravelTypeControllerDocs;
import com.uddangtangtang.domain.traveltype.domain.TravelType;
import com.uddangtangtang.domain.traveltype.dto.request.TypeRequest;
import com.uddangtangtang.domain.traveltype.dto.response.TypeResponse;
import com.uddangtangtang.domain.traveltype.service.TravelTypeService;
import com.uddangtangtang.global.apiPayload.ApiResponse;
import com.uddangtangtang.global.apiPayload.code.status.SuccessStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ai/type")
public class TravelTypeController implements TravelTypeControllerDocs
{
    private final TravelTypeService travelTypeService;

    @PostMapping("/test")
    public ResponseEntity<ApiResponse<TypeResponse>> requestTravelTypeTest(@RequestBody TypeRequest typeRequest) {
        return ResponseEntity.ok(ApiResponse.onSuccess(SuccessStatus._OK,travelTypeService.generateTravelType(typeRequest)));
    }

    @GetMapping("/test-count")
    public ResponseEntity<ApiResponse<Long>> getTestCount() {
        long count = travelTypeService.getTestCount();
        return ResponseEntity.ok(ApiResponse.onSuccess(SuccessStatus._OK, count));
    }

    @GetMapping("/share/{id}")
    public ResponseEntity<ApiResponse<TypeResponse>> requestTravelTypeTest(@PathVariable String id)
    {
        return ResponseEntity.ok(ApiResponse.onSuccess(SuccessStatus._OK,travelTypeService.getShareResult(id)));
    }

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<TravelType>> uploadImage(@RequestParam("file") MultipartFile file, @RequestParam Long id) throws IOException
    {
        TravelType response = travelTypeService.uploadImage(file, id);
        return ResponseEntity.ok(ApiResponse.onSuccess(SuccessStatus._OK,response));
    }
}
