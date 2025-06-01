package com.uddangtangtang.docs;


import com.uddangtangtang.domain.traveltype.domain.TravelType;
import com.uddangtangtang.domain.traveltype.dto.request.TypeRequest;

import com.uddangtangtang.domain.traveltype.dto.response.TypeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name = "여행 유형 테스트 API")
public interface TravelTypeControllerDocs
{
    @Operation(summary = "여행 유형 테스트", description = "12가지 질문에 대한 답을 RequestBody로 넣음\n" +
            "3가지 축에 대한 답을 넘겨주면 됨 ex) planAnswer : A-B-B-B")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유형 테스트 성공"),
            @ApiResponse(responseCode = "500", description = "서버 에러",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "서버 에러 응답",
                                    summary = "예상치 못한 서버 에러",
                                    value = """
                {
                  "success": false,
                  "code": "COMMON_500",
                  "message": "서버 에러, 관리자에게 문의 바랍니다."
                }
                """
                            )
                    )
            )
    })
    ResponseEntity<com.uddangtangtang.global.apiPayload.ApiResponse<TypeResponse>> requestTravelTypeTest(@RequestBody TypeRequest typeRequest);

    @Operation(summary = "여행 유형 테스트 횟수")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "테스트 횟수 반환 성공"),
            @ApiResponse(responseCode = "500", description = "서버 에러",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "서버 에러 응답",
                                    summary = "예상치 못한 서버 에러",
                                    value = """
                {
                  "success": false,
                  "code": "COMMON_500",
                  "message": "서버 에러, 관리자에게 문의 바랍니다."
                }
                """
                            )
                    )
            )
    })
    ResponseEntity<com.uddangtangtang.global.apiPayload.ApiResponse<Long>> getTestCount();


    @Operation(summary = "여행 유형 테스트 공유 링크", description = "여행 유형테스트 응답 바디에 나오는 shareId를 매개로 넣어주면 됨")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "공유 url 반환 성공"),
            @ApiResponse(responseCode = "500", description = "서버 에러",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "서버 에러 응답",
                                    summary = "예상치 못한 서버 에러",
                                    value = """
                {
                  "success": false,
                  "code": "COMMON_500",
                  "message": "서버 에러, 관리자에게 문의 바랍니다."
                }
                """
                            )
                    )
            )
    })
    ResponseEntity<com.uddangtangtang.global.apiPayload.ApiResponse<TypeResponse>> requestTravelTypeTest(@PathVariable String id);

    @Operation(summary = "사진 업로드", description = "사진 업로드")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사진 업로드 성공"),
            @ApiResponse(responseCode = "500", description = "서버 에러",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "서버 에러 응답",
                                    summary = "예상치 못한 서버 에러",
                                    value = """
                {
                  "success": false,
                  "code": "COMMON_500",
                  "message": "서버 에러, 관리자에게 문의 바랍니다."
                }
                """
                            )
                    )
            )
    })
    ResponseEntity<com.uddangtangtang.global.apiPayload.ApiResponse<TravelType>> uploadImage(@RequestParam("file") MultipartFile file, @RequestParam Long id) throws IOException;
}

