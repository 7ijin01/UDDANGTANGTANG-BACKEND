package com.uddangtangtang.domain.traveltype.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity(name = "TravelTypeTestResult")
@Data
public class TravelTypeTestResult
{
    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travel_type")
    private TravelType travelType;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String reason;

    private LocalDateTime createdAt;

    public TravelTypeTestResult(String id, TravelType travelType, String reason, LocalDateTime createdAt) {
        this.id = id;
        this.travelType = travelType;
        this.reason = reason;
        this.createdAt = createdAt;
    }

    protected TravelTypeTestResult() {}
}
