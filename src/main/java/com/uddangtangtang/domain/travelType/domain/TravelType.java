package com.uddangtangtang.domain.travelType.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "TravelType")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TravelType
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int type_id;
    private String code;//해당 유형 코드 ex)A-A-A-B
    private String type_name;// 해당 유형 이름
    private String type_description;//해당 유형 설명
    private String image;//해당 유형 캐릭터 저장
    private String trip_recommand;// 여행지 추천임다
}
