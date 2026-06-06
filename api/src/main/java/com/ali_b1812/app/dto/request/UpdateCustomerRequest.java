package com.ali_b1812.app.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
@Schema(description = "Request DTO for updating a customer")
public class UpdateCustomerRequest {

    @Schema(description = "Full name of the customer", example = "John Doe")
    private String fullName;

    @Email(message = "Email should be valid")
    @Schema(description = "Email address of the customer", example = "john.doe@example.com")
    private String email;

    @Schema(description = "Phone number of the customer", example = "+1 (555) 123-4567")
    private String phone;

    @Schema(description = "Avatar URL for the customer", example = "https://example.com/avatar.jpg")
    private String avatarUrl;

    @Schema(description = "Customer status", example = "active")
    private String status;

    @Schema(description = "Customer type", example = "vip")
    private String type;
}
