package com.uddangtangtang.domain.traveltype.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "TravelType")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TravelType
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "type_id")
    private Long id;
    private String code;//해당 유형 코드 ex)A-A-A-B

    @Column(name = "type_name")
    private String typeName;// 해당 유형 이름

    @Column(name = "type_description")
    @Lob
    private String typeDescription;//해당 유형 설명

    private String image;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String travelScheduleJson;


}
