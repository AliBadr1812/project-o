package com.ali_b1812.app.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ali_b1812.app.dto.request.CreateProductRequest;
import com.ali_b1812.app.dto.request.UpdateProductRequest;
import com.ali_b1812.app.dto.response.ProductResponse;
import com.ali_b1812.app.mockdata.ProductMockData;
import com.ali_b1812.app.service.interfaces.IProductService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService implements IProductService {

    private final ProductMockData productMockData;

    @Override
    public List<ProductResponse> getAllProducts() {
        log.info("Fetching all products from mock data");
        return productMockData.getAllProducts();
    }

    @Override
    public ProductResponse getProductById(Long id) {
        log.info("Fetching product with ID: {}", id);
        return productMockData.getProductById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));
    }

    @Override
    public ProductResponse createProduct(CreateProductRequest request, Long userId) {
        log.info("Creating new product: {}", request.getName());
        String now = LocalDateTime.now().toString();
        ProductResponse newProduct = ProductResponse.builder()
                .id(productMockData.nextId())
                .name(request.getName())
                .sku(request.getSku())
                .description(request.getDescription())
                .price(request.getPrice())
                .cost(request.getCost())
                .compareAtPrice(request.getCompareAtPrice())
                .stock(request.getStock())
                .status(request.getStatus() != null ? request.getStatus() : "active")
                .images(request.getImageUrl() != null ? List.of(request.getImageUrl()) : List.of())
                .inStock(request.getStock() != null && request.getStock() > 0)
                .trackInventory(true)
                .isVisible(true)
                .isFeatured(false)
                .lowStockThreshold(5)
                .createdAt(now)
                .updatedAt(now)
                .build();
        return productMockData.saveProduct(newProduct);
    }

    @Override
    public ProductResponse updateProduct(Long id, UpdateProductRequest request) {
        log.info("Updating product with ID: {}", id);
        ProductResponse existing = productMockData.getProductById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));

        ProductResponse updated = ProductResponse.builder()
                .id(id)
                .name(request.getName() != null ? request.getName() : existing.getName())
                .sku(request.getSku() != null ? request.getSku() : existing.getSku())
                .description(request.getDescription() != null ? request.getDescription() : existing.getDescription())
                .price(request.getPrice() != null ? request.getPrice() : existing.getPrice())
                .cost(request.getCost() != null ? request.getCost() : existing.getCost())
                .compareAtPrice(request.getCompareAtPrice() != null ? request.getCompareAtPrice() : existing.getCompareAtPrice())
                .stock(request.getStock() != null ? request.getStock() : existing.getStock())
                .status(request.getStatus() != null ? request.getStatus() : existing.getStatus())
                .images(existing.getImages())
                .inStock(request.getStock() != null ? request.getStock() > 0 : existing.getInStock())
                .categories(existing.getCategories())
                .trackInventory(existing.getTrackInventory())
                .isVisible(existing.getIsVisible())
                .isFeatured(existing.getIsFeatured())
                .lowStockThreshold(existing.getLowStockThreshold())
                .createdAt(existing.getCreatedAt())
                .updatedAt(LocalDateTime.now().toString())
                .build();

        return productMockData.saveProduct(updated);
    }

    @Override
    public void deleteProduct(Long id, Long userId) {
        log.info("Deleting product with ID: {}", id);
        productMockData.getProductById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));
        productMockData.deleteProduct(id);
    }
}
