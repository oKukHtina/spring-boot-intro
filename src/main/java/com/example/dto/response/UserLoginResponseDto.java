package com.example.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserLoginResponseDto(
        @Schema(example = "token")
        String token
) {
}
