package com.ali_b1812.app.dto.mapper;

import java.util.List;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import com.ali_b1812.app.dto.request.CreateProductRequest;
import com.ali_b1812.app.dto.request.UpdateProductRequest;
import com.ali_b1812.app.dto.response.ProductResponse;
import com.ali_b1812.app.model.entity.Product;

@Mapper(componentModel = "spring", uses = {DateMapper.class})
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(target = "categories", expression = "java(product.getCategory() != null ? product.getCategory().getName() : null)")
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "averageRating", ignore = true)
    @Mapping(target = "reviewCount", ignore = true)
    @Mapping(target = "salesData", ignore = true)
    @Mapping(target = "weight", ignore = true)
    @Mapping(target = "weightUnit", ignore = true)
    @Mapping(target = "dimensions", ignore = true)
    @Mapping(target = "specifications", ignore = true)
    ProductResponse toResponse(Product product);

    List<ProductResponse> toResponseList(List<Product> products);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Product toEntity(CreateProductRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(UpdateProductRequest request, @MappingTarget Product entity);

    @AfterMapping
    default void calculateStockStatus(@MappingTarget ProductResponse response, Product product) {
        if (product.getTrackInventory() != null && product.getTrackInventory()) {
            response.setInStock(product.getStock() != null && product.getStock() > 0);
        } else {
            response.setInStock(true);
        }
    }
}
