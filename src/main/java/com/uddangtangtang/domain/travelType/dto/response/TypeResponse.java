package com.uddangtangtang.domain.travelType.dto.response;

import lombok.Getter;

public record TypeResponse(
        String code,
        String reason,
        String keyWord,
        String image,
        String description,
        String type_name,
        String trip_recommand

) {

}
