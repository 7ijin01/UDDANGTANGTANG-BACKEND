package com.uddangtangtang.global.AI.dto.response;

import java.util.List;

public record ChatGptResponse(
        List<Choice> choices
) {
    public record Choice(
            Message message
    ) {}

    public record Message(
            String role,
            String content
    ) {}
}
