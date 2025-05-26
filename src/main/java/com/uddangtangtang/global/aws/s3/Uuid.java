package com.uddangtangtang.global.aws.s3;

import com.uddangtangtang.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "uuids")   // 테이블 이름을 명시해 두면 나중에 헷갈리지 않아요
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Uuid extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 36)
    private String uuid;

    /** 새 레코드 저장 직전에 UUID 를 자동으로 채워-넣고 싶다면 */
    @PrePersist
    private void init() {
        if (uuid == null) {
            uuid = UUID.randomUUID().toString();   // 36자 표준 형식
        }
    }
}