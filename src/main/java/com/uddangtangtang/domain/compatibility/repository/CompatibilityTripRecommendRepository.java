package com.uddangtangtang.domain.compatibility.repository;

import com.uddangtangtang.domain.compatibility.domain.CompatibilityTripRecommend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompatibilityTripRecommendRepository extends JpaRepository<CompatibilityTripRecommend, Integer>
{
    CompatibilityTripRecommend findByTypeAAndTypeB(String typeA, String typeB);
}
