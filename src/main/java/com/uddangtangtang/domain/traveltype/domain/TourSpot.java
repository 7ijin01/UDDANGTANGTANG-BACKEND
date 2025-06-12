package com.uddangtangtang.domain.traveltype.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name = "tour_spot")
public class TourSpot
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    @ManyToOne
    @JoinColumn(name = "travel_type_id")
    private TravelType travelType;
}
