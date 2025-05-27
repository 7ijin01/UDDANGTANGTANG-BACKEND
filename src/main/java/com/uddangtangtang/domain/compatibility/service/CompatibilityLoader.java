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
        List<String> codes = typeRepo.findAll().stream().map(TravelType::getCode).toList();
        for (int i = 0; i < codes.size(); i++) {
            for (int j = i; j < codes.size(); j++) {
                String my = codes.get(i);
                String other = codes.get(j);
                compatibilityService.computeCompatibility(
                        new CompatibilityRequest(my.equals("?") ? "" : my, other));
                log.info("Preloaded compatibility: {} - {}", my, other);
            }
        }
    }
}

