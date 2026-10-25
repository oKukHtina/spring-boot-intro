package com.example.dto.request;

import com.example.validation.annotation.FieldMatch;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@FieldMatch
@Data
public class UserRegistrationRequestDto {
    @Schema(example = "anonimus@gmail.com")
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @Schema(example = "pasword****")
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @Schema(example = "pasword****")
    @NotBlank(message = "Repeat password is required")
    private String repeatPassword;

    @Schema(example = "Bob")
    @NotBlank(message = "First name cannot be blank")
    private String firstName;

    @Schema(example = "Bobchynskiy")
    @NotBlank(message = "Last name cannot be blank")
    private String lastName;

    @Schema(example = "15 Shevchenko Street, Kyiv")
    private String shippingAddress;
}
