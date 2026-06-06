package com.ali_b1812.app.service.interfaces;

import java.util.List;

import com.ali_b1812.app.dto.request.CreateCategoryRequest;
import com.ali_b1812.app.dto.request.UpdateCategoryRequest;
import com.ali_b1812.app.dto.response.CategoryResponse;

public interface ICategoryService {

    CategoryResponse getCategoryById(Long id);

    List<CategoryResponse> getAllCategories();

    CategoryResponse createCategory(CreateCategoryRequest request);

    CategoryResponse updateCategory(Long id, UpdateCategoryRequest request);

    void deleteCategory(Long id);
}
