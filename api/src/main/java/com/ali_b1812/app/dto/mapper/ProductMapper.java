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
    
    ProductResponse toResponse(Product product);

    List<ProductResponse> toResponseList(List<Product> products);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "sku", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "imageUrl", ignore = true)
    @Mapping(target = "price", ignore = true)
    @Mapping(target = "cost", ignore = true)
    @Mapping(target = "compareAtPrice", ignore = true)
    @Mapping(target = "stock", ignore = true)
    @Mapping(target = "lowStockThreshold", ignore = true)
    @Mapping(target = "trackInventory", ignore = true)
    @Mapping(target = "inStock", ignore = true)
    @Mapping(target = "isVisible", ignore = true)
    @Mapping(target = "isFeatured", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Product toEntity(CreateProductRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(UpdateProductRequest request, @MappingTarget Product entity);

    @AfterMapping
    default void calculateStockStatus(@MappingTarget ProductResponse response, Product product) {
        if (product.getTrackInventory() != null && product.getTrackInventory()) {
            response.setInStock(product.getStock() != null && product.getStock() > 0);
        } else {
            response.setInStock(true); // If not tracking inventory, assume it's in stock
        }
    }
}
