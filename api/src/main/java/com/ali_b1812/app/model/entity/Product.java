package com.ali_b1812.app.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "sku", unique = true, length = 50)
    private String sku;

    @Column(name = "category", length = 100)
    private String category;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private Double price;

    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Column(name = "status", length = 20)
    private String status; // active, inactive, draft, archived

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "description", length = 2000)
    private String description;

    @Column(name = "cost", precision = 10, scale = 2)
    private Double cost;

    @Column(name = "compare_at_price", precision = 10, scale = 2)
    private Double compareAtPrice;

    @Column(name = "low_stock_threshold")
    private Integer lowStockThreshold;

    @Column(name = "track_inventory")
    @Builder.Default
    private Boolean trackInventory = true;

    @Column(name = "in_stock")
    @Builder.Default
    private Boolean inStock = true;

    @Column(name = "is_visible")
    @Builder.Default
    private Boolean isVisible = true;

    @Column(name = "is_featured")
    @Builder.Default
    private Boolean isFeatured = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}