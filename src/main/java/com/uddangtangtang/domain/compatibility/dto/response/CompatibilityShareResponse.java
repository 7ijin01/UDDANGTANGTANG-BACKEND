package com.uddangtangtang.domain.compatibility.dto.response;

import com.uddangtangtang.domain.traveltype.dto.response.TravelScheduleResponse;

import java.util.List;

public record CompatibilityShareResponse(
        String result,
        String tips,
        String conflictPoints,
        TravelScheduleResponse recommendation,
        String shareId,
        String myTypeImage,
        String otherTypeImage

) {}
