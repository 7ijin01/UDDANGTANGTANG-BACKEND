package com.uddangtangtang.domain.compatibility.controller;

import com.uddangtangtang.docs.TravelCompatibilityControllerDocs;
import com.uddangtangtang.domain.compatibility.domain.CompatibilityTripRecommend;
import com.uddangtangtang.domain.compatibility.dto.request.CompatibilityRequest;
import com.uddangtangtang.domain.compatibility.dto.response.Compatibility4CutShareResponse;
import com.uddangtangtang.domain.compatibility.dto.response.CompatibilityResponse;
import com.uddangtangtang.domain.compatibility.dto.response.CompatibilityShareResponse;
import com.uddangtangtang.domain.compatibility.repository.CompatibilityTripRecommendRepository;
import com.uddangtangtang.domain.compatibility.service.TravelCompatibilityService;
import com.uddangtangtang.domain.traveltype.dto.response.TypeResponse;
import com.uddangtangtang.global.ai.service.AiService;
import com.uddangtangtang.global.apiPayload.ApiResponse;
import com.uddangtangtang.global.apiPayload.code.status.SuccessStatus;
import com.uddangtangtang.global.util.AiCompatibilitylRecommendPromptBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ai/type")
public class TravelCompatibilityController implements TravelCompatibilityControllerDocs {
    private final TravelCompatibilityService compatibilityService;
    private final AiService aiService;
    private final CompatibilityTripRecommendRepository compatibilityTripRecommendRepository;
    @PostMapping("/compatibility")
    public ResponseEntity<ApiResponse<CompatibilityResponse>> getCompatibility(
            @RequestBody CompatibilityRequest request) {
        CompatibilityResponse response = compatibilityService.computeCompatibility(request);
        response= compatibilityService.parseRecommendResponse(response,request.myType(),request.otherType());
        compatibilityService.saveCompatibilityTestResult(request, response);

        return ResponseEntity.ok(
                ApiResponse.onSuccess(SuccessStatus._OK,
                        response)
        );
    }
    @GetMapping("/compatibility/share/{id}")
    public ResponseEntity<ApiResponse<CompatibilityShareResponse>> requestCompatibilityTest(@PathVariable String id)
    {
        return ResponseEntity.ok(ApiResponse.onSuccess(SuccessStatus._OK,compatibilityService.getShareResult(id)));
    }


