package com.uddangtangtang.domain.traveltype.repository;

import com.uddangtangtang.domain.traveltype.domain.TravelTypeTestLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TravelTypeTestLogRepository extends JpaRepository<TravelTypeTestLog, Long> {
}
