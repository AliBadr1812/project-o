package com.ali_b1812.app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ali_b1812.app.dto.mapper.OrderMapper;
import com.ali_b1812.app.dto.request.CreateOrderRequest;
import com.ali_b1812.app.dto.response.OrderResponse;
import com.ali_b1812.app.mockdata.OrderMockData;
import com.ali_b1812.app.model.entity.Order;
import com.ali_b1812.app.service.interfaces.IOrderService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService implements IOrderService {

    private final OrderMockData orderMockData;
    private final OrderMapper orderMapper;
    private final AuditLoggerService auditLogger;

    @Override
    public OrderResponse getOrderById(Long id) {
        log.info("Fetching order with ID: {}", id);
        Order order = orderMockData.getOrderById(id)
            .orElseThrow(() -> new RuntimeException("Order not found with ID: " + id));

        auditLogger.logOrderActivity(
            "GET_ORDER",
            order.getId(),
            order.getCustomerName(),
            null,
            "Order retrieved",
            null,
            order
        );

        return orderMapper.toResponse(order);
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        log.info("Fetching all orders");
        List<Order> orders = orderMockData.getAllOrders();

        auditLogger.logOrderActivity(
            "GET_ALL_ORDERS",
            null,
            null,
            null,
            "All orders retrieved",
            null,
            orders.size()
        );

        return orderMapper.toResponseList(orders);
    }

    @Override
    public OrderResponse createOrder(CreateOrderRequest request, Long userId) {
        log.info("Creating new order for customer");
        Order order = orderMapper.toEntity(request);
        Order savedOrder = orderMockData.saveOrder(order);

        auditLogger.logOrderActivity(
            "CREATE_ORDER",
            savedOrder.getId(),
            savedOrder.getCustomerName(),
            userId,
            "Order created",
            null,
            savedOrder
        );

        return orderMapper.toResponse(savedOrder);
    }
}
