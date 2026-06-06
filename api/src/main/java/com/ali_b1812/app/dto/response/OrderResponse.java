package com.ali_b1812.app.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Order response data")
public class OrderResponse {

    @Schema(description = "Order ID", example = "1")
    private Long id;

    @Schema(description = "Order number", example = "ORD-2024-001234")
    private String orderNumber;

    @Schema(description = "Order status", example = "pending")
    private String status;

    @Schema(description = "Customer name", example = "John Doe")
    private String customerName;

    @Schema(description = "Customer email", example = "john.doe@example.com")
    private String customerEmail;

    @Schema(description = "Customer phone", example = "+1 (555) 123-4567")
    private String customerPhone;

    @Schema(description = "ID of the linked Customer record, if any", example = "1")
    private Long customerId;

    @Schema(description = "Order subtotal before tax and shipping", example = "180.00")
    private Double subtotal;

    @Schema(description = "Shipping cost", example = "9.99")
    private Double shippingCost;

    @Schema(description = "Tax amount", example = "10.00")
    private Double tax;

    @Schema(description = "Total amount for the order", example = "199.99")
    private Double total;

    @Schema(description = "Discount applied to the order", example = "0.00")
    private Double discount;

    @Schema(description = "Payment method used", example = "credit_card")
    private String paymentMethod;

    @Schema(description = "Order creation timestamp")
    private LocalDateTime createdAt;

    @Schema(description = "Order processing timestamp")
    private LocalDateTime processedAt;

    @Schema(description = "Order shipping timestamp")
    private LocalDateTime shippedAt;

    @Schema(description = "Order delivery timestamp")
    private LocalDateTime deliveredAt;
}
