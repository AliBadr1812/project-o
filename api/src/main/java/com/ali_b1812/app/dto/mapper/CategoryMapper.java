package com.ali_b1812.app.dto.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import com.ali_b1812.app.dto.response.CategoryResponse;
import com.ali_b1812.app.model.entity.Category;

@Mapper(componentModel = "spring", uses = {DateMapper.class})
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper((CategoryMapper.class));

    CategoryResponse toResponse(Category category);

    List<CategoryResponse> toResponseList(List<Category> categories);

    Category toEntity(CategoryResponse response);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromResponse(CategoryResponse response, @MappingTarget Category entity);
}
