package com.uddangtangtang.domain.compatibility.dto.request;

public record CompatibilityRequest(
        String myType,    // null or empty if unknown
        String otherType
) {}

