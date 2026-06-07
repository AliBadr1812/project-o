package com.ali_b1812.app.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ali_b1812.app.dto.request.CreateCategoryRequest;
import com.ali_b1812.app.dto.request.UpdateCategoryRequest;
import com.ali_b1812.app.dto.response.CategoryResponse;
import com.ali_b1812.app.mockdata.CategoryMockData;
import com.ali_b1812.app.service.interfaces.ICategoryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService implements ICategoryService {

    private final CategoryMockData categoryMockData;

    @Override
    public List<CategoryResponse> getAllCategories() {
        log.info("Fetching all categories from mock data");
        return categoryMockData.getAllCategories();
    }

    @Override
    public CategoryResponse getCategoryById(Long id) {
        log.info("Fetching category with ID: {}", id);
        return categoryMockData.getCategoryById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + id));
    }

    @Override
    public CategoryResponse createCategory(CreateCategoryRequest request) {
        log.info("Creating new category: {}", request.getName());
        CategoryResponse newCategory = CategoryResponse.builder()
                .id(categoryMockData.nextId())
                .name(request.getName())
                .description(request.getDescription())
                .icon(request.getIcon())
                .productCount(0)
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .createdAt(LocalDateTime.now())
                .build();
        return categoryMockData.saveCategory(newCategory);
    }

    @Override
    public CategoryResponse updateCategory(Long id, UpdateCategoryRequest request) {
        log.info("Updating category with ID: {}", id);
        CategoryResponse existing = categoryMockData.getCategoryById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + id));

        CategoryResponse updated = CategoryResponse.builder()
                .id(id)
                .name(request.getName() != null ? request.getName() : existing.getName())
                .description(request.getDescription() != null ? request.getDescription() : existing.getDescription())
                .icon(request.getIcon() != null ? request.getIcon() : existing.getIcon())
                .productCount(existing.getProductCount())
                .isActive(request.getIsActive() != null ? request.getIsActive() : existing.getIsActive())
                .createdAt(existing.getCreatedAt())
                .build();
        return categoryMockData.saveCategory(updated);
    }

    @Override
    public void deleteCategory(Long id) {
        log.info("Deleting category with ID: {}", id);
        categoryMockData.getCategoryById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + id));
        categoryMockData.deleteCategory(id);
    }
}
