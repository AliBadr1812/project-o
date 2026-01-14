package com.ali_b1812.app.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ali_b1812.app.model.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "User response data")
public class UserResponse {
    
    @Schema(description = "User ID", example = "1")
    private Long id;
    
    @Schema(description = "Username", example = "john_doe")
    private String username;
    
    @Schema(description = "Email", example = "john@example.com")
    private String email;
    
    @Schema(description = "User role", example = "USER")
    private UserRole role;
    
    @Schema(description = "First name", example = "John")
    private String firstName;
    
    @Schema(description = "Last name", example = "Doe")
    private String lastName;
    
    @Schema(description = "Phone number", example = "+1234567890")
    private String phoneNumber;
    
    @Schema(description = "Profile image URL")
    private String profileImageUrl;
    
    @Schema(description = "Is user active", example = "true")
    private Boolean isActive;
    
    @Schema(description = "Account creation timestamp")
    private LocalDateTime createdAt;
    
    @Schema(description = "Last update timestamp")
    private LocalDateTime updatedAt;
    
    @Schema(description = "Last login timestamp")
    private LocalDateTime lastLoginAt;
    
    @Schema(description = "Email verification timestamp")
    private LocalDateTime emailVerifiedAt;
    
    @Schema(description = "Number of followers", example = "150")
    private Integer followersCount;
    
    @Schema(description = "Number of following", example = "75")
    private Integer followingCount;
}