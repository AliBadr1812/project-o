package com.ali_b1812.app.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ali_b1812.app.dto.response.ApiResponse;
import com.ali_b1812.app.dto.response.CustomerResponse;
import com.ali_b1812.app.service.CustomerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Customers", description = "Customer management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class CustomerController {
    
    private final CustomerService customerService;

    @Operation(summary = "Get customer by ID", description = "Retrieve a customer by its ID")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Customer found and returned successfully",
            content = @Content(mediaType = "application/json", 
                     schema = @Schema(implementation = ApiResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Invalid input data"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Customer not found with the given ID"
        )
    })
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse<CustomerResponse>> getCustomerById(@PathVariable Long id) {
        log.info("Received request to get customer with ID: {}", id);

        CustomerResponse customerResponse = customerService.getCustomerById(id);

        ApiResponse<CustomerResponse> response = ApiResponse.<CustomerResponse>builder()
            .success(true)
            .data(customerResponse)
            .message("Customer retrieved successfully")
            .build();
        
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get all customers", description = "Retrieve a list of all customers")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "All customers found and returned successfully",
            content = @Content(mediaType = "application/json", 
                     schema = @Schema(implementation = ApiResponse.class))
        )
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse<List<CustomerResponse>>> getAllCustomers() {
        log.info("Received request to get all customers");
        List<CustomerResponse> customers = customerService.findAllCustomers();
        ApiResponse<List<CustomerResponse>> response = ApiResponse.<List<CustomerResponse>>builder()
            .success(true)
            .data(customers)
            .message("All customers retrieved successfully")
            .build();

        return ResponseEntity.ok(response);
    }
}
