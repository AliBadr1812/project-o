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

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

}
