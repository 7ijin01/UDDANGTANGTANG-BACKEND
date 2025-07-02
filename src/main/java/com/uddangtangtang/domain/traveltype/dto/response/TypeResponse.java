package com.uddangtangtang.domain.traveltype.dto.response;


import com.uddangtangtang.domain.traveltype.domain.TourSpot;

import java.util.List;

public record TypeResponse(
        String code,
        String reason,
        String image,
        String description,
        String typeName,
        TravelScheduleResponse recommendation,
        String shareId

) {

}
