package com.uddangtangtang.domain.compatibility.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table( uniqueConstraints = @UniqueConstraint(columnNames = {"type_a","type_b"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompatibilityTripRecommend
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "type_a", nullable = false)
    private String typeA;
    @Column(name = "type_b", nullable = false)
    private String typeB;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String travelScheduleJson;


}
