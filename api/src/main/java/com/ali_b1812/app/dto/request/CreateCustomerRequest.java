package com.ali_b1812.app.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Request DTO for creating a new customer")
public class CreateCustomerRequest {

    @NotBlank(message = "Customer full name is required")
    @Schema(description = "Full name of the customer", example = "John Doe")
    private String fullName;

    @NotBlank(message = "Customer email is required")
    @Email(message = "Email should be valid")
    @Schema(description = "Email address of the customer", example = "john.doe@example.com")
    private String email;

    @Schema(description = "Phone number of the customer", example = "+1 (555) 123-4567")
    private String phone;

    @Schema(description = "Avatar URL for the customer", example = "https://example.com/avatar.jpg")
    private String avatarUrl;

    @Schema(description = "Customer status", example = "active")
    private String status;

    @Schema(description = "Customer type", example = "new")
    private String type;
}
