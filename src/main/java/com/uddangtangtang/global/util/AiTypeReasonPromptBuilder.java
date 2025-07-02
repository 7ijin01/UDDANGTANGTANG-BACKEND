package com.uddangtangtang.global.util;

public class AiTypeReasonPromptBuilder {

    public static String buildPromptFromRequest(String typeDescription) {
        return """
                너는 사람의 여행 성향을 유쾌하고 위트 있게 설명해주는 작가야.
                
                아래의 여행 성향 설명을 보고, 왜 이 사람이 이런 유형으로 분류되었는지를 
                재미있고 개성 있게 한 문단으로 작성해줘. 
           
                ✅ 반드시 지켜야 할 조건:
                - 내용은 병맛스럽거나 현실 공감되는 말투로 써줘.
                - 여행 성향 설명은 그대로 쓰지 말고 재해석해서 써.
                - 분량은 200자 정도. 너무 짧게 쓰지 마.
                - 문장은 줄바꿈 없이 한 문단으로 써줘.
                - 너무 GPT스러운 딱딱한 설명은 금지. 유쾌하게!

                [여행 성향 설명]
                %s
                """.formatted(typeDescription);
    }
}
