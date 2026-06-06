package com.ali_b1812.app.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ali_b1812.app.dto.mapper.ProductMapper;
import com.ali_b1812.app.dto.request.CreateProductRequest;
import com.ali_b1812.app.dto.request.UpdateProductRequest;
import com.ali_b1812.app.dto.response.ProductResponse;
import com.ali_b1812.app.model.entity.Product;
import com.ali_b1812.app.repository.ProductRepository;
import com.ali_b1812.app.service.interfaces.IProductService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductService implements IProductService {
    
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final AuditLoggerService auditLogger;

    @Override
    public ProductResponse getProductById(Long id) {
        log.info("Fetching product with ID: {}", id);
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));

        auditLogger.logProductActivity(
            "GET_PRODUCT",
            product.getId(),
            product.getName(),
            null,
            "Product retrieved",
            null,
            product
        );

        return productMapper.toResponse(product);
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        log.info("Fetching all products");
        List<Product> products = productRepository.findAll();

        auditLogger.logProductActivity(
            "GET_ALL_PRODUCTS",
            null,
            null,
            null,
            "All products retrieved",
            null,
            products.size()
        );

        return productMapper.toResponseList(products);
    }

    @Override
    public ProductResponse createProduct(CreateProductRequest request, Long userId) {
        log.info("Creating new product with name: {}", request.getName());
        Product product = productMapper.toEntity(request);
        Product savedProduct = productRepository.save(product);

        auditLogger.logProductActivity(
            "CREATE_PRODUCT",
            savedProduct.getId(),
            savedProduct.getName(),
            userId,
            "Product created",
            null,
            savedProduct
        );

        return productMapper.toResponse(savedProduct);
    }

    @Override
    public ProductResponse updateProduct(Long id, UpdateProductRequest request) {
        log.info("Updating product with ID: {}", id);
        Product existingProduct = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));
        
        productMapper.updateEntityFromRequest(request, existingProduct);
        Product updatedProduct = productRepository.save(existingProduct);

        auditLogger.logProductActivity(
            "UPDATE_PRODUCT",
            updatedProduct.getId(),
            updatedProduct.getName(),
            id,
            "Product updated",
            existingProduct,
            updatedProduct
        );

        return productMapper.toResponse(updatedProduct);
    }

    @Override
    public void deleteProduct(Long id, Long userId) {
        log.info("Deleting product with ID: {}", id);
        Product existingProduct = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));
        
        productRepository.delete(existingProduct);

        auditLogger.logProductActivity(
            "DELETE_PRODUCT",
            existingProduct.getId(),
            existingProduct.getName(),
            userId,
            "Product deleted",
            existingProduct,
            null
        );
    }
}
