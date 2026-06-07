package com.ali_b1812.app.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReturnResponse {

    private Long id;
    private String returnNumber;
    private Long orderId;
    private String orderNumber;
    private Long customerId;
    private String customerName;
    private String customerEmail;
    /** DEFECTIVE | WRONG_ITEM | NOT_AS_DESCRIBED | CHANGED_MIND | OTHER */
    private String reason;
    private String reasonDetail;
    /** PENDING | APPROVED | REJECTED | REFUNDED | IN_TRANSIT */
    private String status;
    private Double refundAmount;
    /** ORIGINAL_PAYMENT | STORE_CREDIT | BANK_TRANSFER */
    private String refundMethod;
    private List<ReturnItemResponse> items;
    private String createdAt;
    private String updatedAt;
    private String staffNotes;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ReturnItemResponse {
        private Long productId;
        private String productName;
        private String sku;
        private Integer quantity;
        private Double unitPrice;
    }
}
