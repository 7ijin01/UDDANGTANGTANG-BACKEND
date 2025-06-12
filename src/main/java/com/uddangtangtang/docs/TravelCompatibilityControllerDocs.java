package com.uddangtangtang.docs;

import com.uddangtangtang.domain.compatibility.dto.request.CompatibilityRequest;
import com.uddangtangtang.domain.compatibility.dto.response.CompatibilityResponse;
import com.uddangtangtang.domain.compatibility.dto.response.CompatibilityShareResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "여행 궁합 테스트 API")
public interface TravelCompatibilityControllerDocs {

    @Operation(summary = "여행 궁합 테스트", description = "나의 여행 유형과 상대방의 유형을 입력하면, 궁합 결과, 여행 팁, 갈등&조화 포인트, 추천 여행지를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "궁합 테스트 성공"),
            @ApiResponse(responseCode = "500", description = "서버 에러",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "서버 에러 응답", summary = "예상치 못한 서버 에러",
                                    value = """
{ "success": false, "code": "COMMON_500", "message": "서버 에러, 관리자에게 문의 바랍니다." }
""")))
    })
    ResponseEntity<com.uddangtangtang.global.apiPayload.ApiResponse<CompatibilityResponse>> getCompatibility(CompatibilityRequest request);


    @Operation(summary = "여행 궁합 테스트 공유 링크", description = "여행 궁합 테스트 응답 바디에 나오는 shareId를 매개로 넣어주면 됨")
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
    ResponseEntity<com.uddangtangtang.global.apiPayload.ApiResponse<CompatibilityShareResponse>> requestCompatibilityTest(@PathVariable String id);
}

