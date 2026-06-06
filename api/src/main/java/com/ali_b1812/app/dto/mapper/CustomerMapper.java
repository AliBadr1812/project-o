package com.ali_b1812.app.dto.mapper;

import java.util.List;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import com.ali_b1812.app.dto.request.CreateCustomerRequest;
import com.ali_b1812.app.dto.request.UpdateCustomerRequest;
import com.ali_b1812.app.dto.response.CustomerResponse;
import com.ali_b1812.app.model.entity.Customer;

@Mapper(componentModel = "spring", uses = {DateMapper.class})
public interface CustomerMapper {
    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    CustomerResponse toResponse(Customer customer);

    List<CustomerResponse> toResponseList(List<Customer> customers);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fullName", ignore = true)
    @Mapping(target = "email", ignore = true)
    Customer toEntity(CreateCustomerRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(UpdateCustomerRequest request, @MappingTarget Customer entity);
}
