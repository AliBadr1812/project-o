package com.ali_b1812.app.mockdata;

import com.ali_b1812.app.dto.response.ProductResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class ProductMockData {

    private final List<ProductResponse> products = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @PostConstruct
    public void init() {
        products.add(build(idGenerator.getAndIncrement(),
                "Premium Wireless Headphones", "PHONES-001", "Electronics",
                149.99, 75.00, 25, "active", true,
                "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=400&h=400&fit=crop",
                "High-quality wireless headphones with active noise cancellation and 30-hour battery life.",
                "2024-01-15T10:30:00"));

        ProductResponse tshirt = build(idGenerator.getAndIncrement(),
                "Organic Cotton T-Shirt", "CLOTH-045", "Clothing",
                24.99, 9.00, 15, "active", false,
                "https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=400&h=400&fit=crop",
                "Soft, breathable organic cotton t-shirt available in multiple colours.",
                "2024-01-14T14:20:00");
        tshirt.setVariants(List.of(
            ProductResponse.ProductVariantResponse.builder().id(1L).options("White / S").sku("CLOTH-045-WS").price(24.99).stock(5).isAvailable(true).build(),
            ProductResponse.ProductVariantResponse.builder().id(2L).options("White / M").sku("CLOTH-045-WM").price(24.99).stock(4).isAvailable(true).build(),
            ProductResponse.ProductVariantResponse.builder().id(3L).options("White / L").sku("CLOTH-045-WL").price(24.99).stock(3).isAvailable(true).build(),
            ProductResponse.ProductVariantResponse.builder().id(4L).options("Black / S").sku("CLOTH-045-BS").price(24.99).stock(2).isAvailable(true).build(),
            ProductResponse.ProductVariantResponse.builder().id(5L).options("Black / M").sku("CLOTH-045-BM").price(24.99).stock(1).isAvailable(true).build(),
            ProductResponse.ProductVariantResponse.builder().id(6L).options("Black / L").sku("CLOTH-045-BL").price(24.99).stock(0).isAvailable(false).build()
        ));
        products.add(tshirt);

        ProductResponse bottle = build(idGenerator.getAndIncrement(),
                "Stainless Steel Water Bottle", "HOME-023", "Home & Kitchen",
                29.99, 11.00, 8, "active", false,
                "https://images.unsplash.com/photo-1523362628745-0c100150b504?w=400&h=400&fit=crop",
                "Double-walled insulated stainless steel bottle, keeps drinks cold 24 h / hot 12 h.",
                "2024-01-13T09:15:00");
        bottle.setVariants(List.of(
            ProductResponse.ProductVariantResponse.builder().id(7L).options("Silver / 500ml").sku("HOME-023-SIL-S").price(29.99).stock(4).isAvailable(true).build(),
            ProductResponse.ProductVariantResponse.builder().id(8L).options("Silver / 750ml").sku("HOME-023-SIL-L").price(34.99).stock(2).isAvailable(true).build(),
            ProductResponse.ProductVariantResponse.builder().id(9L).options("Matte Black / 500ml").sku("HOME-023-BLK-S").price(29.99).stock(2).isAvailable(true).build(),
            ProductResponse.ProductVariantResponse.builder().id(10L).options("Matte Black / 750ml").sku("HOME-023-BLK-L").price(34.99).stock(0).isAvailable(false).build()
        ));
        products.add(bottle);

        products.add(build(idGenerator.getAndIncrement(),
                "Yoga Mat Premium", "FIT-078", "Fitness",
                39.99, 14.00, 0, "inactive", false,
                "https://images.unsplash.com/photo-1599901860904-17e6ed7083a0?w=400&h=400&fit=crop",
                "Non-slip 6mm thick yoga mat with alignment lines and carry strap.",
                "2024-01-12T16:45:00"));

        products.add(build(idGenerator.getAndIncrement(),
                "Bluetooth Speaker", "ELECT-012", "Electronics",
                89.99, 38.00, 32, "active", true,
                "https://images.unsplash.com/photo-1546435770-a3e426bf472b?w=400&h=400&fit=crop",
                "360° surround sound portable speaker with 20-hour battery and IPX7 waterproofing.",
                "2024-01-11T11:30:00"));

        products.add(build(idGenerator.getAndIncrement(),
                "Coffee Maker", "HOME-067", "Home & Kitchen",
                129.99, 52.00, 5, "active", false,
                "https://images.unsplash.com/photo-1495474472287-4d71bcdd2085?w=400&h=400&fit=crop",
                "Programmable 12-cup coffee maker with thermal carafe and auto-clean feature.",
                "2024-01-10T13:20:00"));

        ProductResponse shoes = build(idGenerator.getAndIncrement(),
                "Running Shoes", "SHOE-034", "Footwear",
                79.99, 30.00, 12, "active", false,
                "https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=400&h=400&fit=crop",
                "Lightweight breathable running shoes with responsive cushioning sole.",
                "2024-01-09T15:10:00");
        shoes.setVariants(List.of(
            ProductResponse.ProductVariantResponse.builder().id(11L).options("US 8").sku("SHOE-034-8").price(79.99).stock(3).isAvailable(true).build(),
            ProductResponse.ProductVariantResponse.builder().id(12L).options("US 9").sku("SHOE-034-9").price(79.99).stock(4).isAvailable(true).build(),
            ProductResponse.ProductVariantResponse.builder().id(13L).options("US 10").sku("SHOE-034-10").price(79.99).stock(3).isAvailable(true).build(),
            ProductResponse.ProductVariantResponse.builder().id(14L).options("US 11").sku("SHOE-034-11").price(79.99).stock(2).isAvailable(true).build(),
            ProductResponse.ProductVariantResponse.builder().id(15L).options("US 12").sku("SHOE-034-12").price(79.99).stock(0).isAvailable(false).build()
        ));
        products.add(shoes);

        products.add(build(idGenerator.getAndIncrement(),
                "Backpack", "BAG-021", "Accessories",
                49.99, 18.00, 18, "active", false,
                "https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=400&h=400&fit=crop",
                "30L waterproof backpack with USB charging port and laptop compartment.",
                "2024-01-08T10:45:00"));

        products.add(build(idGenerator.getAndIncrement(),
                "Smart Watch", "ELECT-089", "Electronics",
                199.99, 90.00, 3, "active", true,
                "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=400&h=400&fit=crop",
                "Health tracking smartwatch with heart rate, SpO2, GPS and 7-day battery life.",
                "2024-01-07T14:30:00"));

        products.add(build(idGenerator.getAndIncrement(),
                "Desk Lamp", "HOME-045", "Home & Kitchen",
                34.99, 13.00, 22, "active", false,
                "https://images.unsplash.com/photo-1507473885765-e6ed057f782c?w=400&h=400&fit=crop",
                "LED desk lamp with adjustable colour temperature, brightness and USB charging port.",
                "2024-01-06T09:20:00"));
    }

    // ── Factory helper ────────────────────────────────────────────────────
    private ProductResponse build(Long id, String name, String sku, String category,
                                   double price, double cost, int stock, String status,
                                   boolean featured, String imageUrl, String description,
                                   String createdAt) {
        return ProductResponse.builder()
                .id(id)
                .name(name)
                .sku(sku)
                .categories(category)
                .price(price)
                .cost(cost)
                .stock(stock)
                .status(status)
                .inStock(stock > 0)
                .isFeatured(featured)
                .isVisible(true)
                .trackInventory(true)
                .lowStockThreshold(5)
                .description(description)
                .images(List.of(imageUrl))
                .createdAt(createdAt)
                .updatedAt(createdAt)
                .build();
    }

    // ── CRUD ──────────────────────────────────────────────────────────────
    public List<ProductResponse> getAllProducts() {
        return new ArrayList<>(products);
    }

    public Optional<ProductResponse> getProductById(Long id) {
        return products.stream().filter(p -> p.getId().equals(id)).findFirst();
    }

    public ProductResponse saveProduct(ProductResponse product) {
        if (product.getId() == null) {
            // reflection-free: rebuild with generated id using the helper isn't viable,
            // so callers must supply an id from nextId()
            throw new IllegalArgumentException("id must be set before saving");
        }
        products.removeIf(p -> p.getId().equals(product.getId()));
        products.add(product);
        return product;
    }

    public void deleteProduct(Long id) {
        products.removeIf(p -> p.getId().equals(id));
    }

    public Long nextId() {
        return idGenerator.getAndIncrement();
    }

    public long count() {
        return products.size();
    }
}
