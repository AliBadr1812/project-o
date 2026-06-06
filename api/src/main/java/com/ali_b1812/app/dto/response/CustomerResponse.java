package com.ali_b1812.app.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Customer response data")
public class CustomerResponse {

    @Schema(description = "Customer ID", example = "1")
    private Long id;

    @Schema(description = "Customer full name", example = "John Doe")
    private String fullName;

    @Schema(description = "Customer email", example = "john.doe@example.com")
    private String email;

    @Schema(description = "Customer phone number", example = "+1 (555) 123-4567")
    private String phone;

    @Schema(description = "Customer avatar URL", example = "https://example.com/avatar.jpg")
    private String avatarUrl;

    @Schema(description = "Number of orders placed by the customer", example = "5")
    private Integer orderCount;

    @Schema(description = "Total amount spent by the customer", example = "1250.00")
    private Double totalSpent;

    @Schema(description = "Date of the customer's last order")
    private LocalDateTime lastOrderDate;

    @Schema(description = "Customer status", example = "active")
    private String status;

    @Schema(description = "Customer type", example = "vip")
    private String type;

    @Schema(description = "Customer creation timestamp")
    private LocalDateTime createdAt;
}
