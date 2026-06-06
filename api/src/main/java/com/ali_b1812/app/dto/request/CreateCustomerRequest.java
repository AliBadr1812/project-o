package com.ali_b1812.app.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Response DTO for creating a new customer")
public class CreateCustomerRequest {
    
    @NotBlank(message = "Customer ID is required")
    @Schema(description = "Unique identifier for the customer", example = "1")
    private Long id;
}
