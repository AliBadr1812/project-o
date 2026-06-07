package com.ali_b1812.app.mockdata;

import com.ali_b1812.app.dto.response.CategoryResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class CategoryMockData {

    private final List<CategoryResponse> categories = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @PostConstruct
    public void init() {
        categories.add(build(idGenerator.getAndIncrement(),
                "Electronics",
                "Electronic devices and gadgets including phones, laptops, and accessories",
                "fas fa-laptop", 42, true, "2024-01-15T10:30:00"));

        categories.add(build(idGenerator.getAndIncrement(),
                "Clothing",
                "Apparel and fashion items for all ages and genders",
                "fas fa-tshirt", 128, true, "2024-01-14T14:20:00"));

        categories.add(build(idGenerator.getAndIncrement(),
                "Home & Kitchen",
                "Home appliances, furniture, and kitchen essentials",
                "fas fa-home", 76, true, "2024-01-13T09:15:00"));

        categories.add(build(idGenerator.getAndIncrement(),
                "Fitness",
                "Exercise equipment, sportswear, and health supplements",
                "fas fa-dumbbell", 31, true, "2024-01-12T16:45:00"));

        categories.add(build(idGenerator.getAndIncrement(),
                "Books",
                "Fiction, non-fiction, educational books and magazines",
                "fas fa-book", 89, true, "2024-01-11T11:30:00"));

        categories.add(build(idGenerator.getAndIncrement(),
                "Beauty",
                "Cosmetics, skincare, and personal care products",
                "fas fa-spa", 54, true, "2024-01-10T13:20:00"));

        categories.add(build(idGenerator.getAndIncrement(),
                "Toys & Games",
                "Children toys, board games, and entertainment products",
                "fas fa-gamepad", 23, false, "2024-01-09T15:10:00"));

        categories.add(build(idGenerator.getAndIncrement(),
                "Automotive",
                "Car accessories, tools, and maintenance products",
                "fas fa-car", 18, true, "2024-01-08T10:45:00"));

        categories.add(build(idGenerator.getAndIncrement(),
                "Garden",
                "Gardening tools, plants, and outdoor equipment",
                "fas fa-seedling", 12, true, "2024-01-07T14:30:00"));

        categories.add(build(idGenerator.getAndIncrement(),
                "Office Supplies",
                "Stationery, office furniture, and business essentials",
                "fas fa-briefcase", 67, true, "2024-01-06T09:20:00"));
    }

    private CategoryResponse build(Long id, String name, String description,
                                    String icon, int productCount, boolean isActive,
                                    String createdAt) {
        return CategoryResponse.builder()
                .id(id)
                .name(name)
                .description(description)
                .icon(icon)
                .productCount(productCount)
                .isActive(isActive)
                .createdAt(LocalDateTime.parse(createdAt))
                .build();
    }

    // ── CRUD ──────────────────────────────────────────────────────────────
    public List<CategoryResponse> getAllCategories() {
        return new ArrayList<>(categories);
    }

    public Optional<CategoryResponse> getCategoryById(Long id) {
        return categories.stream().filter(c -> c.getId().equals(id)).findFirst();
    }

    public CategoryResponse saveCategory(CategoryResponse category) {
        categories.removeIf(c -> c.getId().equals(category.getId()));
        categories.add(category);
        return category;
    }

    public void deleteCategory(Long id) {
        categories.removeIf(c -> c.getId().equals(id));
    }

    public Long nextId() {
        return idGenerator.getAndIncrement();
    }
}
