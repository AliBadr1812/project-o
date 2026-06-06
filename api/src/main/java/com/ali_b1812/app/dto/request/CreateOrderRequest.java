package com.ali_b1812.app.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Request DTO for creating a new order")
public class CreateOrderRequest {

    @NotBlank(message = "Customer name is required")
    @Schema(description = "Name of the customer placing the order", example = "John Doe")
    private String customerName;

    @NotBlank(message = "Customer email is required")
    @Schema(description = "Email of the customer placing the order", example = "john.doe@example.com")
    private String customerEmail;

    @Schema(description = "Phone number of the customer", example = "+1 (555) 123-4567")
    private String customerPhone;

    @Schema(description = "ID of the associated Customer record (optional)", example = "1")
    private Long customerId;

    @NotBlank(message = "Order status is required")
    @Schema(description = "Status of the order", example = "pending")
    private String status;

    @NotNull(message = "Order total is required")
    @Schema(description = "Total amount for the order", example = "199.99")
    private Double total;

    @Schema(description = "Order subtotal before tax and shipping", example = "180.00")
    private Double subtotal;

    @Schema(description = "Shipping cost", example = "9.99")
    private Double shippingCost;

    @Schema(description = "Tax amount", example = "10.00")
    private Double tax;

    @Schema(description = "Discount amount", example = "0.00")
    private Double discount;

    @Schema(description = "Payment method used", example = "credit_card")
    private String paymentMethod;
}
