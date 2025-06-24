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
  \"recommendations\": {
                                  "day1": {
                                    "morning": "방콕 짜오프라야강 보트 투어 – 시원한 바람 맞으며 도시 전체를 느긋하게 감상",
                                    "afternoon": "왓 아룬 사원 방문 – 조용한 분위기에서 정신적 휴식",
                                    "evening": "아시아티크 야시장 – 야경 보며 스트리트푸드 즐기기",
                                    "summary": "보트투어-사원방문-야시장"
                                  },
                                  "day2": {
                                    "morning": "짜뚜짝 시장 – 기념품 쇼핑과 로컬 간식 탐방",
                                    "afternoon": "룸피니 공원 산책 – 도심 속 자연에서 여유 시간",
                                    "evening": "루프탑 바에서 칵테일 한잔 – 화려한 야경과 함께 마무리",
                                    "summary": "보트투어-사원방문-야시장"
                                  },
                                  "day3": {
                                    "morning": "방콕 외곽 온천 체험 – 여행 피로 풀기",
                                    "afternoon": "현지 마사지를 받으며 마지막 힐링",
                                    "evening": "수완나품 공항으로 이동하며 여운 남기기",
                                    "summary": "보트투어-사원방문-야시장"
                                  }
                                }
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
