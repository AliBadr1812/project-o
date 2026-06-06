package com.ali_b1812.app.service.interfaces;
import java.util.List;

import com.ali_b1812.app.dto.request.CreateOrderRequest;
import com.ali_b1812.app.dto.response.OrderResponse;

public interface IOrderService {

    OrderResponse getOrderById(Long id);

    List<OrderResponse> getAllOrders();

    OrderResponse createOrder(CreateOrderRequest request, Long userId);
    
}
