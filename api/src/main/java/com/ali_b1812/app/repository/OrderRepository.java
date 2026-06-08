package com.ali_b1812.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.ali_b1812.app.mockdata.OrderMockData;
import com.ali_b1812.app.model.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    
    public static final OrderMockData orderMockData = new OrderMockData();

    default List<Order> findAll() {
        return orderMockData.getAllOrders();
    }

    default Optional<Order> findById(Long id) {
        return orderMockData.getOrderById(id);
    }

    default Optional<Order> findByOrderNumber(String orderNumber) {
        return orderMockData.getOrderByOrderNumber(orderNumber);
    }

    default List<Order> findByStatus(String status) {
        return orderMockData.getOrdersByStatus(status);
    }

    default List<Order> findByCustomerEmail(String email) {
        return orderMockData.getOrdersByCustomerEmail(email);
    }

    @SuppressWarnings("unchecked")
    default Order save(Order order) {
        return orderMockData.saveOrder(order);
    }

    default void deleteById(Long id) {
        orderMockData.deleteOrder(id);
    }

    default long count() {
        return orderMockData.getOrderCount();
    }

    default long countByStatus(String status) {
        return orderMockData.getOrderCountByStatus(status);
    }

    default Double getTotalRevenue() {
        return orderMockData.getTotalRevenue();
    }
}
