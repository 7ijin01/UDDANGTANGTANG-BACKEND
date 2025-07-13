<img width="1451" height="819" alt="스크린샷 2025-07-13 11 35 02" src="https://github.com/user-attachments/assets/d7d75790-ea03-46c4-8c13-df5b16830f20" />


# 🧳 우당탕탕 여행 궁합 (UDDANGTANGTANG Compatibility)

> 여행 전에, 우리 궁합은 어떨까?

**우당탕탕 여행 궁합**은 여행을 떠나기 전, 친구·연인과의 여행 스타일 궁합을 테스트하고,  
유형에 맞는 여행지와 일정을 추천하는 경량형 성향 분석 기반 매칭 서비스입니다.

---

## 🌈 주요 기능

- **여행 성향 테스트**  
  12문항으로 성향 (계획형/자유형, 내향/외향, 가성비/가심비) 판단

- **여행 궁합 테스트**  
  두 명의 유형 조합 → 궁합 결과 + 갈등 포인트 + 추천 여행지 제공

- **궁합 네컷 생성**  
  캐릭터 기반 이미지 자동 생성 및 공유

- **GPT 기반 여행지 추천**  
  유형에 맞는 여행지와 일정을 AI가 추천

- **결과 공유 기능**  
  카카오톡/링크 공유로 자연스러운 확산 유도

---

## 👥 여행 유형 (총 8가지)

| 이름 | 설명 |
|------|------|
| 🧑‍✈️ 단톡방 총무 | 철저한 계획형 + 외향 + 실속파. 단톡방/엑셀로 일정 리딩 |
| 📦 패키지 러버 | 계획형 + 외향 + 감성파. 감성+뷰 맛집 선호 |
| 🛟 계획충 쉴러 | 계획형 + 내향 + 실속파. 통제된 쉼 추구 |
| 🧃 감성 도파민러 | 무계획 + 내향 + 감성. 즉흥 감성 여행 |
| 🎯 자낳괴 탐험가 | 자유형 + 외향 + 실속파. 즉흥 액티비티 중심 |
| 🧃 온도차 낭만파 | 자유형 + 외향 + 감성. 예쁜 여행에 몰입 |
| 💸 가성비 장인 | 자유형 + 내향 + 실속. 효율적 소비형 여행러 |
| 🐚 무념무상 힐링러 | 자유형 + 내향 + 감성. 무계획·멍 때리기 지향 |

---

## 🏗️ 백엔드 기술 스택

- Java 17, Spring Boot 3
- Spring Data JPA + MySQL (Cloud SQL)
- GPT API 연동 (OpenAI)
- Cloud Run + Cloud Storage
- Gradle, GitHub Actions
- Swagger + Spring RestDocs

---

## 📁 디렉토리 구조 (요약)

```
src/main/java/com/uddangtangtang/
├── HelloController.java
├── UddangtangtangApplication.java
├── docs/
│   ├── TravelCompatibilityControllerDocs.java
│   └── TravelTypeControllerDocs.java
├── domain/
│   ├── compatibility/
│   │   ├── controller/
│   │   │   └── TravelCompatibilityController.java
│   │   ├── domain/
│   │   │   ├── Compatibility.java
│   │   │   ├── CompatibilityTestResult.java
│   │   │   └── CompatibilityTripRecommend.java
│   │   ├── dto/
│   │   │   ├── request/
│   │   │   │   └── CompatibilityRequest.java
│   │   │   └── response/
│   │   │       ├── Compatibility4CutShareResponse.java
│   │   │       ├── CompatibilityResponse.java
│   │   │       └── CompatibilityShareResponse.java
│   │   ├── repository/
│   │   │   ├── CompatibilityRepository.java
│   │   │   ├── CompatibilityResultRepository.java
│   │   │   └── CompatibilityTripRecommendRepository.java
│   │   ├── service/
│   │   │   ├── CompatibilityLoader.java
│   │   │   └── TravelCompatibilityService.java
│   │   └── util/
│   │       └── CompatibilityUtil.java
│   └── traveltype/
│       ├── controller/
│       │   └── TravelTypeController.java
│       ├── domain/
│       │   ├── TourSpot.java
│       │   ├── TravelType.java
│       │   ├── TravelTypeTestLog.java
│       │   └── TravelTypeTestResult.java
│       ├── dto/
│       │   ├── request/
│       │   │   └── TypeRequest.java
│       │   └── response/
│       │       ├── DaySchedule.java
│       │       ├── TravelScheduleResponse.java
│       │       └── TypeResponse.java
│       ├── repository/
│       │   ├── TourSpotRepository.java
│       │   ├── TravelTypeRepository.java
│       │   ├── TravelTypeTestLogRepository.java
│       │   ├── TravelTypeTestResultRepository.java
│       │   └── custom/
│       │       ├── CustomTravelTypeRepository.java
│       │       └── CustomTravelTypeRepositoryImpl.java
│       └── service/
│           └── TravelTypeService.java
└── global/
    ├── ai/
    │   ├── dto/
    │   │   └── response/
    │   │       └── ChatGptResponse.java
    │   └── service/
    │       └── AiService.java
    ├── apiPayload/
    │   ├── ApiResponse.java
    │   ├── code/
    │   │   ├── BaseCode.java
    │   │   ├── BaseErrorCode.java
    │   │   ├── ErrorReasonDTO.java
    │   │   ├── ReasonDTO.java
    │   │   └── status/
    │   │       ├── ErrorStatus.java
    │   │       └── SuccessStatus.java
    │   └── exception/
    │       ├── ExceptionAdvice.java
    │       └── GeneralException.java
    ├── config/
    │   ├── JacksonConfig.java
    │   ├── SecurityConfig.java
    │   ├── SwaggerConfig.java
    │   └── WebClientConfig.java
    ├── controller/
    │   └── WebForwardingController.java
    └── util/
        ├── AiCompatibilityPromptBuilder.java
        ├── AiCompatibilitylRecommendPromptBuilder.java
        ├── AiTravelTypeRecommendPromptBuilder.java
        ├── AiTypePromptBuilder.java
        └── AiTypeReasonPromptBuilder.java
```

---

## ☁️ 인프라 구성

| 항목 | 구성 |
|------|------|
| 운영환경 | Google Cloud Run  |
| DB | Cloud SQL (MySQL) |
| 이미지 저장소 | Google Cloud Storage |
| GPT 연동 | OpenAI API |
| 환경변수 관리 | GitHub Secrets + application.yml |

---

## 🚀 CI/CD 파이프라인

**브랜치 전략**

- `develop` → 개발 서버(EC2) 자동 배포  
- `release/*` → 프로덕션 서버(Cloud Run) 배포

**워크플로 개요**

- ✅ `develop` 브랜치  
  Gradle 빌드 → JAR 생성 → EC2 업로드 및 재실행

- ✅ `release/*` 브랜치  
  Docker 이미지 빌드 → GCP Artifact Registry에 푸시 → Cloud Run 배포
