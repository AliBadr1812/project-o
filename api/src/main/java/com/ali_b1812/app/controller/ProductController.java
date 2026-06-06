package com.ali_b1812.app.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.ali_b1812.app.dto.request.CreateProductRequest;
import com.ali_b1812.app.dto.request.UpdateProductRequest;
import com.ali_b1812.app.dto.response.ApiResponse;
import com.ali_b1812.app.dto.response.ProductResponse;
import com.ali_b1812.app.service.ProductService;
import com.ali_b1812.app.util.UserPrincipal;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * REST controller for managing products.
 * 
 * Provides endpoints for creating, retrieving, updating, and deleting products.
 * All endpoints are secured and require authentication.
 */
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Products", description = "Product management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class ProductController {
    
    private final ProductService productService;

    @Operation(summary = "Get product by ID", description = "Retrieve a product by its ID")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Product found and returned successfully",
            content = @Content(mediaType = "application/json", 
                     schema = @Schema(implementation = ApiResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Invalid input data"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Product not found with the given ID"
        )
    })
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(@PathVariable Long id) {
        log.info("Received request to get product with ID: {}", id);
        ProductResponse productResponse = productService.getProductById(id);
        ApiResponse<ProductResponse> response = ApiResponse.<ProductResponse>builder()
            .success(true)
            .data(productResponse)
            .build();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get all products", description = "Retrieve a list of all products")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "All products found and returned successfully",
            content = @Content(mediaType = "application/json", 
                     schema = @Schema(implementation = ApiResponse.class))
        )
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAllProducts() {
        log.info("Received request to get all products");
        List<ProductResponse> products = productService.getAllProducts();
        ApiResponse<List<ProductResponse>> response = ApiResponse.<List<ProductResponse>>builder()
            .success(true)
            .data(products)
            .build();
        return ResponseEntity.ok(response);
    }


    @Operation(summary = "Create a new product", description = "Create a new product with the provided details")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "Product created successfully",
            content = @Content(mediaType = "application/json", 
                     schema = @Schema(implementation = ApiResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Invalid input data"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "409",
            description = "Product with the same SKU already exists"
        )
    })
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @RequestBody CreateProductRequest productRequest,
            @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipal currentUser) {

        log.info("Received request to create product: {}", productRequest);
        ProductResponse createdProduct = productService.createProduct(productRequest, currentUser.getId());
        ApiResponse<ProductResponse> response = ApiResponse.<ProductResponse>builder()
            .success(true)
            .data(createdProduct)
            .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Update an existing product", description = "Update the details of an existing product by its ID")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Product updated successfully",
            content = @Content(mediaType = "application/json", 
                     schema = @Schema(implementation = ApiResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Invalid input data"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Product not found with the given ID"
        )
    })
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @PathVariable Long id,
            @RequestBody UpdateProductRequest productRequest) {
        log.info("Received request to update product with ID: {}. Update data: {}", id, productRequest);
        ProductResponse updatedProduct = productService.updateProduct(id, productRequest);
        ApiResponse<ProductResponse> response = ApiResponse.<ProductResponse>builder()
            .success(true)
            .data(updatedProduct)
            .build();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a product", description = "Delete a product by its ID")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Product deleted successfully",
            content = @Content(mediaType = "application/json", 
                     schema = @Schema(implementation = ApiResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Product not found with the given ID"
        )
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(
            @PathVariable Long id,
            @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipal currentUser) {
        
        log.info("Received request to delete product with ID: {}", id);
        productService.deleteProduct(id, currentUser.getId());
        
        ApiResponse<Void> response = ApiResponse.<Void>builder()
            .success(true)
            .message("Product deleted successfully")
            .build();
        
        return ResponseEntity.ok(response);
    }

}
