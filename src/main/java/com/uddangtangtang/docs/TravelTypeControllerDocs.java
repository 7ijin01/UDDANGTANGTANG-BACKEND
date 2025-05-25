package com.uddangtangtang.docs;


import com.uddangtangtang.domain.traveltype.dto.request.TypeRequest;

import com.uddangtangtang.domain.traveltype.dto.response.TypeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "여행 유형 테스트 API")
public interface TravelTypeControllerDocs
{
    @Operation(summary = "여행 유형 테스트", description = "12가지 질문에 대한 답을 RequestBody로 넣음\n" +
            "3가지 축에 대한 답을 넘겨주면 됨 ex) planAnswer : A-B-B-B")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정된 문서 반환 성공"),
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

}
