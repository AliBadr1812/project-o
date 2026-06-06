package com.ali_b1812.app.dto.request;

import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Schema(description = "Request DTO for updating a product")
public class UpdateProductRequest {
    
    @NotBlank(message = "Product name is required")
    @Size(max = 255, message = "Product name must be less than 255 characters")
    @Schema(description = "Name of the product", example = "Apple iPhone 13")
    private String name;

    @NotBlank(message = "Product SKU is required")
    @Size(max = 100, message = "Product SKU must be less than 100 characters")
    @Schema(description = "Stock Keeping Unit (SKU) for the product", example = "SKU12345")
    private String sku;

    @Schema(description = "Description of the product", example = "The latest Apple iPhone with advanced features.")
    private String description;

    @Schema(description = "Price of the product", example = "999.99")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private Double price;

    @Schema(description = "Cost of the product", example = "599.99")
    @DecimalMin(value = "0.0", inclusive = false, message = "Cost must be greater than 0")
    private Double cost;

    @Schema(description = "Compare at price for the product", example = "1099.99")
    @DecimalMin(value = "0.0", inclusive = false, message = "Compare at price must be greater than 0")
    private Double compareAtPrice;

    @Schema(description = "Stock quantity available for the product", example = "50")
    @Min(value = 0, message = "Stock quantity cannot be negative")
    private Integer stock;

    @Schema(description = "Product status", example = "ACTIVE")
    private String status;

    @Schema(description = "Product categories", example = "ELECTRONICS")
    private String categories;

    @Schema(description = "Product specifications")
    private Map<String, String> specifications;
}
