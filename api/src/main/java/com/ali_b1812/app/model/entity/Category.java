package com.ali_b1812.app.model.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.Formula;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categories", uniqueConstraints = {
    @UniqueConstraint(columnNames = "name")
})
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"description", "products"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @EqualsAndHashCode.Include
    private String name;

    @Column(length = 2000)
    private String description;

    @Column(length = 500)
    private String icon;

    @Formula("(SELECT COUNT(*) FROM products p WHERE p.category_id = id)")
    private Integer productCount;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Product> products;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
