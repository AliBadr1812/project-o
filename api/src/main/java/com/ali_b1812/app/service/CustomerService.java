package com.ali_b1812.app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ali_b1812.app.dto.mapper.CustomerMapper;
import com.ali_b1812.app.dto.response.CustomerResponse;
import com.ali_b1812.app.model.entity.Customer;
import com.ali_b1812.app.repository.CustomerRepository;
import com.ali_b1812.app.service.interfaces.ICustomerService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CustomerService implements ICustomerService{
    
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final AuditLoggerService auditLogger;

    @Override
    public CustomerResponse getCustomerById(Long id) {
        log.info("Fetching customer with ID: {}", id);
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + id));

        auditLogger.logCustomerActivity(
            "GET_CUSTOMER",
            customer.getId(),
            customer.getFullName(),
            null,
            "Customer retrieved",
            null,
            customer
        );

        return customerMapper.toResponse(customer);
    }

    @Override
    public List<CustomerResponse> findAllCustomers() {
        log.info("Fetching all customers");
        List<Customer> customers = customerRepository.findAll();

        auditLogger.logCustomerActivity(
            "GET_ALL_CUSTOMERS",
            null,
            null,
            null,
            "All customers retrieved",
            null,
            customers.size()
        );

        return customerMapper.toResponseList(customers);
    } 
}
