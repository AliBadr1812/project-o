package com.ali_b1812.app.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"items", "customer"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_number", nullable = false, unique = true, length = 100)
    @EqualsAndHashCode.Include
    private String orderNumber;

    @Column(name = "status", nullable = false, length = 50)
    private String status; // pending, processing, shipped, delivered, cancelled, refunded

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    @Column(name = "shipped_at")
    private LocalDateTime shippedAt;

    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;

    // Customer info (denormalized for performance)
    @Column(name = "customer_name", nullable = false, length = 255)
    private String customerName;

    @Column(name = "customer_email", nullable = false, length = 255)
    private String customerEmail;

    @Column(name = "customer_phone", length = 20)
    private String customerPhone;

    // Relationship to Customer (optional)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Customer customer;

    // Order items - stored as JSON in database or in separate table
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();

    // Shipping address - stored as embeddable object
    @Embedded
    private ShippingAddress shippingAddress;

    // Financials
    @Column(name = "subtotal")
    private Double subtotal;

    @Column(name = "shipping_cost")
    private Double shippingCost;

    @Column(name = "tax")
    private Double tax;

    @Column(name = "total")
    private Double total;

    @Column(name = "discount")
    @Builder.Default
    private Double discount = 0.0;

    @Column(name = "payment_method", length = 100)
    private String paymentMethod;

    // Notes - stored as JSON or in separate table
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<OrderNote> notes = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (orderNumber == null) {
            orderNumber = "ORD-" + System.currentTimeMillis();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        if ("processing".equals(status) && processedAt == null) {
            processedAt = LocalDateTime.now();
        } else if ("shipped".equals(status) && shippedAt == null) {
            shippedAt = LocalDateTime.now();
        } else if ("delivered".equals(status) && deliveredAt == null) {
            deliveredAt = LocalDateTime.now();
        }
    }
}