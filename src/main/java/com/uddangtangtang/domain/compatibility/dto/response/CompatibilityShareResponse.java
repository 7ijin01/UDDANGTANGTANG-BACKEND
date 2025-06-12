package com.uddangtangtang.domain.compatibility.dto.response;

import java.util.List;

public record CompatibilityShareResponse(
        String result,
        String tips,
        String conflictPoints,
        List<String> recommendations,
        String shareId,
        String myTypeImage,
        String otherTypeImage

) {}
