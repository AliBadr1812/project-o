package com.ali_b1812.app.dto.mapper;

import java.util.List;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import com.ali_b1812.app.dto.request.CreateOrderRequest;
import com.ali_b1812.app.dto.request.UpdateOrderRequest;
import com.ali_b1812.app.dto.response.OrderResponse;
import com.ali_b1812.app.model.entity.Order;

@Mapper(componentModel = "spring", uses = {DateMapper.class})
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    OrderResponse toResponse(Order order);

    List<OrderResponse> toResponseList(List<Order> orders);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderNumber", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "total", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "processedAt", ignore = true)
    @Mapping(target = "shippedAt", ignore = true)
    @Mapping(target = "deliveredAt", ignore = true)
    Order toEntity(CreateOrderRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(UpdateOrderRequest request, @MappingTarget Order entity);
}
