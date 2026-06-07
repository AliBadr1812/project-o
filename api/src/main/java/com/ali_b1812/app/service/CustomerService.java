package com.ali_b1812.app.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ali_b1812.app.dto.request.CreateCustomerRequest;
import com.ali_b1812.app.dto.request.UpdateCustomerRequest;
import com.ali_b1812.app.dto.response.CustomerResponse;
import com.ali_b1812.app.mockdata.CustomerMockData;
import com.ali_b1812.app.service.interfaces.ICustomerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService implements ICustomerService {

    private final CustomerMockData customerMockData;

    @Override
    public List<CustomerResponse> findAllCustomers() {
        log.info("Fetching all customers from mock data");
        return customerMockData.getAllCustomers();
    }

    @Override
    public CustomerResponse getCustomerById(Long id) {
        log.info("Fetching customer with ID: {}", id);
        return customerMockData.getCustomerById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + id));
    }

    @Override
    public CustomerResponse createCustomer(CreateCustomerRequest request) {
        log.info("Creating new customer: {}", request.getFullName());
        CustomerResponse newCustomer = CustomerResponse.builder()
                .id(customerMockData.nextId())
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .avatarUrl(request.getAvatarUrl())
                .orderCount(0)
                .totalSpent(0.0)
                .status(request.getStatus() != null ? request.getStatus() : "active")
                .type(request.getType() != null ? request.getType() : "new")
                .createdAt(LocalDateTime.now())
                .build();
        return customerMockData.saveCustomer(newCustomer);
    }

    @Override
    public CustomerResponse updateCustomer(Long id, UpdateCustomerRequest request) {
        log.info("Updating customer with ID: {}", id);
        CustomerResponse existing = customerMockData.getCustomerById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + id));

        CustomerResponse updated = CustomerResponse.builder()
                .id(id)
                .fullName(request.getFullName() != null ? request.getFullName() : existing.getFullName())
                .email(request.getEmail() != null ? request.getEmail() : existing.getEmail())
                .phone(request.getPhone() != null ? request.getPhone() : existing.getPhone())
                .avatarUrl(request.getAvatarUrl() != null ? request.getAvatarUrl() : existing.getAvatarUrl())
                .orderCount(existing.getOrderCount())
                .totalSpent(existing.getTotalSpent())
                .lastOrderDate(existing.getLastOrderDate())
                .status(request.getStatus() != null ? request.getStatus() : existing.getStatus())
                .type(request.getType() != null ? request.getType() : existing.getType())
                .createdAt(existing.getCreatedAt())
                .build();
        return customerMockData.saveCustomer(updated);
    }

    @Override
    public void deleteCustomer(Long id) {
        log.info("Deleting customer with ID: {}", id);
        customerMockData.getCustomerById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + id));
        customerMockData.deleteCustomer(id);
    }
}
