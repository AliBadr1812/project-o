package com.ali_b1812.app.model.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.ali_b1812.app.model.enums.UserRole;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users", uniqueConstraints = {
    @UniqueConstraint(columnNames = "userName"),
    @UniqueConstraint(columnNames = "email")
    },
    indexes = {
        @Index(columnList = "email"),
        @Index(columnList = "username"),
        @Index(columnList = "is_active")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"hashedPassword", "following", "followers"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private UserRole role = UserRole.USER;

    @Column(name = "username", nullable = false)
    @EqualsAndHashCode.Include
    private String userName;

    @Column(name = "first_name", nullable = true)
    private String firstName;

    @Column(name = "last_name", nullable = true)
    private String lastName;

    @Column(name = "email", nullable = false)
    @EqualsAndHashCode.Include
    private String email;

    @Column(name = "hashed_password", nullable = false)
    private String hashedPassword;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;
    
    @Column(name = "profile_image_url", length = 500)
    private String profileImageUrl;

    @ManyToMany
    @JoinTable(
        name = "user_follows",
        joinColumns = @JoinColumn(name = "follower_id"), 
        inverseJoinColumns = @JoinColumn(name = "following_id")
    )
    @Builder.Default
    private Set<User> following = new HashSet<>();
    
    @ManyToMany(mappedBy = "following")
    @Builder.Default
    private Set<User> followers = new HashSet<>();

    @Column(name = "failed_login_attempts")
    @Builder.Default
    private Integer failedLoginAttempts = 0;

    @Column(name = "account_locked_until")
    private LocalDateTime accountLockedUntil;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @Column(name = "email_verified_at")
    private LocalDateTime emailVerifiedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    // Business methods
    public String getFullName() {
        if (firstName == null && lastName == null) return userName;
        if (firstName == null) return lastName;
        if (lastName == null) return firstName;
        return firstName + " " + lastName;
    }
    
    public boolean isAccountLocked() {
        return accountLockedUntil != null && 
               accountLockedUntil.isAfter(LocalDateTime.now());
    }
    
    public boolean isEmailVerified() {
        return emailVerifiedAt != null;
    }
    
    public void recordSuccessfulLogin() {
        this.lastLoginAt = LocalDateTime.now();
        this.failedLoginAttempts = 0;
        this.accountLockedUntil = null;
    }
    
    public void recordFailedLogin() {
        this.failedLoginAttempts++;
        if (this.failedLoginAttempts >= 5) {
            this.accountLockedUntil = LocalDateTime.now().plusMinutes(15);
        }
    }

}
