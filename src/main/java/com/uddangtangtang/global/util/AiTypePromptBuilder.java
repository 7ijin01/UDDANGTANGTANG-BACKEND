package com.uddangtangtang.global.util;

import com.uddangtangtang.domain.travelType.dto.request.TypeRequest;
public class AiTypePromptBuilder {
    public static String buildPromptFromRequest(TypeRequest request) {
        return """
        너는 여행 성향 분석가야.

        아래 질문에 대한 답변을 보고, 다음 3가지 축에 따라 사용자의 여행 성향을 분석해줘:

        계획성 (P)	A = 철저한 플래너형 / B = 유연한 자유형	
        에너지 방향 (E)	A = 외향 / B = 내향	
        소비 성향 (C)	A = 실속파 / B = 플렉스파
        여행 목적 (M) A = 힐링 / B = 자극 탐험
        
        [질문 목록]   
        
        (1) 계획성 문항 
        Q1. 비행기 표가 갑자기 생겼다! 
        A. 패키지 여행이 편해 
        B. 자유여행 고고 
                
        Q2. 식당이 닫혔다면? 
        A. 계획 틀어져서 불안 
        B. 근처 맛집 찾자 
                
        Q3. 자유여행을 간다면? 
        A. 전부 예약해둬야 안심 
        B. 현지에서 유동적으로 
                              
        (2) 에너지 방향 
        Q1. 여행 준비 
        A. 단톡방 만들어서 친구들과 
        B. 혼자 루트 정리 
                
        Q2. 저녁 시간 
        A. 게스트와 술 한잔 
        B. 조용히 휴식 
                
        Q3. 우연히 만난 사람 
        A. 같이 밥 먹자고 함 
        B. 대화 후 각자 여행 
                    
        (3) 소비 성향 
        Q1. 숙소 선택 
        A. 후기/위치/가격 
        B. 수영장/뷰/인테리어 
                
        Q2. 맛집 선택 
        A. 로컬 저렴 맛집 
        B. 분위기 있는 인스타 맛집 
                
        Q3. 예쁜 소품 발견 
        A. 안 쓰면 안 삼 
        B. 기념으로 산다 
                 
        (4) 여행 목적 
        Q1. 지쳐서 떠나는 여행 
        A. 쉬고 싶다 
        B. 새로운 걸 해보고 싶다 
                
        Q2. 완벽한 사진 
        A. 선셋 아래 커피 
        B. 타투 체험 중인 나 
                
        Q3. 가장 먼저 떠오르는 건 
        A. 뷰 좋은 숙소 
        B. 축제나 특별한 경험 
                
        [유형 리스트]
        1.계획충 쉴러 A-B-A-A
        2.자낳괴 탐험가 B-A-A-B
        3.패키지 러버 A-A-B-A
        4.가성비 장인 B-B-A-A-1
        5.감성 도파민러 B-A-B-B
        6.단톡방 총무 A-A-A-B
        7.무념무상 힐링러 B-B-A-A-2
        8.온도차 낭만파 B-A-B-A

        [사용자 답변 요약]
        계획성 문항 답변: %s
        에너지 방향 답변: %s
        소비성향 답변: %s
        여행목적 답변: %s

        분석 결과를 유형 중 가장 가까운 하나로 매칭해줘.
        가성비 장인과 무념무상 힐링러는 답변에 따라서 너가 구분해줘

        위 답변을 분석하고, 가장 잘 맞는 하나의 유형으로 결과를 반환해줘.
        [반환형식예시]
        {
            "type": "유형 번호 (1~8)",
            "code": "A-A-A-B" <- 해당 유형 코드
            "reason": "분석 설명 자세히"
            "key_word: "#집순이 #커피 한 잔의 여유" <- 이런식으로
        }
        """.formatted(
                request.planAnswer(),
                request.energyAnswer(),
                request.moneyAnswer(),
                request.goalAnswer()
        );
    }
}


