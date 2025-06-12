package com.uddangtangtang.domain.traveltype.repository;

import com.uddangtangtang.domain.traveltype.domain.TourSpot;
import com.uddangtangtang.domain.traveltype.domain.TravelType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TourSpotRepository extends JpaRepository<TourSpot, Long>
{
    List<TourSpot> findByTravelType(TravelType travelType);
}
