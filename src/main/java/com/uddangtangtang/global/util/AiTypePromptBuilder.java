package com.uddangtangtang.global.util;

import com.uddangtangtang.domain.traveltype.dto.request.TypeRequest;

public class AiTypePromptBuilder {

    public static String buildPromptFromRequest(TypeRequest request) {
        String[] plan = request.planAnswer().split("-");
        String[] energy = request.energyAnswer().split("-");
        String[] money = request.moneyAnswer().split("-");

        return """
너는 여행 성향 분석가야.

사용자의 답변을 보고 아래 3가지 축에 따라 여행 성향을 판단하라:
- 계획성(P), 에너지 방향(E), 소비 성향(C)

각 문항마다 선택지에 따라 점수가 다르며, 선택된 값에 해당하는 점수를 누적해 최종 결과를 판단하라.

[문항별 점수표 및 사용자 선택]
(1) 계획성
Q1. 비행기 표가 갑자기 생겼다!
- 선택: %s (A: 1점 / B: 1점)
Q2. 식당이 닫혔다면?
- 선택: %s (A: 1점 / B: 1점)
Q3. 자유여행을 간다면? (우선순위)
- 선택: %s (A: 2점 / B: 2점)
Q4. 여행 전날, 짐 싸면서 드는 생각은?
- 선택: %s (A: 1점 / B: 1점)


(2) 에너지 방향
Q1. 여행 준비
- 선택: %s (A: 1점 / B: 1점)
Q2. 저녁 시간
- 선택: %s (A: 1점 / B: 1점)
Q3. 우연히 만난 사람
- 선택: %s (A: 1점 / B: 1점)
Q4. 여행 끝나고 느낀 점은? (우선순위)
- 선택: %s (A: 2점 / B: 2점)

(3) 소비 성향
Q1. 숙소 선택
- 선택: %s (A: 1점 / B: 1점)
Q2. 맛집 선택
- 선택: %s (A: 1점 / B: 1점)
Q3. 예쁜 소품 발견
- 선택: %s (A: 1점 / B: 1점)
Q4. 교통수단을 고를 때는? (우선순위)
- 선택: %s (A: 2점 / B: 2점)

[요구사항]
1. 각 축별 A점수와 B점수를 계산하라.
2. 점수가 높은 쪽이 해당 축의 결과이다.
3. 점수가 같으면 우선순위 문항의 선택지를 기준으로 결정하라.
4. 최종적으로 3축 결과를 조합하여 code를 만들고, reason을 작성하라.
5. 반드시 아래 JSON 형식으로만 결과를 출력하라.

        [유형 리스트]
        1.계획충 쉴러 A-B-A
        2.자낳괴 탐험가 B-A-A
        3.패키지 러버 A-A-B
        4.가성비 장인 B-A-A
        5.감성 도파민러 B-A-B
        6.단톡방 총무 A-A-A
        7.무념무상 힐링러 B-B-A
        8.온도차 낭만파 B-A-B

        계획, 에너지, 소비성향 답변의 알파벳을 조합하여 유형리스트와 비교 후 동일한 것을 아래 반환형식 예시와 같이 반환해라
        
        ※ 아래 형식 외에는 어떤 말도 하지 마라. 반드시 이 형식만 그대로 JSON으로 반환해라.
        반드시 아래 JSON 형식만 출력하라. 설명 문장, 개행, 문장부호 등은 절대 포함하지 마라.
        ※또한 예시에서 reason 부분은 예시일 뿐 너가 해당 유형에 맞게 알아서 작성해줘 그리고 JSON 문자열 안에서 줄바꿈(엔터, \\n)을 절대 사용하지 말고, reason은 한 줄로 길게 작성해라.
                                                                            
        [반환형식예시]
        {
         "code": "A-A-B",
         "reason": "할인과 특가, 쿠폰은 절대 못 지나치는 알뜰한 여행 스타일! 여행에서도 철저한 예산 관리와 계획으로 최소의 비용으로 최대의 만족을 추구하는 실속파예요. 소소한 것에서 행복을 찾고, 혼자서도 여행을 즐길 줄 아는 ‘가성비 여행 마스터’!"
        }
        """.formatted(
                plan[0], plan[1], plan[2], plan[3],
                energy[0], energy[1], energy[2], energy[3],
                money[0], money[1], money[2], money[3]
        );
    }
}
