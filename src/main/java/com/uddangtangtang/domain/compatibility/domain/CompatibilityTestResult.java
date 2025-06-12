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

    @Column(name = "type_a", nullable = false)
    private String typeA;

    @Column(name = "type_b", nullable = false)
    private String typeB;

    @Lob
    @Column(name = "response_json", columnDefinition = "LONGTEXT", nullable = false)
    private String responseJson;

    private LocalDateTime createdAt;

    public CompatibilityTestResult(String id,String typeA, String typeB,String json,LocalDateTime createdAt) {
        this.id = id;
        this.responseJson = json;
        this.createdAt = createdAt;
        this.typeA = typeA;
        this.typeB = typeB;
    }

    protected CompatibilityTestResult() {}
}
