package com.ali_b1812.app.dto.response;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Product response data")
public class ProductResponse {

    @Schema(description = "Product ID", example = "1")
    private Long id;
    
    @Schema(description = "Product name", example = "Smartphone XYZ")
    private String name;

    @Schema(description = "Product sku", example = "SKU12345")
    private String sku;

    @Schema(description = "Product status", example = "ACTIVE")
    private String status;

    @Schema(description = "Product description", example = "A high-end smartphone with great features.")
    private String description;

    @Schema(description = "Product image URLs")
    private List<String> images;

    @Schema(description = "Product price", example = "499.99")
    private Double price;

    @Schema(description = "Product cost", example = "299.99")
    private Double cost;

    @Schema(description = "Compare at price", example = "599.99")
    private Double compareAtPrice;

    @Schema(description = "Stock quantity", example = "50")
    private Integer stock;

    @Schema(description = "Low stock threshold", example = "10")
    private Integer lowStockThreshold;

    @Schema(description = "Track inventory", example = "true")
    private Boolean trackInventory;

    @Schema(description = "In stock", example = "true")
    private Boolean inStock;

    @Schema(description = "Product categories", example = "ELECTRONICS")
    private String categories;

    @Schema(description = "Product specifications")
    private Map<String, String> specifications;

    @Schema(description = "Average rating", example = "4.5")
    private Double averageRating;

    @Schema(description = "Review count", example = "150")
    private Integer reviewCount;

    @Schema(description = "Sales data (wip)")
    private String salesData;

    @Schema(description = "Is visible", example = "true")
    private Boolean isVisible;

    @Schema(description = "Product featured", example = "true")
    private Boolean isFeatured;

    @Schema(description = "Product created at")
    private String createdAt;

    @Schema(description = "Product updated at")
    private String updatedAt;

    @Schema(description = "Product weight", example = "0.5")
    private Double weight;

    @Schema(description = "Weight unit", example = "kg")
    private String weightUnit;

    @Schema(description = "dimensions", example = "10x5x2 cm")
    private String dimensions;
}
