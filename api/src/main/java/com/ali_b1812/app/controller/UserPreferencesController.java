package com.ali_b1812.app.controller;

import com.ali_b1812.app.dto.response.ApiResponse;
import com.ali_b1812.app.dto.response.UserPreferencesResponse;
import com.ali_b1812.app.service.UserPreferencesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user-preferences")
@RequiredArgsConstructor
@Tag(name = "User Preferences", description = "Persist user UI preferences such as theme and dark mode")
public class UserPreferencesController {

    private final UserPreferencesService service;

    /**
     * GET /user-preferences/default
     * Convenience endpoint — the frontend calls this when no specific userId is
     * known yet. Returns empty preferences (null data) so the UI uses its own defaults.
     */
    @GetMapping("/default")
    @Operation(summary = "Get default (anonymous) preferences")
    public ResponseEntity<ApiResponse<UserPreferencesResponse>> getDefault() {
        return ResponseEntity.ok(ApiResponse.success("No preferences saved yet", null));
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get preferences for a user")
    public ResponseEntity<ApiResponse<UserPreferencesResponse>> get(@PathVariable String userId) {
        return service.getPreferences(userId)
                .map(prefs -> ResponseEntity.ok(ApiResponse.success("OK", prefs)))
                .orElse(ResponseEntity.ok(ApiResponse.success("No preferences saved yet", null)));
    }

    @PutMapping("/{userId}")
    @Operation(summary = "Save or update preferences for a user")
    public ResponseEntity<ApiResponse<UserPreferencesResponse>> save(
            @PathVariable String userId,
            @RequestBody UserPreferencesResponse patch) {
        UserPreferencesResponse saved = service.savePreferences(userId, patch);
        return ResponseEntity.ok(ApiResponse.success("Preferences saved", saved));
    }
}