    @GetMapping("/4cut/share/{id}")
    public ResponseEntity<ApiResponse<Compatibility4CutShareResponse>> get4CutShare(
            @PathVariable("id") String shareId) {


        Compatibility4CutShareResponse dto = compatibilityService.get4CutShare(shareId);
        return ResponseEntity.ok(
                ApiResponse.onSuccess(SuccessStatus._OK, dto)
        );
    }

//    @GetMapping("/compatibility/nogada")
//    public void update()
//    {
//        List<String> combinations = new ArrayList<>();
//
//        combinations.add("가성비 장인 원숭이,가성비 장인 원숭이");
//        combinations.add("가성비 장인 원숭이,계획충 쉴러 곰");
//        combinations.add("가성비 장인 원숭이,자낳괴 탐험가 코끼리");
//        combinations.add("가성비 장인 원숭이,패키지 러버 토끼");
//        combinations.add("가성비 장인 원숭이,감성 도파민러 돼지");
//        combinations.add("가성비 장인 원숭이,단톡방 총무 고양이");
//        combinations.add("가성비 장인 원숭이,무념무상 힐링러 병아리");
//
//        combinations.add("계획충 쉴러 곰,계획충 쉴러 곰");
//        combinations.add("계획충 쉴러 곰,자낳괴 탐험가 코끼리");
//        combinations.add("계획충 쉴러 곰,패키지 러버 토끼");
//        combinations.add("계획충 쉴러 곰,감성 도파민러 돼지");
//        combinations.add("계획충 쉴러 곰,단톡방 총무 고양이");
//        combinations.add("계획충 쉴러 곰,무념무상 힐링러 병아리");
//
//        combinations.add("자낳괴 탐험가 코끼리,자낳괴 탐험가 코끼리");
//        combinations.add("자낳괴 탐험가 코끼리,패키지 러버 토끼");
//        combinations.add("자낳괴 탐험가 코끼리,감성 도파민러 돼지");
//        combinations.add("자낳괴 탐험가 코끼리,단톡방 총무 고양이");
//        combinations.add("자낳괴 탐험가 코끼리,무념무상 힐링러 병아리");
//
//        combinations.add("패키지 러버 토끼,패키지 러버 토끼");
//        combinations.add("패키지 러버 토끼,감성 도파민러 돼지");
//        combinations.add("패키지 러버 토끼,단톡방 총무 고양이");
//        combinations.add("패키지 러버 토끼,무념무상 힐링러 병아리");
//
//        combinations.add("감성 도파민러 돼지,감성 도파민러 돼지");
//        combinations.add("감성 도파민러 돼지,단톡방 총무 고양이");
//        combinations.add("감성 도파민러 돼지,무념무상 힐링러 병아리");
//
//        combinations.add("단톡방 총무 고양이,단톡방 총무 고양이");
//        combinations.add("단톡방 총무 고양이,무념무상 힐링러 병아리");
//
//        combinations.add("무념무상 힐링러 병아리,무념무상 힐링러 병아리");
//        combinations.add("온도차 낭만파 강아지,가성비 장인 원숭이");
//        combinations.add("온도차 낭만파 강아지,감성 도파민러 돼지");
//        combinations.add("온도차 낭만파 강아지,단톡방 총무 고양이");
//        combinations.add("온도차 낭만파 강아지,무념무상 힐링러 병아리");
//        combinations.add("온도차 낭만파 강아지,자낳괴 탐험가 코끼리");
//        combinations.add("온도차 낭만파 강아지,패키지 러버 토끼");
//        combinations.add("온도차 낭만파 강아지,계획충 쉴러 곰");
//        combinations.add("온도차 낭만파 강아지,온도차 낭만파 강아지");
//
//
//        List<String> rrsult = new ArrayList<>();
//        List<CompatibilityTripRecommend> compatibilityTripRecommends=new ArrayList<>();
//        rrsult.add("두 분은 가성비를 중시하는 성향이 강해 여행 경비를 효율적으로 관리할 수 있습니다. 비슷한 취향 덕분에 서로의 의견을 존중하며 여행 계획을 세울 수 있습니다. 그러나 지나치게 절약하려는 경향이 갈등을 유발할 수 있습니다.");
//        rrsult.add("가성비 장인 원숭이와 계획충 쉴러 곰은 서로의 스타일이 다르지만, 조화를 이룰 수 있습니다. 원숭이는 유연한 사고로 곰의 계획을 보완할 수 있습니다. 함께하는 여행에서 서로의 장점을 살리면 더욱 즐거운 경험이 될 것입니다.");
//        rrsult.add("자낳괴 탐험가와 가성비 장인 원숭이는 서로의 여행 스타일을 보완할 수 있습니다. 탐험가의 호기심이 원숭이의 실용적인 접근과 잘 어우러질 것입니다. 함께 새로운 경험을 추구하면서도 비용을 절약하는 방법을 찾을 수 있습니다.");
//        rrsult.add("가성비 장인 원숭이와 패키지 러버 토끼는 서로 다른 여행 스타일을 가지고 있습니다. 원숭이는 비용을 절약하는 데 집중하는 반면, 토끼는 편안함과 편리함을 중시합니다. 서로의 장점을 이해하고 조화롭게 여행하면 좋은 경험을 할 수 있습니다.");
//        rrsult.add("감성 도파민러와 가성비 장인은 서로 다른 여행 스타일을 가지고 있습니다. 감성을 중시하는 당신은 특별한 경험을 원하지만, 상대방은 비용을 중요시합니다. 서로의 장점을 살려 조화를 이루면 더욱 즐거운 여행이 될 것입니다.");
//        rrsult.add("가성비 장인 원숭이와 단톡방 총무 고양이는 서로의 장점을 잘 활용할 수 있습니다. 원숭이는 비용을 절약하는 데 능하고, 고양이는 계획을 잘 세우는 편입니다. 함께하면 효율적인 여행이 될 것입니다.");
//        rrsult.add("가성비 장인 원숭이와 무념무상 힐링러 병아리는 서로 다른 여행 스타일을 가지고 있습니다. 원숭이는 실속을 중시하고, 병아리는 여유로운 힐링을 추구합니다. 하지만 서로의 장점을 이해하면 조화로운 여행이 가능합니다.");
//
//        rrsult.add("두 분 모두 계획을 중시하는 타입이라 여행이 원활하게 진행될 것입니다. 서로의 의견을 존중하며 계획을 세우면 더욱 즐거운 시간이 될 것입니다. 하지만 세부사항에 대한 갈등이 생길 수 있으니 주의가 필요합니다.");
//        rrsult.add("자낳괴 탐험가와 계획충 쉴러는 서로 다른 여행 스타일을 가지고 있습니다. 탐험가는 즉흥적인 모험을 선호하고, 계획충은 철저한 준비를 중시합니다. 하지만 서로의 장점을 이해하면 멋진 여행이 될 수 있습니다.");
//        rrsult.add("여행 전 서로의 일정과 계획을 공유하세요. 패키지 투어의 장점을 활용해 쉴러 곰이 원하는 일정도 포함시키세요. 함께하는 시간을 늘리기 위해 유연한 일정을 유지하는 것이 중요합니다.");
//        rrsult.add("여행 전 서로의 기대를 명확히 이야기해보세요. 계획적인 일정 속에 즉흥적인 활동을 포함시켜 보세요. 감성적인 순간을 함께 공유하며 서로의 스타일을 이해하는 시간을 가지세요.");
//        rrsult.add("계획충 쉴러 곰은 세부적인 일정에 집착할 수 있어 고양이의 즉흥적인 성향과 충돌할 수 있습니다. 고양이는 때때로 계획을 무시하고 즉흥적으로 행동할 수 있어 쉴러 곰을 불안하게 만들 수 있습니다. 서로의 스타일을 이해하고 조화롭게 조율하는 것이 중요합니다.");
//        rrsult.add("계획충 쉴러 곰과 무념무상 힐링러 병아리는 서로의 스타일을 보완할 수 있습니다. 계획적인 여행과 즉흥적인 순간이 조화를 이루며 특별한 경험을 만들어낼 것입니다. 서로의 속도를 존중하면 더욱 즐거운 여행이 될 것입니다.");
//
//        rrsult.add("두 사람 모두 탐험가 유형으로, 새로운 경험을 추구하는 데에 큰 공감대를 형성합니다. 서로의 호기심을 자극하며 즐거운 여행을 만들 수 있습니다. 그러나 때때로 의견 충돌이 있을 수 있으니 유연한 태도가 필요합니다.");
//        rrsult.add("탐험가는 즉흥적인 계획을 선호하지만, 패키지 러버는 사전 계획을 중시합니다. 일정 변경에 대한 갈등이 발생할 수 있습니다. 서로의 여행 스타일을 이해하고 조율하는 것이 중요합니다.");
//        rrsult.add("탐험가는 빠른 속도를 원할 수 있지만, 돼지는 느긋한 일정을 선호할 수 있습니다. 서로의 여행 스타일을 이해하고 조율하는 것이 필요합니다. 감정적인 순간에 대한 이해가 필요할 수 있습니다.");
//        rrsult.add("고양이는 조용한 시간을 원할 수 있지만, 코끼리는 활동적인 일정을 선호할 수 있습니다. 이로 인해 일정 조율에서 갈등이 생길 수 있습니다. 서로의 필요를 이해하고 조율하는 것이 중요합니다.");
//        rrsult.add("탐험가는 빠른 속도를 원하지만 힐링러는 느긋한 일정을 선호할 수 있습니다. 서로의 관심사에 대한 이해가 부족할 경우 갈등이 생길 수 있습니다. 여행 중에는 서로의 기분을 잘 살피고 조율하는 것이 필요합니다.");
//
//        rrsult.add("여행 일정이 너무 빡빡할 경우 스트레스를 받을 수 있습니다. 각자의 선호하는 활동이 다를 경우 조율이 필요할 수 있습니다. 패키지 여행의 선택지가 제한적일 수 있어 유연한 태도가 필요합니다.");
//        rrsult.add("토끼는 계획적인 여행을 선호하지만, 돼지는 즉흥적인 활동을 좋아할 수 있습니다. 일정에 대한 의견 차이가 생길 수 있으니 미리 조율하세요. 서로의 스타일을 이해하고 조화롭게 여행하는 것이 관건입니다.");
//        rrsult.add("토끼는 즉흥적인 활동을 선호할 수 있지만, 고양이는 계획적인 일정을 선호합니다. 일정 변경에 대한 갈등이 생길 수 있습니다. 서로의 스타일을 이해하고 조율하는 것이 중요합니다.");
//        rrsult.add("여행 일정의 유연성에 대한 의견 차이가 있을 수 있습니다. 힐링을 중시하는 당신은 느긋한 일정을 원할 수 있지만, 상대방은 계획적인 일정을 선호할 수 있습니다. 서로의 스타일을 이해하고 조화롭게 조정하는 것이 중요합니다.");
//
//        rrsult.add("감정적인 결정으로 인해 계획이 변경될 수 있습니다. 서로의 의견을 존중하지 않으면 갈등이 생길 수 있습니다. 감정의 기복이 심할 수 있으니 서로의 감정을 잘 살펴야 합니다.");
//        rrsult.add("감성 도파민러는 즉흥적인 활동을 선호할 수 있지만, 단톡방 총무는 계획적인 일정을 중시합니다. 서로의 스타일이 충돌할 수 있으니 조율이 필요합니다. 감정 표현의 차이로 인해 오해가 생길 수 있으니 소통이 중요합니다.");
//        rrsult.add("감성 도파민러는 자극적인 활동을 원할 수 있지만, 힐링러는 조용한 시간을 선호할 수 있습니다. 일정 조율에서 갈등이 생길 수 있습니다. 서로의 필요를 이해하고 조화롭게 조정하는 것이 필요합니다.");
//
//        rrsult.add("일정 조율에서 의견 차이가 발생할 수 있습니다. 각자의 고집이 충돌할 가능성이 있습니다. 하지만 서로의 성향을 이해하면 갈등을 줄일 수 있습니다.");
//        rrsult.add("고양이는 계획적인 성향이 강해 즉흥적인 힐링러와 갈등이 생길 수 있습니다. 반면, 힐링러는 고양이의 빠른 템포에 지칠 수 있습니다. 서로의 속도를 맞추는 것이 중요합니다");
//
//        rrsult.add("의사결정 시 소극적일 수 있어 갈등이 생길 수 있습니다. 너무 많은 선택지로 인해 혼란스러울 수 있습니다. 각자의 힐링 방식이 다를 수 있으니 조율이 필요합니다.");
//
//        rrsult.add("원숭이는 비용을 절약하려고 할 것이고, 강아지는 특별한 경험을 원할 수 있습니다. 일정이 너무 빡빡하면 갈등이 생길 수 있습니다. 서로의 기대치를 조율하는 것이 중요합니다.");
//        rrsult.add("감성 도파민러는 즉흥적인 활동을 선호할 수 있지만, 온도차 낭만파는 계획적인 일정을 선호할 수 있습니다. 서로의 스타일을 존중하며 조율하는 것이 필요합니다. 감정 표현의 방식이 다를 수 있으니, 솔직한 대화가 중요합니다.");
//        rrsult.add("고양이는 조용한 시간을 원할 수 있지만, 강아지는 활동적인 일정을 선호할 수 있습니다. 서로의 속도에 맞추는 것이 중요합니다. 감정 표현 방식이 다를 수 있으니, 소통을 통해 이해를 깊이세요.");
//        rrsult.add("힐링러는 조용한 시간을 원할 수 있지만, 낭만파는 활동적인 일정을 선호할 수 있습니다. 서로의 속도 차이로 인해 갈등이 생길 수 있으니 미리 조율이 필요합니다. 감정 표현 방식이 다를 수 있으니, 서로의 언어를 이해하려는 노력이 중요합니다.");
//        rrsult.add("탐험가는 빠른 속도를 원하고, 낭만파는 느긋한 일정을 선호할 수 있습니다. 서로의 일정 조율이 필요합니다. 탐험가가 너무 많은 활동을 계획하면 낭만파가 지칠 수 있습니다.");
//        rrsult.add("토끼는 계획적인 여행을 선호하지만, 강아지는 즉흥적인 변화를 원할 수 있습니다. 강아지가 감정적으로 기분이 변할 때, 토끼는 당황할 수 있습니다. 서로의 스타일을 이해하고 조율하는 것이 필요합니다.");
//        rrsult.add("계획충 쉴러는 세부적인 일정에 집착할 수 있습니다. 온도차 낭만파는 즉흥적인 순간을 선호할 수 있습니다. 서로의 스타일을 존중하지 않으면 갈등이 생길 수 있습니다.");
//        rrsult.add("감정의 기복이 있을 수 있어 서로의 기분을 잘 살펴야 합니다. 결정할 때 의견 차이가 생길 수 있으니 미리 조율하는 것이 좋습니다. 너무 비슷한 취향으로 인해 선택의 폭이 좁아질 수 있습니다.");
//
//
//        int index=0;
//        for(String arr:  combinations)
//        {
//            String [] token=arr.split(",");
//            String a=token[0];
//            String b=token[1];
//            String prompt=  AiCompatibilitylRecommendPromptBuilder.buildPromptFromCompatibilityResult(rrsult.get(index));
//            String json = aiService.askChatGPT(prompt).block();
//            CompatibilityTripRecommend recommend= new CompatibilityTripRecommend(index,a,b,json);
//            index++;
//            compatibilityTripRecommendRepository.save(recommend);
//        }
//    }

}
