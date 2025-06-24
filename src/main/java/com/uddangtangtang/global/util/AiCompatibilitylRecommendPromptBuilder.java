package com.uddangtangtang.global.util;

public class AiCompatibilitylRecommendPromptBuilder {

    public static String buildPromptFromCompatibilityResult(String compatibilityResult) {
        return """
너는 유쾌하고 재치 있는 여행 플래너야.

아래 궁합 결과를 참고해서, 이 두 사람이 함께 가면 좋을 **해외 여행지 3일치 코스**를 추천해줘.

반드시 지켜야 할 조건:
- 추천 장소는 **해외 여행지**로 구성해.
- 일정은 **1일차, 2일차, 3일차**로 나눠서 자세히 작성해줘.
- 각 일차별로 아침~저녁까지의 주요 동선과 활동을 자연스럽게 설명해.
- 장소명, 활동, 그 이유까지 설명해. (예: 왜 여기가 이 둘에게 잘 맞는지)
- 추천지는 **이 궁합 유형에 딱 맞는 곳**으로 정해줘.
- 문체는 너무 딱딱하지 말고, 현실적이고 위트 있게!
- 마지막 출력은 아래 JSON 형식으로 정확하게 리턴해줘. **문장 외 출력 금지!**

[궁합 결과]
%s

 [출력 형식 예시 - 정확히 이 구조로!]
                                {
                                  "day1": {
                                    "morning": "방콕 짜오프라야강 보트 투어 – 시원한 바람 맞으며 도시 전체를 느긋하게 감상",
                                    "afternoon": "왓 아룬 사원 방문 – 조용한 분위기에서 정신적 휴식",
                                    "evening": "아시아티크 야시장 – 야경 보며 스트리트푸드 즐기기"
                                  },
                                  "day2": {
                                    "morning": "짜뚜짝 시장 – 기념품 쇼핑과 로컬 간식 탐방",
                                    "afternoon": "룸피니 공원 산책 – 도심 속 자연에서 여유 시간",
                                    "evening": "루프탑 바에서 칵테일 한잔 – 화려한 야경과 함께 마무리"
                                  },
                                  "day3": {
                                    "morning": "방콕 외곽 온천 체험 – 여행 피로 풀기",
                                    "afternoon": "현지 마사지를 받으며 마지막 힐링",
                                    "evening": "수완나품 공항으로 이동하며 여운 남기기"
                                  }
                                }
""".formatted(compatibilityResult);
    }
}
