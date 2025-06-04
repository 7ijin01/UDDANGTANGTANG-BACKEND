package com.uddangtangtang.domain.compatibility.dto.request;

public record CompatibilityRequest(
        String myType,    // 현재는 코드(e.g. "A-B-A")
        String otherType  // 현재는 코드(e.g. "B-A-B")
) {
}
