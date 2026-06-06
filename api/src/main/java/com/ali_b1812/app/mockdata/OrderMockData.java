package com.ali_b1812.app.mockdata;

import com.ali_b1812.app.model.entity.Order;
import com.ali_b1812.app.model.entity.OrderItem;
import com.ali_b1812.app.model.entity.OrderNote;
import com.ali_b1812.app.model.entity.ShippingAddress;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class OrderMockData {
    private List<Order> orders = new ArrayList<>();
    private AtomicLong orderIdGenerator = new AtomicLong(1);
    private AtomicLong itemIdGenerator = new AtomicLong(1);
    private AtomicLong noteIdGenerator = new AtomicLong(1);

    @PostConstruct
    public void init() {
        // Order 1 - Processing (matches OrderDetail.vue example)
        orders.add(createOrder1());

        // Order 2 - Shipped
        orders.add(createOrder2());

        // Order 3 - Delivered
        orders.add(createOrder3());

        // Order 4 - Cancelled
        orders.add(createOrder4());

        // Order 5 - Pending
        orders.add(createOrder5());

        // Orders 6-12 - Match OrderList.vue data
        orders.addAll(createOrderListOrders());
    }

    private Order createOrder1() {
        List<OrderItem> items = Arrays.asList(
            OrderItem.builder()
                .id(itemIdGenerator.getAndIncrement())
                .name("Premium Wireless Headphones")
                .sku("PHONES-001")
                .price(149.99)
                .quantity(1)
                .imageUrl("https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=400&h=400&fit=crop")
                .total(149.99)
                .build(),
            OrderItem.builder()
                .id(itemIdGenerator.getAndIncrement())
                .name("Phone Case - Black")
                .sku("CASE-045")
                .price(24.99)
                .quantity(2)
                .imageUrl("https://images.unsplash.com/photo-1546868871-7041f2a55e12?w=400&h=400&fit=crop")
                .total(49.98)
                .build(),
            OrderItem.builder()
                .id(itemIdGenerator.getAndIncrement())
                .name("Screen Protector (3-Pack)")
                .sku("SCRN-008")
                .price(19.99)
                .quantity(1)
                .imageUrl("https://images.unsplash.com/photo-1589578228447-e1a4e481c6c8?w=400&h=400&fit=crop")
                .total(19.99)
                .build()
        );

        ShippingAddress shippingAddress = ShippingAddress.builder()
            .name("Alex Johnson")
            .street("123 Main Street")
            .city("San Francisco")
            .state("CA")
            .zipCode("94107")
            .country("United States")
            .phone("+1 (555) 123-4567")
            .build();

        List<OrderNote> notes = Arrays.asList(
            OrderNote.builder()
                .id(noteIdGenerator.getAndIncrement())
                .author("Support Team")
                .content("Customer requested expedited shipping if possible.")
                .createdAt(LocalDateTime.parse("2024-01-15T11:00:00"))
                .build(),
            OrderNote.builder()
                .id(noteIdGenerator.getAndIncrement())
                .author("Alex Johnson")
                .content("Please leave package at the front door if I'm not home.")
                .createdAt(LocalDateTime.parse("2024-01-15T10:45:00"))
                .build()
        );

        Order order = Order.builder()
            .id(orderIdGenerator.getAndIncrement())
            .orderNumber("ORD-2024-001234")
            .status("processing")
            .createdAt(LocalDateTime.parse("2024-01-15T10:30:00"))
            .processedAt(LocalDateTime.parse("2024-01-15T11:45:00"))
            .shippedAt(null)
            .deliveredAt(null)
            .customerName("Alex Johnson")
            .customerEmail("alex.johnson@example.com")
            .customerPhone("+1 (555) 123-4567")
            .items(items)
            .shippingAddress(shippingAddress)
            .subtotal(219.96)
            .shippingCost(9.99)
            .tax(18.30)
            .total(248.25)
            .discount(0.0)
            .paymentMethod("Visa •••• 4242")
            .notes(notes)
            .build();

        // Set the order reference on items and notes
        items.forEach(item -> item.setOrder(order));
        notes.forEach(note -> note.setOrder(order));

        return order;
    }

    private Order createOrder2() {
        List<OrderItem> items = Arrays.asList(
            OrderItem.builder()
                .id(itemIdGenerator.getAndIncrement())
                .name("MacBook Pro 14\"")
                .sku("MBP-14-2024")
                .price(1999.99)
                .quantity(1)
                .imageUrl("https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=400&h=400&fit=crop")
                .total(1999.99)
                .build(),
            OrderItem.builder()
                .id(itemIdGenerator.getAndIncrement())
                .name("USB-C Hub")
                .sku("USBH-002")
                .price(79.99)
                .quantity(1)
                .imageUrl("https://images.unsplash.com/photo-1625723044792-44de16ccb4e9?w=400&h=400&fit=crop")
                .total(79.99)
                .build()
        );

        ShippingAddress shippingAddress = ShippingAddress.builder()
            .name("Sarah Williams")
            .street("456 Oak Avenue")
            .city("Los Angeles")
            .state("CA")
            .zipCode("90001")
            .country("United States")
            .phone("+1 (555) 234-5678")
            .build();

        Order order = Order.builder()
            .id(orderIdGenerator.getAndIncrement())
            .orderNumber("ORD-2024-001235")
            .status("shipped")
            .createdAt(LocalDateTime.parse("2024-01-14T08:30:00"))
            .processedAt(LocalDateTime.parse("2024-01-14T10:45:00"))
            .shippedAt(LocalDateTime.parse("2024-01-15T14:20:00"))
            .deliveredAt(null)
            .customerName("Sarah Williams")
            .customerEmail("sarah.w@example.com")
            .customerPhone("+1 (555) 234-5678")
            .items(items)
            .shippingAddress(shippingAddress)
            .subtotal(2079.98)
            .shippingCost(0.0)
            .tax(173.60)
            .total(2253.58)
            .discount(0.0)
            .paymentMethod("Mastercard •••• 9876")
            .notes(new ArrayList<>())
            .build();

        items.forEach(item -> item.setOrder(order));
        return order;
    }

    private Order createOrder3() {
        List<OrderItem> items = Arrays.asList(
            OrderItem.builder()
                .id(itemIdGenerator.getAndIncrement())
                .name("Smart Watch Series 7")
                .sku("WATCH-007")
                .price(399.99)
                .quantity(1)
                .imageUrl("https://images.unsplash.com/photo-1546868871-7041f2a55e12?w=400&h=400&fit=crop")
                .total(399.99)
                .build(),
            OrderItem.builder()
                .id(itemIdGenerator.getAndIncrement())
                .name("Watch Band - Sport")
                .sku("BAND-003")
                .price(49.99)
                .quantity(2)
                .imageUrl("https://images.unsplash.com/photo-1579586337278-3befd40fd17a?w=400&h=400&fit=crop")
                .total(99.98)
                .build()
        );

        ShippingAddress shippingAddress = ShippingAddress.builder()
            .name("Michael Chen")
            .street("789 Pine Street")
            .city("Seattle")
            .state("WA")
            .zipCode("98101")
            .country("United States")
            .phone("+1 (555) 345-6789")
            .build();

        Order order = Order.builder()
            .id(orderIdGenerator.getAndIncrement())
            .orderNumber("ORD-2024-001236")
            .status("delivered")
            .createdAt(LocalDateTime.parse("2024-01-10T14:20:00"))
            .processedAt(LocalDateTime.parse("2024-01-10T15:30:00"))
            .shippedAt(LocalDateTime.parse("2024-01-11T09:45:00"))
            .deliveredAt(LocalDateTime.parse("2024-01-13T11:20:00"))
            .customerName("Michael Chen")
            .customerEmail("michael.c@example.com")
            .customerPhone("+1 (555) 345-6789")
            .items(items)
            .shippingAddress(shippingAddress)
            .subtotal(499.97)
            .shippingCost(9.99)
            .tax(42.50)
            .total(552.46)
            .discount(0.0)
            .paymentMethod("Amex •••• 3456")
            .notes(new ArrayList<>())
            .build();

        items.forEach(item -> item.setOrder(order));
        return order;
    }

    private Order createOrder4() {
        List<OrderItem> items = Arrays.asList(
            OrderItem.builder()
                .id(itemIdGenerator.getAndIncrement())
                .name("Gaming Keyboard RGB")
                .sku("KB-789")
                .price(129.99)
                .quantity(1)
                .imageUrl("https://images.unsplash.com/photo-1511467687858-23d96c32e4ae?w=400&h=400&fit=crop")
                .total(129.99)
                .build(),
            OrderItem.builder()
                .id(itemIdGenerator.getAndIncrement())
                .name("Gaming Mouse")
                .sku("MOUSE-456")
                .price(79.99)
                .quantity(1)
                .imageUrl("https://images.unsplash.com/photo-1527814050087-3793815479db?w=400&h=400&fit=crop")
                .total(79.99)
                .build()
        );

        ShippingAddress shippingAddress = ShippingAddress.builder()
            .name("Emily Rodriguez")
            .street("321 Cedar Road")
            .city("Austin")
            .state("TX")
            .zipCode("78701")
            .country("United States")
            .phone("+1 (555) 456-7890")
            .build();

        List<OrderNote> notes = Arrays.asList(
            OrderNote.builder()
                .id(noteIdGenerator.getAndIncrement())
                .author("Emily Rodriguez")
                .content("Changed my mind, would like to cancel this order.")
                .createdAt(LocalDateTime.parse("2024-01-12T16:30:00"))
                .build(),
            OrderNote.builder()
                .id(noteIdGenerator.getAndIncrement())
                .author("Support Team")
                .content("Order cancelled as requested. Refund processed.")
                .createdAt(LocalDateTime.parse("2024-01-12T17:15:00"))
                .build()
        );

        Order order = Order.builder()
            .id(orderIdGenerator.getAndIncrement())
            .orderNumber("ORD-2024-001237")
            .status("cancelled")
            .createdAt(LocalDateTime.parse("2024-01-12T15:45:00"))
            .processedAt(null)
            .shippedAt(null)
            .deliveredAt(null)
            .customerName("Emily Rodriguez")
            .customerEmail("emily.r@example.com")
            .customerPhone("+1 (555) 456-7890")
            .items(items)
            .shippingAddress(shippingAddress)
            .subtotal(209.98)
            .shippingCost(9.99)
            .tax(18.90)
            .total(238.87)
            .discount(0.0)
            .paymentMethod("PayPal")
            .notes(notes)
            .build();

        items.forEach(item -> item.setOrder(order));
        notes.forEach(note -> note.setOrder(order));
        return order;
    }

    private Order createOrder5() {
        List<OrderItem> items = Arrays.asList(
            OrderItem.builder()
                .id(itemIdGenerator.getAndIncrement())
                .name("Bluetooth Speaker")
                .sku("SPKR-234")
                .price(89.99)
                .quantity(1)
                .imageUrl("https://images.unsplash.com/photo-1608043152269-423dbba4e7e1?w=400&h=400&fit=crop")
                .total(89.99)
                .build()
        );

        ShippingAddress shippingAddress = ShippingAddress.builder()
            .name("David Kim")
            .street("567 Maple Drive")
            .city("Chicago")
            .state("IL")
            .zipCode("60601")
            .country("United States")
            .phone("+1 (555) 567-8901")
            .build();

        Order order = Order.builder()
            .id(orderIdGenerator.getAndIncrement())
            .orderNumber("ORD-2024-001238")
            .status("pending")
            .createdAt(LocalDateTime.parse("2024-01-16T09:10:00"))
            .processedAt(null)
            .shippedAt(null)
            .deliveredAt(null)
            .customerName("David Kim")
            .customerEmail("david.k@example.com")
            .customerPhone("+1 (555) 567-8901")
            .items(items)
            .shippingAddress(shippingAddress)
            .subtotal(89.99)
            .shippingCost(5.99)
            .tax(7.65)
            .total(103.63)
            .discount(0.0)
            .paymentMethod("Visa •••• 1122")
            .notes(new ArrayList<>())
            .build();

        items.forEach(item -> item.setOrder(order));
        return order;
    }

    private List<Order> createOrderListOrders() {
        List<Order> orderList = new ArrayList<>();
        
        // Order 6 (matches OrderList.vue id 1)
        Order order6 = Order.builder()
            .id(orderIdGenerator.getAndIncrement())
            .orderNumber("001245")
            .status("pending")
            .createdAt(LocalDateTime.parse("2024-01-15T10:30:00"))
            .customerName("John Smith")
            .customerEmail("john.smith@example.com")
            .customerPhone("+1 (555) 123-4567")
            .items(Arrays.asList(
                OrderItem.builder()
                    .id(itemIdGenerator.getAndIncrement())
                    .name("Product")
                    .price(149.99)
                    .quantity(2)
                    .total(149.99)
                    .build()
            ))
            .subtotal(149.99)
            .shippingCost(0.0)
            .tax(0.0)
            .total(149.99)
            .discount(15.0)
            .paymentMethod("credit_card")
            .notes(new ArrayList<>())
            .build();
        order6.getItems().forEach(item -> item.setOrder(order6));
        orderList.add(order6);

        // Order 7 (matches OrderList.vue id 2)
        Order order7 = Order.builder()
            .id(orderIdGenerator.getAndIncrement())
            .orderNumber("001244")
            .status("processing")
            .createdAt(LocalDateTime.parse("2024-01-14T14:20:00"))
            .customerName("Emma Johnson")
            .customerEmail("emma.j@example.com")
            .customerPhone("+1 (555) 234-5678")
            .items(Arrays.asList(
                OrderItem.builder()
                    .id(itemIdGenerator.getAndIncrement())
                    .name("Product")
                    .price(289.98)
                    .quantity(4)
                    .total(289.98)
                    .build()
            ))
            .subtotal(289.98)
            .shippingCost(0.0)
            .tax(0.0)
            .total(289.98)
            .discount(25.0)
            .paymentMethod("paypal")
            .notes(new ArrayList<>())
            .build();
        order7.getItems().forEach(item -> item.setOrder(order7));
        orderList.add(order7);

        // Add more orders to match OrderList.vue...
        // Continue for all 12 orders from OrderList.vue
        
        return orderList;
    }

    // CRUD Methods
    public List<Order> getAllOrders() {
        return new ArrayList<>(orders);
    }

    public Optional<Order> getOrderById(Long id) {
        return orders.stream()
            .filter(order -> order.getId().equals(id))
            .findFirst();
    }

    public Optional<Order> getOrderByOrderNumber(String orderNumber) {
        return orders.stream()
            .filter(order -> order.getOrderNumber().equals(orderNumber))
            .findFirst();
    }

    public List<Order> getOrdersByStatus(String status) {
        return orders.stream()
            .filter(order -> order.getStatus().equals(status))
            .toList();
    }

    public List<Order> getOrdersByCustomerEmail(String email) {
        return orders.stream()
            .filter(order -> order.getCustomerEmail().equals(email))
            .toList();
    }

    public Order saveOrder(Order order) {
        if (order.getId() == null) {
            order.setId(orderIdGenerator.getAndIncrement());
            orders.add(order);
        } else {
            deleteOrder(order.getId());
            orders.add(order);
        }
        return order;
    }

    public void deleteOrder(Long id) {
        orders.removeIf(order -> order.getId().equals(id));
    }

    public long getOrderCount() {
        return orders.size();
    }

    public long getOrderCountByStatus(String status) {
        return orders.stream()
            .filter(order -> order.getStatus().equals(status))
            .count();
    }

    public Double getTotalRevenue() {
        return orders.stream()
            .filter(order -> !"cancelled".equals(order.getStatus()))
            .mapToDouble(Order::getTotal)
            .sum();
    }
}