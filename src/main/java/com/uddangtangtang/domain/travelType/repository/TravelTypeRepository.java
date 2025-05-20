package com.uddangtangtang.domain.travelType.repository;

import com.uddangtangtang.domain.travelType.domain.TravelType;
import com.uddangtangtang.domain.travelType.repository.custom.CustomTravelTypeRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TravelTypeRepository extends JpaRepository<TravelType, Long>, CustomTravelTypeRepository
{
    Optional<TravelType> findTravelTypeByCode(String code);
}
