package com.example.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserResponseDto {
    @Schema(example = "1")
    private Long id;
    @Schema(example = "anonimus123@gmail.com")
    private String email;
    @Schema(example = "Bob")
    private String firstName;
    @Schema(example = "Bobchanskiy")
    private String lastName;
    @Schema(example = "15 Shevchenko Street, Kyiv")
    private String shippingAddress;
}
