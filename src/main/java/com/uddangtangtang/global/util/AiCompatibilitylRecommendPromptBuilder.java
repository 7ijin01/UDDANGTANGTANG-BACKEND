package com.uddangtangtang.global.util;

import java.util.List;

public class AiCompatibilitylRecommendPromptBuilder {
    private static final List<String> TRAVEL_CITIES = List.of(
            "일본 오사카", "일본 후쿠오카", "일본 오키나와", "대만 타이중", "태국 치앙마이",
            "베트남 다낭", "말레이시아 코타키나발루", "인도네시아 욕야카르타", "필리핀 세부",
            "우즈베키스탄 사마르칸트", "포르투갈 포르투", "체코 프라하", "크로아티아 두브로브니크",
            "슬로베니아 류블랴나", "폴란드 크라쿠프", "헝가리 부다페스트", "오스트리아 그라츠",
            "루마니아 브라쇼브", "에스토니아 탈린", "스페인 톨레도", "캐나다 밴쿠버",
            "미국 시애틀", "멕시코 과나후아토", "브라질 플로리아노폴리스", "아르헨티나 바릴로체",
            "칠레 발파라이소", "뉴질랜드 더니든", "프랑스 파리", "스페인 바르셀로나", "일본 교토"
    );

    public static String buildPromptFromCompatibilityResult(String compatibilityResult) {
        String randomCity = getRandomCity();

        return """
                너는 유쾌하고 위트 있는 고급 여행 플래너야.

                아래 여행 성향 궁합 결과를 참고해서, 저 궁합에게 딱 어울리는 **해외 여행지에서의 3일 코스**를 추천해줘.

                추천 도시는 %s 이야. 오직 이 도시에 대해서만 추천 코스를 작성해.

                일정 구성:
                - **1일차, 2일차, 3일차**로 나누고,
                - 각 일차별로 **morning / afternoon / evening**을 구분해.
                - 각 일정은 최소 **2~3문장 이상**으로 작성해서 충분히 설명해줘.
                - 활동 중간중간에 음식, 산책, 사진 포인트, 감성적인 요소도 포함해줘.
                - 마지막에 각 일차마다 **"동선 요약"**을 추가해서 한 줄로 마무리.

                문체는:
                - 딱딱하지 않고 말하듯 자연스럽게, 현실적인 여행 가이드 느낌
                - 위트 있고 감정이 느껴지게!

                출력은 아래 JSON 형식으로 정확하게 반환해줘. 그 외 문장은 절대 출력하지 마.

                [여행 성향 결과]
                %s

                [출력 형식 예시]
                {
                  "day1": {
                    "morning": "...",
                    "afternoon": "...",
                    "evening": "...",
                    "summary": "..."
                  }
                }
                """.formatted(randomCity, compatibilityResult);
    }
    private static String getRandomCity() {
        List<String> filtered = TRAVEL_CITIES;
        return filtered.get((int) (Math.random() * filtered.size()));

    }
}
