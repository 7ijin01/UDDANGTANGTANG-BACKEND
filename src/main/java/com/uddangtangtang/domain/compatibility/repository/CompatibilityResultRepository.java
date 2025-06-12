package com.uddangtangtang.domain.compatibility.repository;

import com.uddangtangtang.domain.compatibility.domain.CompatibilityTestResult;
import com.uddangtangtang.domain.traveltype.domain.TravelTypeTestResult;
import org.hibernate.query.sqm.tree.expression.Compatibility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompatibilityResultRepository extends JpaRepository<CompatibilityTestResult,String>
{
   Optional<CompatibilityTestResult> findCompatibilityTestResultById(String id);
}
