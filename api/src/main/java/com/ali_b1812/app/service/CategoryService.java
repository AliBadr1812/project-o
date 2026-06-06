package com.ali_b1812.app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ali_b1812.app.dto.mapper.CategoryMapper;
import com.ali_b1812.app.dto.response.CategoryResponse;
import com.ali_b1812.app.model.entity.Category;
import com.ali_b1812.app.repository.CategoryRepository;
import com.ali_b1812.app.service.interfaces.ICategoryService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CategoryService implements ICategoryService{
    
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final AuditLoggerService auditLogger;

    @Override
    public CategoryResponse getCategoryById(Long id) {
        log.info("Fetching category with ID: {}", id);
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Category not found with ID: " + id));

        auditLogger.logCategoryActivity(
            "GET_CATEGORY",
            category.getId(),
            category.getName(),
            null,
            "Category retrieved",
            null,
            category
        );

        return categoryMapper.toResponse(category);
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        log.info("Fetching all categories");
        List<Category> categories = categoryRepository.findAll();

        auditLogger.logCategoryActivity(
            "GET_ALL_CATEGORIES",
            null,
            null,
            null,
            "All categories retrieved",
            null,
            categories.size()
        );

        return categoryMapper.toResponseList(categories);
    }
}
