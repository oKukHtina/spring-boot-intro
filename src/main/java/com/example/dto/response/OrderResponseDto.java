package com.example.dto.response;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDto(
        @Schema(example = "1")
        Long id,
        @Schema(example = "4")
        Long userId,
        @ArraySchema(schema = @Schema(
                description = "Order item",
                implementation = OrderItemResponseDto.class
        ))
        List<OrderItemResponseDto> orderItems,
        @Schema(example = "2026-10-21T14:30:00")
        LocalDateTime orderDate,
        @Schema(example = "120.00")
        BigDecimal total,
        @Schema(example = "PENDING")
        String status
) {
}
