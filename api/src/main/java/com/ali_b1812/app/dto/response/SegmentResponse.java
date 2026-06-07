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
public class SegmentResponse {

    private Long id;
    private String name;
    private String description;
    /** Color hex used in the UI, e.g. "#7c5ef0" */
    private String color;
    /** Icon class, e.g. "fas fa-crown" */
    private String icon;
    private List<SegmentCriteria> criteria;
    private Integer customerCount;
    private Double totalRevenue;
    private Double avgOrderValue;
    private Boolean isSystem;
    private String createdAt;
    private String updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class SegmentCriteria {
        /**
         * CUSTOMER_TYPE | ORDER_COUNT_MIN | ORDER_COUNT_MAX |
         * TOTAL_SPENT_MIN | TOTAL_SPENT_MAX | LAST_ORDER_DAYS | STATUS
         */
        private String type;
        private String value;
        private String label;
    }
}
