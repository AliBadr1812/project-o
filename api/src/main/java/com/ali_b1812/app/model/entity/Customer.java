package com.ali_b1812.app.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customers", uniqueConstraints = {
    @UniqueConstraint(columnNames = "email")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"orders"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false)
    @EqualsAndHashCode.Include
    private String fullName;

    @Column(name = "email", nullable = false)
    @EqualsAndHashCode.Include
    private String email;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;

    @Column(name = "order_count")
    @Builder.Default
    private Integer orderCount = 0;

    @Column(name = "total_spent")
    @Builder.Default
    private Double totalSpent = 0.0;

    @Column(name = "last_order_date")
    private LocalDateTime lastOrderDate;

    @Column(name = "status", length = 20)
    private String status; // active, inactive

    @Column(name = "type", length = 20)
    private String type; // vip, returning, new

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Order> orders = new ArrayList<>();

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}