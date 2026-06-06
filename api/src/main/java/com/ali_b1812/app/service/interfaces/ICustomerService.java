package com.ali_b1812.app.service.interfaces;

import java.util.List;

import com.ali_b1812.app.dto.request.CreateCustomerRequest;
import com.ali_b1812.app.dto.request.UpdateCustomerRequest;
import com.ali_b1812.app.dto.response.CustomerResponse;

public interface ICustomerService {

    CustomerResponse getCustomerById(Long id);

    List<CustomerResponse> findAllCustomers();

    CustomerResponse createCustomer(CreateCustomerRequest request);

    CustomerResponse updateCustomer(Long id, UpdateCustomerRequest request);

    void deleteCustomer(Long id);
}
