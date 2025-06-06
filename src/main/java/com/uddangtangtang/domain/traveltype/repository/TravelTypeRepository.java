package com.uddangtangtang.domain.traveltype.repository;

import com.uddangtangtang.domain.traveltype.domain.TravelType;
import com.uddangtangtang.domain.traveltype.repository.custom.CustomTravelTypeRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TravelTypeRepository extends JpaRepository<TravelType, Long>, CustomTravelTypeRepository
{
    Optional<TravelType> findTravelTypeByCode(String code);
    Optional<TravelType> findTravelTypeById(Long type_id);
    Optional<TravelType> findTravelTypeByTypeName(String typeName);
}
