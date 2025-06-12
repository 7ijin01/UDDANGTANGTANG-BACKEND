package com.uddangtangtang.domain.compatibility.repository;

import com.uddangtangtang.domain.compatibility.domain.Compatibility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompatibilityRepository extends JpaRepository<Compatibility, Long> {
    Optional<Compatibility> findByTypeAAndTypeB(String typeA, String typeB);

}

