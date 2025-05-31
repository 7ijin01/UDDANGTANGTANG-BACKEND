package com.uddangtangtang.domain.traveltype.repository;

import com.uddangtangtang.domain.traveltype.domain.TravelTypeTestResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TravelTypeTestResultRepository extends JpaRepository<TravelTypeTestResult, String>
{
    @Query("SELECT r FROM TravelTypeTestResult r LEFT JOIN FETCH r.travelType WHERE r.id = :id")
    Optional<TravelTypeTestResult> findTravelTypeTestResultById(String id);
}
