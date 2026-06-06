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
@Schema(description = "Category response data")
public class CategoryResponse {

    @Schema(description = "Category ID", example = "1")
    private Long id;

    @Schema(description = "Category name", example = "Electronics")
    private String name;

    @Schema(description = "Category description", example = "Electronic devices and accessories")
    private String description;

    @Schema(description = "Category icon or image URL", example = "https://example.com/icons/electronics.png")
    private String icon;

    @Schema(description = "Number of products in this category", example = "42")
    private Integer productCount;

    @Schema(description = "Whether the category is active", example = "true")
    private Boolean isActive;

    @Schema(description = "Category creation timestamp")
    private LocalDateTime createdAt;
}
