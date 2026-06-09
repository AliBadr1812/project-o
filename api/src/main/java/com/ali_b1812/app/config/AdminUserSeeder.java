package com.ali_b1812.app.config;

import com.ali_b1812.app.model.entity.User;
import com.ali_b1812.app.model.enums.UserRole;
import com.ali_b1812.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Seeds a default administrator account on first startup so that the
 * application is immediately usable without manual DB inserts.
 *
 * Credentials (dev only):
 *   email:    admin@shopadmin.com
 *   password: admin123
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AdminUserSeeder implements ApplicationRunner {

    private final UserRepository  userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        if (userRepository.existsByEmail("admin@shopadmin.com")) {
            return; // already seeded
        }

        User admin = User.builder()
                .userName("admin")
                .email("admin@shopadmin.com")
                .firstName("Admin")
                .lastName("User")
                .hashedPassword(passwordEncoder.encode("admin123"))
                .role(UserRole.ADMIN)
                .isActive(true)
                .build();

        userRepository.save(admin);
        log.info("=======================================================");
        log.info("  Default admin account created:");
        log.info("    Email:    admin@shopadmin.com");
        log.info("    Password: admin123");
        log.info("  Change this password before deploying to production!");
        log.info("=======================================================");
    }
}
