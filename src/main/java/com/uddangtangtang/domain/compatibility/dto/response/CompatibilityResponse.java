package com.uddangtangtang.domain.compatibility.dto.response;

import java.util.List;

public record CompatibilityResponse(
        String result,
        String tips,
        String conflictPoints,
        List<String> recommendations,
        String shareId
) {}
