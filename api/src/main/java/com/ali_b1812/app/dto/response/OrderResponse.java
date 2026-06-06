package com.ali_b1812.app.dto.response;

import com.ali_b1812.app.model.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Order response data")
public class OrderResponse {
    
    @Schema(description = "Order ID", example = "1")
    private Long id;

    @Schema(description = "Order number", example = "ORD12345")
    private String orderNumber;

    @Schema(description = "Order status", example = "PENDING")
    private String status;

    @Schema(description = "Total amount for the order", example = "199.99")
    private Double total;

    @Schema(description = "Order creation timestamp")
    private User customer;

    @Schema(description = "Order processing timestamp")
    private String processedAt;

    @Schema(description = "Order shipping timestamp")
    private String shippedAt;

    @Schema(description = "Order delivery timestamp")
    private String deliveredAt;
}
