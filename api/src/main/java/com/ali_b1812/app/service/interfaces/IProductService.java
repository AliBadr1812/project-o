package com.ali_b1812.app.service.interfaces;

import java.util.List;

import com.ali_b1812.app.dto.request.CreateProductRequest;
import com.ali_b1812.app.dto.request.UpdateProductRequest;
import com.ali_b1812.app.dto.response.ProductResponse;

public interface IProductService {

    ProductResponse getProductById(Long id);

    ProductResponse createProduct(CreateProductRequest request, Long userId);

    ProductResponse updateProduct(Long id, UpdateProductRequest request);

    List<ProductResponse> getAllProducts();

    void deleteProduct(Long id, Long userId);
    
}
