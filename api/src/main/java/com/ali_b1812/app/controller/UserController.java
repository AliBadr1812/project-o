package com.ali_b1812.app.controller;

import com.ali_b1812.app.dto.request.CreateUserRequest;
import com.ali_b1812.app.dto.request.UpdateUserRequest;
import com.ali_b1812.app.dto.response.ApiResponse;
import com.ali_b1812.app.dto.response.UserResponse;
import com.ali_b1812.app.model.enums.UserRole;
import com.ali_b1812.app.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * REST controller for managing users.
 * 
 * Provides endpoints for user registration, retrieval, update, and deletion.
 * Includes role-based access control and comprehensive validation.
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Users", description = "User management endpoints")
@SecurityRequirement(name = "bearerAuth")  // For Swagger/OpenAPI
public class UserController {

    private final UserService userService;
    
    // ============ PUBLIC ENDPOINTS ============
    
    /**
     * Register a new user.
     * 
     * This endpoint is publicly accessible for user registration.
     * Creates a new user with USER role by default.
     */
    @Operation(
        summary = "Register a new user",
        description = "Creates a new user account. Returns the created user with location header."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "User created successfully",
            content = @Content(mediaType = "application/json", 
                     schema = @Schema(implementation = ApiResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Invalid input data"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "409",
            description = "Username or email already exists"
        )
    })
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse<UserResponse>> registerUser(
            @Valid @RequestBody CreateUserRequest request) {
        
        log.info("Registering new user with username: {}", request.getUsername());
        
        UserResponse createdUser = userService.createUser(request);
        
        // Build location header
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(createdUser.getId())
            .toUri();
        
        ApiResponse<UserResponse> response = ApiResponse.success(
            "User registered successfully",
            createdUser
        );
        
        return ResponseEntity
            .created(location)
            .body(response);
    }
    
    // ============ AUTHENTICATED ENDPOINTS ============
    
    /**
     * Get current user profile.
     * 
     * Returns the profile of the currently authenticated user.
     */
    @Operation(summary = "Get current user profile")
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser(
            @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipal currentUser) {
        
        log.debug("Getting profile for current user: {}", currentUser.getUsername());
        
        UserResponse user = userService.getUserById(currentUser.getId());
        
        return ResponseEntity.ok(
            ApiResponse.success("User profile retrieved", user)
        );
    }
    
    /**
     * Update current user profile.
     */
    @Operation(summary = "Update current user profile")
    @PutMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<UserResponse>> updateCurrentUser(
            @Valid @RequestBody UpdateUserRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipal currentUser) {
        
        log.info("Updating profile for user ID: {}", currentUser.getId());
        
        UserResponse updatedUser = userService.updateUser(currentUser.getId(), request);
        
        return ResponseEntity.ok(
            ApiResponse.success("Profile updated successfully", updatedUser)
        );
    }
    
    /**
     * Upload profile picture for current user.
     */
    @Operation(summary = "Upload profile picture")
    @PostMapping(value = "/me/profile-picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<String>> uploadProfilePicture(
            @RequestParam("file") MultipartFile file,
            @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipal currentUser) {
        
        log.info("Uploading profile picture for user ID: {}", currentUser.getId());
        
        String imageUrl = userService.uploadProfilePicture(currentUser.getId(), file);
        
        return ResponseEntity.ok(
            ApiResponse.success("Profile picture uploaded successfully", imageUrl)
        );
    }
    
    // ============ ADMIN ENDPOINTS ============
    
