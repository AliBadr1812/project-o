package com.ali_b1812.app.controller;

import com.ali_b1812.app.dto.request.AuthRequest;
import com.ali_b1812.app.dto.response.ApiResponse;
import com.ali_b1812.app.dto.response.AuthResponse;
import com.ali_b1812.app.model.entity.User;
import com.ali_b1812.app.repository.UserRepository;
import com.ali_b1812.app.service.UserDetailsServiceImpl;
import com.ali_b1812.app.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Auth", description = "Login / logout")
public class AuthController {

    private final UserRepository         userRepository;
    private final UserDetailsServiceImpl userDetailsService;
    private final PasswordEncoder        passwordEncoder;
    private final JwtUtil                jwtUtil;

    /**
     * POST /api/auth/login
     * Body: { "email": "...", "password": "..." }
     * Returns: { "token": "...", "user": { id, name, email, role } }
     */
    @PostMapping("/login")
    @Operation(summary = "Authenticate and receive a JWT")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody AuthRequest request) {

        // 1. Look up user by email
        User user = userRepository.findByEmail(request.getEmail()).orElse(null);

        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getHashedPassword())) {
            log.warn("Failed login attempt for email: {}", request.getEmail());
            return ResponseEntity.status(401)
                    .body(ApiResponse.error("Invalid email or password", 401));
        }

        // 2. Guard: deactivated / locked
        if (!user.getIsActive()) {
            return ResponseEntity.status(403)
                    .body(ApiResponse.error("Account is deactivated. Contact an administrator.", 403));
        }

        if (user.isAccountLocked()) {
            return ResponseEntity.status(403)
                    .body(ApiResponse.error(
                            "Account is temporarily locked due to multiple failed attempts. " +
                            "Please try again later.", 403));
        }

        // 3. Record successful login and persist
        user.recordSuccessfulLogin();
        userRepository.save(user);

        // 4. Generate JWT
        var userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String token    = jwtUtil.generateToken(userDetails);

        // 5. Build response
        AuthResponse body = AuthResponse.builder()
                .token(token)
                .user(AuthResponse.UserDto.builder()
                        .id(user.getId())
                        .name(user.getFullName())
                        .email(user.getEmail())
                        .role(user.getRole().name())
                        .build())
                .build();

        log.info("Successful login for user: {} (id={})", user.getEmail(), user.getId());
        return ResponseEntity.ok(ApiResponse.success("Login successful", body));
    }

    /**
     * POST /api/auth/logout
     * JWTs are stateless — this just signals the client to discard its token.
     */
    @PostMapping("/logout")
    @Operation(summary = "Logout (client-side token discard)")
    public ResponseEntity<ApiResponse<Void>> logout() {
        return ResponseEntity.ok(ApiResponse.success("Logged out successfully", null));
    }
}
