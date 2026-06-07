package com.ali_b1812.app.mockdata;

import com.ali_b1812.app.dto.response.ReturnResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class ReturnMockData {

    private final List<ReturnResponse> returns = new ArrayList<>();
    private final AtomicLong idGen = new AtomicLong(1);
    private static final DateTimeFormatter FMT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private String ts(int daysAgo) {
        return LocalDateTime.now().minusDays(daysAgo).format(FMT);
    }

    @PostConstruct
    public void init() {
        returns.add(ReturnResponse.builder()
                .id(idGen.getAndIncrement())
                .returnNumber("RET-2024-001")
                .orderId(1L)
                .orderNumber("ORD-2024-001234")
                .customerId(1L)
                .customerName("Emma Thompson")
                .customerEmail("emma.thompson@example.com")
                .reason("DEFECTIVE")
                .reasonDetail("Headphones stopped working after 2 days")
                .status("PENDING")
                .refundAmount(199.99)
                .refundMethod("ORIGINAL_PAYMENT")
                .items(Arrays.asList(
                        ReturnResponse.ReturnItemResponse.builder()
                                .productId(1L)
                                .productName("Premium Wireless Headphones")
                                .sku("PHONES-001")
                                .quantity(1)
                                .unitPrice(199.99)
                                .build()
                ))
                .createdAt(ts(1))
                .updatedAt(ts(1))
                .build());

        returns.add(ReturnResponse.builder()
                .id(idGen.getAndIncrement())
                .returnNumber("RET-2024-002")
                .orderId(2L)
                .orderNumber("ORD-2024-001235")
                .customerId(2L)
                .customerName("James Wilson")
                .customerEmail("j.wilson@example.com")
                .reason("WRONG_ITEM")
                .reasonDetail("Received blue, ordered black")
                .status("APPROVED")
                .refundAmount(89.99)
                .refundMethod("STORE_CREDIT")
                .items(Arrays.asList(
                        ReturnResponse.ReturnItemResponse.builder()
                                .productId(3L)
                                .productName("Ergonomic Office Chair")
                                .sku("CHAIR-003")
                                .quantity(1)
                                .unitPrice(89.99)
                                .build()
                ))
                .createdAt(ts(3))
                .updatedAt(ts(2))
                .staffNotes("Customer confirmed wrong color sent. Approved for full refund.")
                .build());

        returns.add(ReturnResponse.builder()
                .id(idGen.getAndIncrement())
                .returnNumber("RET-2024-003")
                .orderId(5L)
                .orderNumber("ORD-2024-001238")
                .customerId(3L)
                .customerName("Sarah Chen")
                .customerEmail("sarah.chen@techcorp.com")
                .reason("NOT_AS_DESCRIBED")
                .reasonDetail("Product dimensions were different from listing")
                .status("REFUNDED")
                .refundAmount(329.00)
                .refundMethod("ORIGINAL_PAYMENT")
                .items(Arrays.asList(
                        ReturnResponse.ReturnItemResponse.builder()
                                .productId(5L)
                                .productName("4K Monitor")
                                .sku("MON-4K-001")
                                .quantity(1)
                                .unitPrice(329.00)
                                .build()
                ))
                .createdAt(ts(7))
                .updatedAt(ts(4))
                .staffNotes("Refund processed to original payment method.")
                .build());

        returns.add(ReturnResponse.builder()
                .id(idGen.getAndIncrement())
                .returnNumber("RET-2024-004")
                .orderId(7L)
                .orderNumber("ORD-2024-001240")
                .customerId(4L)
                .customerName("Michael Brown")
                .customerEmail("m.brown@example.com")
                .reason("CHANGED_MIND")
                .reasonDetail("No longer needed")
                .status("REJECTED")
                .refundAmount(49.99)
                .refundMethod("ORIGINAL_PAYMENT")
                .items(Arrays.asList(
                        ReturnResponse.ReturnItemResponse.builder()
                                .productId(8L)
                                .productName("USB-C Hub")
                                .sku("HUB-USB-008")
                                .quantity(1)
                                .unitPrice(49.99)
                                .build()
                ))
                .createdAt(ts(10))
                .updatedAt(ts(9))
                .staffNotes("Return window expired. Policy requires return within 7 days.")
                .build());

        returns.add(ReturnResponse.builder()
                .id(idGen.getAndIncrement())
                .returnNumber("RET-2024-005")
                .orderId(9L)
                .orderNumber("ORD-2024-001242")
                .customerId(5L)
                .customerName("Olivia Martinez")
                .customerEmail("o.martinez@startup.io")
                .reason("DEFECTIVE")
                .reasonDetail("Screen has dead pixels")
                .status("IN_TRANSIT")
                .refundAmount(649.00)
                .refundMethod("BANK_TRANSFER")
                .items(Arrays.asList(
                        ReturnResponse.ReturnItemResponse.builder()
                                .productId(2L)
                                .productName("Laptop Pro 15")
                                .sku("LAPTOP-PRO-15")
                                .quantity(1)
                                .unitPrice(649.00)
                                .build()
                ))
                .createdAt(ts(5))
                .updatedAt(ts(2))
                .staffNotes("Item in transit back to warehouse. Refund pending receipt.")
                .build());
    }

    public List<ReturnResponse> getAll() {
        return new ArrayList<>(returns);
    }

    public Optional<ReturnResponse> getById(Long id) {
        return returns.stream().filter(r -> r.getId().equals(id)).findFirst();
    }

    public ReturnResponse save(ReturnResponse r) {
        if (r.getId() == null) {
            r.setId(idGen.getAndIncrement());
            r.setCreatedAt(LocalDateTime.now().format(FMT));
            r.setUpdatedAt(r.getCreatedAt());
            returns.add(r);
        } else {
            for (int i = 0; i < returns.size(); i++) {
                if (returns.get(i).getId().equals(r.getId())) {
                    r.setUpdatedAt(LocalDateTime.now().format(FMT));
                    returns.set(i, r);
                    return r;
                }
            }
        }
        return r;
    }

    public void delete(Long id) {
        returns.removeIf(r -> r.getId().equals(id));
    }
}
