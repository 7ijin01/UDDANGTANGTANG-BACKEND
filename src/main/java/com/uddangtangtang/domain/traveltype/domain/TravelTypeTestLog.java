package com.uddangtangtang.domain.traveltype.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity(name = "TravelTypeLog")
public class TravelTypeTestLog {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime testedAt = LocalDateTime.now();

}

