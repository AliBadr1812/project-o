package com.ali_b1812.app.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Request DTO for updating a product")
public class UpdateCustomerRequest {
    
    @NotBlank(message = "Customer name is required")
    @Schema(description = "Name of the customer", example = "John Doe")
    private String fullName;
}
