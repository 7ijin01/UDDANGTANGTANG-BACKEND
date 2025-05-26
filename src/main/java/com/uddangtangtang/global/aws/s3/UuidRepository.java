package com.uddangtangtang.global.aws.s3;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UuidRepository extends JpaRepository<Uuid, Long> {

    /** 중복 확인용 */
    boolean existsByUuid(String uuid);

    /** 필요하다면 조회용 */
    Optional<Uuid> findByUuid(String uuid);
}
