package com.uddangtangtang.domain.compatibility.service;

import com.uddangtangtang.domain.compatibility.dto.request.CompatibilityRequest;
import com.uddangtangtang.domain.traveltype.domain.TravelType;
import com.uddangtangtang.domain.traveltype.repository.TravelTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CompatibilityLoader implements CommandLineRunner {
    private final TravelTypeRepository typeRepo;
    private final TravelCompatibilityService compatibilityService;

    @Override
    public void run(String... args) {
        // 1) TravelType 엔티티에서 typeName(예: "무념무상 힐링러")을 뽑아온다
        List<String> names = typeRepo.findAll().stream()
                .map(TravelType::getTypeName)
                .toList();

        // 2) 이중 루프로 i ≤ j 순서로 조합
        for (int i = 0; i < names.size(); i++) {
            for (int j = i; j < names.size(); j++) {
                String myName    = names.get(i);
                String otherName = names.get(j);
                // 3) typeName 기반 새로운 DTO를 만든 뒤, 서비스 호출
                CompatibilityRequest req =
                        new CompatibilityRequest(myName, otherName);

                compatibilityService.computeCompatibility(req);
                log.info("Preloaded compatibility by name: {} - {}", myName, otherName);
            }
        }
    }
}

