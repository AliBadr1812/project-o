package com.ali_b1812.app.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Request DTO for updating a category")
public class UpdateCategoryRequest {

    @Schema(description = "Name of the category", example = "Electronics")
    private String name;

    @Schema(description = "Description of the category", example = "Electronic devices and accessories")
    private String description;

    @Schema(description = "Icon or image URL for the category", example = "https://example.com/icons/electronics.png")
    private String icon;

    @Schema(description = "Whether the category is active", example = "true")
    private Boolean isActive;
}
