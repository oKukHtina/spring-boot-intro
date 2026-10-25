package com.example.dto.request;

import com.example.entity.Order;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record StatusRequestDto(
        @NotNull
        @Schema(
                description = """
                        Order status:
                        PENDING — order is awaiting processing.
                        DELIVERED — order has been delivered.
                        COMPLETED — order is completed.
                        """,
                example = "PENDING"
        )
        Order.Status status
) {
}
