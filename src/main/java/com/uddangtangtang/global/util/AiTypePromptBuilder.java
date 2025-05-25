package com.uddangtangtang.global.util;

import com.uddangtangtang.domain.traveltype.dto.request.TypeRequest;

public class AiTypePromptBuilder {

    public static String buildPromptFromRequest(TypeRequest request) {
        String[] plan = request.planAnswer().split("-");
        String[] energy = request.energyAnswer().split("-");
        String[] money = request.moneyAnswer().split("-");

        return """
        너는 여행 성향 분석가야.

        아래 질문에 대한 답변을 보고, 다음 3가지 축에 따라 사용자의 여행 성향을 분석해줘:

        계획성 (P)	A = 철저한 플래너형 / B = 유연한 자유형	
        에너지 방향 (E)	A = 외향 / B = 내향	
        소비 성향 (C)	A = 실속파 / B = 플렉스파

        [규칙 - 반드시 지켜야 할 사항]
                
        1. 사용자 답변 3개(계획성, 에너지, 소비)에 대해, 각 영역별 A/B 개수를 정확히 비교하라.
                
        2. 각 영역에서 A 개수가 많으면 A, B 개수가 많으면 B로 판단하라. 
           ex) B-A-A-A → B 1개, A 3개 → A로 판단.

        3. A와 B 개수가 동일한 경우에는 해당 영역의 **우선순위 문항**의 선택지를 기준으로 A 또는 B를 결정하라.
           - 계획성 → Q3. 자유여행을 간다면?
           - 에너지 방향 → Q4. 여행 끝나고 느낀 점은?
           - 소비 성향 → Q4. 교통수단을 고를 때는?

        4. 이 과정을 통해 도출된 각 영역별 결과(P/E/C)는 반드시 최종 code에 정확히 반영되어야 한다.
           예: 계획성 = B, 에너지 = A, 소비 성향 = A → code는 "B-A-A"

        5. code가 유형 리스트에 정확히 일치하는 항목이 있다면 해당 번호(type)를 반환하라.

        6. 반드시 reason에 서술된 분석 결과와 code는 완벽히 일치해야 하며, 둘 사이에 모순이 있으면 실패로 간주한다.

        7. 절대로 추측하거나 임의로 판단하지 말고, 오직 A/B 개수 및 우선순위 문항에 따라 결정된 값만 사용하라.

        8. 최종 출력되는 "code" 값은 위에서 판단한 계획성/에너지/소비 성향 결과와 **정확히 동일해야 하며**, 
           "reason"에는 사용자의 입장에서 A/B로 표현하지 말고, 질문 내용 기준으로 왜 그렇게 분류됐는지를 설명하라. 

        [질문 목록 및 사용자 답변]

        (1) 계획성 문항 
        Q1. 비행기 표가 갑자기 생겼다! → %s
        Q2. 식당이 닫혔다면? → %s
        Q3. 자유여행을 간다면? (우선순위) → %s
        Q4. 여행 전날, 짐 싸면서 드는 생각은? → %s
                              
        (2) 에너지 방향 
        Q1. 여행 준비 → %s
        Q2. 저녁 시간 → %s
        Q3. 우연히 만난 사람 → %s
        Q4. 여행 끝나고 느낀 점은? (우선순위) → %s
        
        (3) 소비 성향 
        Q1. 숙소 선택 → %s
        Q2. 맛집 선택 → %s
        Q3. 예쁜 소품 발견 → %s
        Q4. 교통수단을 고를 때는? (우선순위) → %s

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

        [반환형식예시]
        {
         "code": "A-A-B",
         "reason": "당신은 전체적으로 자유롭고 즉흥적인 여행을 선호하는 성향이에요. 현지에서 흐름에 따라 움직이는 걸 더 편하게 느끼는 편이죠.",
         "keyword": "#집순이 #커피 한 잔의 여유"
        }
        """.formatted(
                plan[0], plan[1], plan[2], plan[3],
                energy[0], energy[1], energy[2], energy[3],
                money[0], money[1], money[2], money[3]
        );
    }
}
