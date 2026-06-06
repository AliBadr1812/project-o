package com.ali_b1812.app.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Customer response data")
public class CustomerResponse {

    @Schema(description = "Customer ID", example = "1")
    private Long id;

    @Schema(description = "Customer name", example = "John Doe")
    private String fullName;
}
