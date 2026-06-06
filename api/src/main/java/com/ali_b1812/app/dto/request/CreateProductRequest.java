package com.ali_b1812.app.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Request DTO for creating a new product")
public class CreateProductRequest {

    @NotBlank(message = "Product name is required")
    @Schema(description = "Name of the product", example = "Apple iPhone 13")
    private String name;

    @Schema(description = "Stock Keeping Unit (SKU) for the product", example = "SKU12345")
    private String sku;

    @Schema(description = "Description of the product", example = "The latest Apple iPhone with advanced features.")
    private String description;

    @NotNull(message = "Product price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @Schema(description = "Price of the product", example = "999.99")
    private Double price;

    @Schema(description = "Cost of the product", example = "599.99")
    private Double cost;

    @Schema(description = "Compare at price for the product", example = "1099.99")
    private Double compareAtPrice;

    @NotNull(message = "Stock quantity is required")
    @Min(value = 0, message = "Stock quantity cannot be negative")
    @Schema(description = "Stock quantity available for the product", example = "50")
    private Integer stock;

    @Schema(description = "Product status", example = "active")
    private String status;

    @Schema(description = "Image URL for the product")
    private String imageUrl;

    @Schema(description = "Category ID for the product", example = "1")
    private Long categoryId;
}
