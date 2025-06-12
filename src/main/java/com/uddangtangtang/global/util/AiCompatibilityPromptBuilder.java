package com.uddangtangtang.global.util;

import com.uddangtangtang.domain.compatibility.dto.request.CompatibilityRequest;

public class AiCompatibilityPromptBuilder {
    public static String buildPrompt(CompatibilityRequest request) {
        String myType = request.myType();
        String otherType = request.otherType();
        String prompt = """
너는 여행 궁합 전문가야.
아래 JSON 형식으로만 응답해라.
결과에는 다음 필드를 포함해라:
- \"result\": 궁합 결과(3줄)
- \"tips\": 함께하는 여행 팁(3줄)
- \"conflictPoints\": 갈등&조화 포인트(3줄)
- \"recommendations\": 추천 여행지 3곳을 배열에 담아라. 각 요소는 \"장소: 간단한 코스\" 형식.
JSON 예시:
{
  \"result\": \"...\",
  \"tips\": \"...\",
  \"conflictPoints\": \"...\",
  \"recommendations\": [\"코스1\", \"코스2\", \"코스3\"]
}
""";
        if (myType == null || myType.isBlank()) {
            prompt += String.format("나의 유형: (모름)\n상대방의 유형: %s\n", otherType);
        } else {
            prompt += String.format("나의 유형: %s\n상대방의 유형: %s\n", myType, otherType);
        }
        return prompt;
    }
}
