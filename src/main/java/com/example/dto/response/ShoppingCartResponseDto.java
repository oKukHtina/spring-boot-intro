package com.example.dto.response;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record ShoppingCartResponseDto(
        @Schema(example = "4")
        Long id,
        @Schema(example = "4")
        Long userId,

        @ArraySchema(schema = @Schema(
                implementation = CartItemResponseDto.class
        ))
        List<CartItemResponseDto> cartItems
) {
}
