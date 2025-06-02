package com.uddangtangtang.domain.compatibility.domain;

import com.uddangtangtang.domain.traveltype.domain.TravelType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;


@Entity(name = "CompatibilityTestResult")
@Data
public class CompatibilityTestResult
{
    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "compatibility")
    private Compatibility compatibility;


    private LocalDateTime createdAt;

    public CompatibilityTestResult(String id, Compatibility compatibility, LocalDateTime createdAt) {
        this.id = id;
        this.compatibility = compatibility;
        this.createdAt = createdAt;
    }

    protected CompatibilityTestResult() {}
}
