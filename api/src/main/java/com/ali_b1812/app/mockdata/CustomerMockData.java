package com.ali_b1812.app.mockdata;

import com.ali_b1812.app.dto.response.CustomerResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class CustomerMockData {

    private final List<CustomerResponse> customers = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @PostConstruct
    public void init() {
        customers.add(CustomerResponse.builder()
                .id(idGenerator.getAndIncrement())
                .fullName("John Smith")
                .email("john.smith@example.com")
                .phone("+1 (555) 123-4567")
                .orderCount(24)
                .totalSpent(2899.99)
                .lastOrderDate(LocalDateTime.parse("2024-01-15T14:30:00"))
                .status("active")
                .type("vip")
                .createdAt(LocalDateTime.parse("2023-03-10T09:00:00"))
                .build());

        customers.add(CustomerResponse.builder()
                .id(idGenerator.getAndIncrement())
                .fullName("Emma Johnson")
                .email("emma.j@example.com")
                .phone("+1 (555) 234-5678")
                .orderCount(12)
                .totalSpent(1499.50)
                .lastOrderDate(LocalDateTime.parse("2024-01-14T11:20:00"))
                .status("active")
                .type("returning")
                .createdAt(LocalDateTime.parse("2023-05-22T11:30:00"))
                .build());

        customers.add(CustomerResponse.builder()
                .id(idGenerator.getAndIncrement())
                .fullName("Robert Brown")
                .email("robert.b@example.com")
                .phone("+1 (555) 345-6789")
                .orderCount(8)
                .totalSpent(899.99)
                .lastOrderDate(LocalDateTime.parse("2024-01-12T09:15:00"))
                .status("active")
                .type("new")
                .createdAt(LocalDateTime.parse("2023-11-05T14:45:00"))
                .build());

        customers.add(CustomerResponse.builder()
                .id(idGenerator.getAndIncrement())
                .fullName("Sarah Davis")
                .email("sarah.d@example.com")
                .phone("+1 (555) 456-7890")
                .orderCount(32)
                .totalSpent(3899.99)
                .lastOrderDate(LocalDateTime.parse("2024-01-10T16:45:00"))
                .status("inactive")
                .type("vip")
                .createdAt(LocalDateTime.parse("2022-08-17T10:00:00"))
                .build());

        customers.add(CustomerResponse.builder()
                .id(idGenerator.getAndIncrement())
                .fullName("Michael Wilson")
                .email("michael.w@example.com")
                .phone("+1 (555) 567-8901")
                .orderCount(5)
                .totalSpent(599.99)
                .lastOrderDate(LocalDateTime.parse("2024-01-08T11:30:00"))
                .status("active")
                .type("new")
                .createdAt(LocalDateTime.parse("2023-12-01T08:20:00"))
                .build());

        customers.add(CustomerResponse.builder()
                .id(idGenerator.getAndIncrement())
                .fullName("Jessica Miller")
                .email("jessica.m@example.com")
                .phone("+1 (555) 678-9012")
                .orderCount(18)
                .totalSpent(2299.50)
                .lastOrderDate(LocalDateTime.parse("2024-01-07T13:20:00"))
                .status("active")
                .type("returning")
                .createdAt(LocalDateTime.parse("2023-04-14T16:00:00"))
                .build());

        customers.add(CustomerResponse.builder()
                .id(idGenerator.getAndIncrement())
                .fullName("David Taylor")
                .email("david.t@example.com")
                .phone("+1 (555) 789-0123")
                .orderCount(3)
                .totalSpent(399.99)
                .lastOrderDate(LocalDateTime.parse("2024-01-05T15:10:00"))
                .status("active")
                .type("new")
                .createdAt(LocalDateTime.parse("2023-12-28T12:00:00"))
                .build());

        customers.add(CustomerResponse.builder()
                .id(idGenerator.getAndIncrement())
                .fullName("Lisa Anderson")
                .email("lisa.a@example.com")
                .phone("+1 (555) 890-1234")
                .orderCount(15)
                .totalSpent(1799.99)
                .lastOrderDate(LocalDateTime.parse("2024-01-04T10:45:00"))
                .status("inactive")
                .type("returning")
                .createdAt(LocalDateTime.parse("2023-02-28T09:30:00"))
                .build());

        customers.add(CustomerResponse.builder()
                .id(idGenerator.getAndIncrement())
                .fullName("James Thomas")
                .email("james.t@example.com")
                .phone("+1 (555) 901-2345")
                .orderCount(27)
                .totalSpent(3299.99)
                .lastOrderDate(LocalDateTime.parse("2024-01-03T14:30:00"))
                .status("active")
                .type("vip")
                .createdAt(LocalDateTime.parse("2022-10-09T15:00:00"))
                .build());

        customers.add(CustomerResponse.builder()
                .id(idGenerator.getAndIncrement())
                .fullName("Jennifer White")
                .email("jennifer.w@example.com")
                .phone("+1 (555) 012-3456")
                .orderCount(9)
                .totalSpent(1099.99)
                .lastOrderDate(LocalDateTime.parse("2024-01-02T09:20:00"))
                .status("active")
                .type("new")
                .createdAt(LocalDateTime.parse("2023-10-15T11:00:00"))
                .build());
    }

    // ── CRUD ──────────────────────────────────────────────────────────────
    public List<CustomerResponse> getAllCustomers() {
        return new ArrayList<>(customers);
    }

    public Optional<CustomerResponse> getCustomerById(Long id) {
        return customers.stream().filter(c -> c.getId().equals(id)).findFirst();
    }

    public CustomerResponse saveCustomer(CustomerResponse customer) {
        customers.removeIf(c -> c.getId().equals(customer.getId()));
        customers.add(customer);
        return customer;
    }

    public void deleteCustomer(Long id) {
        customers.removeIf(c -> c.getId().equals(id));
    }

    public Long nextId() {
        return idGenerator.getAndIncrement();
    }
}
