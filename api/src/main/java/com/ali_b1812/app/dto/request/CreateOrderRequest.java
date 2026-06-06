package com.ali_b1812.app.dto.request;

import com.ali_b1812.app.model.entity.User;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Response DTO for creating a new order")
public class CreateOrderRequest {
    
    @NotBlank(message = "Order ID is required")
    @Schema(description = "Unique identifier for the order", example = "1")
    private Long id;

    @Schema(description = "Order number", example = "ORD12345")
    private String orderNumber;

    @NotBlank(message = "Order status is required")
    @Schema(description = "Status of the order", example = "PENDING")
    private String status;

    @NotBlank(message = "Order total is required")
    @Schema(description = "Total amount for the order", example = "199.99")
    private Double total;

    @Schema(description = "Order creation timestamp")
    private String createdAt;

    @Schema(description = "Order processing timestamp")
    private String processedAt;

    @Schema(description = "Order shipping timestamp")
    private String shippedAt;

    @Schema(description = "Order delivery timestamp")
    private String deliveredAt;

    @Schema(description = "Customer associated with the order", example = "1")
    private User customer;

}
