package com.ali_b1812.app.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserPreferencesResponse {

    /** User identifier — defaults to "default" for a single-user admin panel */
    private String userId;
    /** "purple" | "blue" | "green" | "orange" */
    private String themeId;
    /** true = dark mode, false = light mode */
    private Boolean darkMode;
    private String updatedAt;
}
