package com.ali_b1812.app.dto.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import com.ali_b1812.app.dto.request.CreateOrderRequest;
import com.ali_b1812.app.dto.request.UpdateOrderRequest;
import com.ali_b1812.app.dto.response.OrderResponse;
import com.ali_b1812.app.model.entity.Order;
import com.ali_b1812.app.model.entity.OrderItem;
import com.ali_b1812.app.model.entity.OrderNote;
import com.ali_b1812.app.model.entity.ShippingAddress;

@Mapper(componentModel = "spring", uses = {DateMapper.class})
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(target = "customerId", expression = "java(order.getCustomer() != null ? order.getCustomer().getId() : null)")
    @Mapping(target = "items", expression = "java(mapItems(order.getItems()))")
    @Mapping(target = "shippingAddress", expression = "java(mapShippingAddress(order.getShippingAddress()))")
    @Mapping(target = "notes", expression = "java(mapNotes(order.getNotes()))")
    OrderResponse toResponse(Order order);

    default List<OrderResponse> toResponseList(List<Order> orders) {
        if (orders == null) return List.of();
        return orders.stream().map(this::toResponse).collect(Collectors.toList());
    }

    default List<OrderResponse.OrderItemResponse> mapItems(List<OrderItem> items) {
        if (items == null) return List.of();
        return items.stream().map(item -> OrderResponse.OrderItemResponse.builder()
            .id(item.getId())
            .name(item.getName())
            .sku(item.getSku())
            .price(item.getPrice())
            .quantity(item.getQuantity())
            .imageUrl(item.getImageUrl())
            .total(item.getTotal())
            .build()
        ).collect(Collectors.toList());
    }

    default OrderResponse.ShippingAddressResponse mapShippingAddress(ShippingAddress address) {
        if (address == null) return null;
        return OrderResponse.ShippingAddressResponse.builder()
            .name(address.getName())
            .street(address.getStreet())
            .city(address.getCity())
            .state(address.getState())
            .zipCode(address.getZipCode())
            .country(address.getCountry())
            .phone(address.getPhone())
            .build();
    }

    default List<OrderResponse.OrderNoteResponse> mapNotes(List<OrderNote> notes) {
        if (notes == null) return List.of();
        return notes.stream().map(note -> OrderResponse.OrderNoteResponse.builder()
            .id(note.getId())
            .author(note.getAuthor())
            .content(note.getContent())
            .createdAt(note.getCreatedAt())
            .build()
        ).collect(Collectors.toList());
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderNumber", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "shippingAddress", ignore = true)
    @Mapping(target = "notes", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "processedAt", ignore = true)
    @Mapping(target = "shippedAt", ignore = true)
    @Mapping(target = "deliveredAt", ignore = true)
    Order toEntity(CreateOrderRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderNumber", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "shippingAddress", ignore = true)
    @Mapping(target = "notes", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "customerName", ignore = true)
    @Mapping(target = "customerEmail", ignore = true)
    @Mapping(target = "customerPhone", ignore = true)
    @Mapping(target = "subtotal", ignore = true)
    @Mapping(target = "shippingCost", ignore = true)
    @Mapping(target = "tax", ignore = true)
    @Mapping(target = "discount", ignore = true)
    @Mapping(target = "paymentMethod", ignore = true)
    void updateEntityFromRequest(UpdateOrderRequest request, @MappingTarget Order entity);
}
