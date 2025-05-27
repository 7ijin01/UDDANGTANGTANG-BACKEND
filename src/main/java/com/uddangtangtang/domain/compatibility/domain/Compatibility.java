package com.uddangtangtang.domain.compatibility.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table( uniqueConstraints = @UniqueConstraint(columnNames = {"type_a","type_b"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Compatibility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type_a", nullable = false)
    private String typeA;

    @Column(name = "type_b", nullable = false)
    private String typeB;

    @Lob
    @Column(name = "response_json", columnDefinition = "LONGTEXT", nullable = false)
    private String responseJson;

    @Builder.Default
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
