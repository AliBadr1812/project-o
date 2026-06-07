package com.ali_b1812.app.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Discount / coupon code response")
public class DiscountResponse {

    @Schema(description = "Discount ID", example = "1")
    private Long id;

    @Schema(description = "Coupon code", example = "SUMMER20")
    private String code;

    @Schema(description = "Human-readable description", example = "20% off summer sale")
    private String description;

    @Schema(description = "Discount type: PERCENTAGE or FIXED", example = "PERCENTAGE")
    private String type;

    @Schema(description = "Discount value — percent (e.g. 20) or fixed amount (e.g. 10.00)", example = "20.0")
    private Double value;

    @Schema(description = "Minimum order amount required to apply the code", example = "50.0")
    private Double minimumOrderAmount;

    @Schema(description = "Maximum number of times the code can be used (null = unlimited)", example = "100")
    private Integer usageLimit;

    @Schema(description = "Number of times the code has been used so far", example = "42")
    private Integer usedCount;

    @Schema(description = "Whether the discount is currently active", example = "true")
    private Boolean isActive;

    @Schema(description = "Valid-from date (ISO-8601)", example = "2025-01-01T00:00:00")
    private String startDate;

    @Schema(description = "Expiry date (ISO-8601)", example = "2025-12-31T23:59:59")
    private String endDate;

    @Schema(description = "Creation timestamp (ISO-8601)")
    private String createdAt;
}