    /**
     * Get all users (paginated).
     * 
     * Only accessible by users with ADMIN or MODERATOR role.
     * Supports sorting, filtering, and pagination.
     */
    @Operation(
        summary = "Get all users (paginated)",
        description = "Retrieve paginated list of users. Only accessible by ADMIN or MODERATOR."
    )
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getAllUsers(
            @ParameterObject 
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) 
            Pageable pageable,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) UserRole role,
            @RequestParam(required = false) Boolean isActive) {
        
        log.debug("Getting all users - page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());
        
        Page<UserResponse> users = userService.getAllUsers(pageable, search, role, isActive);
        
        return ResponseEntity.ok(
            ApiResponse.success("Users retrieved successfully", users)
        );
    }
    
    /**
     * Get user by ID.
     */
    @Operation(summary = "Get user by ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR') or @securityService.isOwner(#id, principal)")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(
            @Parameter(description = "User ID", example = "1") 
            @PathVariable Long id) {
        
        log.debug("Getting user by ID: {}", id);
        
        UserResponse user = userService.getUserById(id);
        
        return ResponseEntity.ok(
            ApiResponse.success("User retrieved successfully", user)
        );
    }
    
    /**
     * Create a new user (admin only).
     * 
     * Allows specifying user role (unlike public registration).
     */
    @Operation(summary = "Create a new user (admin only)")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse<UserResponse>> createUser(
            @Valid @RequestBody CreateUserRequest request,
            @RequestParam(required = false, defaultValue = "USER") UserRole role) {
        
        log.info("Admin creating user with username: {}, role: {}", request.getUsername(), role);
        
        UserResponse createdUser = userService.createUserWithRole(request, role);
        
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(createdUser.getId())
            .toUri();
        
        return ResponseEntity
            .created(location)
            .body(ApiResponse.success("User created successfully", createdUser));
    }
    
    /**
     * Update user by ID.
     */
    @Operation(summary = "Update user by ID")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isOwner(#id, principal)")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        
        log.info("Updating user ID: {}", id);
        
        UserResponse updatedUser = userService.updateUser(id, request);
        
        return ResponseEntity.ok(
            ApiResponse.success("User updated successfully", updatedUser)
        );
    }
    
    /**
     * Update user role (admin only).
     */
    @Operation(summary = "Update user role")
    @PatchMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> updateUserRole(
            @PathVariable Long id,
            @RequestParam UserRole role) {
        
        log.info("Updating role for user ID: {} to {}", id, role);
        
        UserResponse updatedUser = userService.updateUserRole(id, role);
        
        return ResponseEntity.ok(
            ApiResponse.success("User role updated successfully", updatedUser)
        );
    }
    
    /**
     * Activate/deactivate user (admin only).
     */
    @Operation(summary = "Activate/deactivate user")
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> updateUserStatus(
            @PathVariable Long id,
            @RequestParam Boolean isActive) {
        
        log.info("Updating status for user ID: {} to {}", id, isActive);
        
        UserResponse updatedUser = userService.updateUserStatus(id, isActive);
        
        return ResponseEntity.ok(
            ApiResponse.success(
                isActive ? "User activated successfully" : "User deactivated successfully",
                updatedUser
            )
        );
    }
    
    /**
     * Delete user by ID.
     * 
     * Soft delete by default (sets deletedAt timestamp).
     * Permanent delete with ?permanent=true parameter.
     */
    @Operation(summary = "Delete user")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "false") Boolean permanent) {
        
        log.warn("Deleting user ID: {}, permanent: {}", id, permanent);
        
        if (permanent) {
            userService.deleteUserPermanently(id);
            return ResponseEntity.ok(
                ApiResponse.success("User permanently deleted", null)
            );
        } else {
            userService.deleteUser(id);
            return ResponseEntity.ok(
                ApiResponse.success("User deactivated (soft delete)", null)
            );
        }
    }
    
    /**
     * Get user statistics (admin only).
     */
    @Operation(summary = "Get user statistics")
    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getUserStatistics() {
        
        log.debug("Getting user statistics");
        
        Map<String, Object> stats = userService.getUserStatistics();
        
        return ResponseEntity.ok(
            ApiResponse.success("Statistics retrieved successfully", stats)
        );
    }
    
    /**
     * Export users to CSV (admin only).
     */
    @Operation(summary = "Export users to CSV")
    @GetMapping(value = "/export/csv", produces = "text/csv")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<byte[]> exportUsersToCsv() {
        
        log.info("Exporting users to CSV");
        
        byte[] csvData = userService.exportUsersToCsv();
        
        return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=users-export.csv")
            .contentType(MediaType.parseMediaType("text/csv"))
            .body(csvData);
    }
    
    // ============ FOLLOWERS/FOLLOWING ENDPOINTS ============
    
    /**
     * Follow a user.
     */
    @Operation(summary = "Follow a user")
    @PostMapping("/{id}/follow")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Void>> followUser(
            @PathVariable Long id,
            @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipal currentUser) {
        
        log.info("User {} following user {}", currentUser.getId(), id);
        
        userService.followUser(currentUser.getId(), id);
        
        return ResponseEntity.ok(
            ApiResponse.success("Successfully followed user", null)
        );
    }
    
    /**
     * Unfollow a user.
     */
    @Operation(summary = "Unfollow a user")
    @DeleteMapping("/{id}/follow")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Void>> unfollowUser(
            @PathVariable Long id,
            @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipal currentUser) {
        
        log.info("User {} unfollowing user {}", currentUser.getId(), id);
        
        userService.unfollowUser(currentUser.getId(), id);
        
        return ResponseEntity.ok(
            ApiResponse.success("Successfully unfollowed user", null)
        );
    }
    
    /**
     * Get user's followers.
     */
    @Operation(summary = "Get user's followers")
    @GetMapping("/{id}/followers")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getFollowers(
            @PathVariable Long id,
            @ParameterObject 
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) 
            Pageable pageable) {
        
        log.debug("Getting followers for user ID: {}", id);
        
        Page<UserResponse> followers = userService.getFollowers(id, pageable);
        
        return ResponseEntity.ok(
            ApiResponse.success("Followers retrieved successfully", followers)
        );
    }
    
    /**
     * Get users that this user follows.
     */
    @Operation(summary = "Get user's following")
    @GetMapping("/{id}/following")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getFollowing(
            @PathVariable Long id,
            @ParameterObject 
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) 
            Pageable pageable) {
        
        log.debug("Getting following for user ID: {}", id);
        
        Page<UserResponse> following = userService.getFollowing(id, pageable);
        
        return ResponseEntity.ok(
            ApiResponse.success("Following retrieved successfully", following)
        );
    }
}