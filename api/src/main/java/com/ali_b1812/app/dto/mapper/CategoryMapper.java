package com.ali_b1812.app.dto.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import com.ali_b1812.app.dto.request.CreateCategoryRequest;
import com.ali_b1812.app.dto.request.UpdateCategoryRequest;
import com.ali_b1812.app.dto.response.CategoryResponse;
import com.ali_b1812.app.model.entity.Category;

@Mapper(componentModel = "spring", uses = {DateMapper.class})
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    CategoryResponse toResponse(Category category);

    List<CategoryResponse> toResponseList(List<Category> categories);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "productCount", ignore = true)
    Category toEntity(CreateCategoryRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "productCount", ignore = true)
    void updateEntityFromRequest(UpdateCategoryRequest request, @MappingTarget Category entity);
}
