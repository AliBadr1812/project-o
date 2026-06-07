package com.ali_b1812.app.mockdata;

import com.ali_b1812.app.dto.response.AuditLogResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class AuditLogMockData {

    private final List<AuditLogResponse> logs = new ArrayList<>();
    private final AtomicLong idGen = new AtomicLong(1);
    private static final DateTimeFormatter FMT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private String ts(int hoursAgo) {
        return LocalDateTime.now().minusHours(hoursAgo).format(FMT);
    }

    @PostConstruct
    public void init() {
        log("LOGIN",  "SYSTEM",   null, "ShopAdmin",     "admin@shop.com",  "User logged in",                              "192.168.1.1",  0);
        log("VIEW",   "PRODUCT",  1L,  "Wireless Headphones","admin@shop.com","Viewed product detail",                     "192.168.1.1",  1);
        log("UPDATE", "PRODUCT",  1L,  "Wireless Headphones","admin@shop.com","Updated price from $179.99 to $199.99",     "192.168.1.1",  1);
        log("CREATE", "DISCOUNT", 7L,  "SUMMER20",       "admin@shop.com",  "Created 20% discount code",                  "192.168.1.1",  2);
        log("VIEW",   "ORDER",    3L,  "ORD-2024-001236","admin@shop.com",  "Viewed order detail",                        "192.168.1.1",  3);
        log("UPDATE", "ORDER",    3L,  "ORD-2024-001236","admin@shop.com",  "Status changed: pending → processing",       "192.168.1.1",  3);
        log("DELETE", "PRODUCT",  12L, "Old Keyboard",   "admin@shop.com",  "Product removed from catalog",               "192.168.1.2",  5);
        log("EXPORT", "ORDER",    null,"Orders",         "admin@shop.com",  "Exported 47 orders to CSV",                  "192.168.1.1",  6);
        log("APPROVE","RETURN",   2L,  "RET-2024-002",   "admin@shop.com",  "Return approved for $89.99 store credit",    "192.168.1.1",  8);
        log("CREATE", "CUSTOMER", 28L, "Jane Doe",       "admin@shop.com",  "New customer account created",               "10.0.0.5",    10);
        log("UPDATE", "CUSTOMER", 5L,  "Michael Brown",  "admin@shop.com",  "Customer type changed: standard → vip",      "192.168.1.1", 12);
        log("REJECT", "RETURN",   4L,  "RET-2024-004",   "admin@shop.com",  "Return rejected: policy window expired",     "192.168.1.1", 14);
        log("UPDATE", "ORDER",    7L,  "ORD-2024-001240","admin@shop.com",  "Status changed: shipped → delivered",        "192.168.1.1", 16);
        log("DELETE", "DISCOUNT", 3L,  "FLASH15",        "admin@shop.com",  "Expired discount code deleted",              "192.168.1.1", 20);
        log("EXPORT", "CUSTOMER", null,"Customers",      "admin@shop.com",  "Exported 89 customers to CSV",               "192.168.1.1", 24);
        log("CREATE", "CATEGORY", 9L,  "Smart Home",     "admin@shop.com",  "New category created",                       "192.168.1.1", 28);
        log("LOGIN",  "SYSTEM",   null,"ShopAdmin",      "manager@shop.com","User logged in",                             "10.0.0.8",    30);
        log("VIEW",   "CUSTOMER", 3L,  "Sarah Chen",     "manager@shop.com","Viewed customer profile",                    "10.0.0.8",    31);
        log("UPDATE", "PRODUCT",  5L,  "4K Monitor",     "manager@shop.com","Updated stock from 2 to 15",                 "10.0.0.8",    32);
        log("LOGOUT", "SYSTEM",   null,"ShopAdmin",      "manager@shop.com","User logged out",                            "10.0.0.8",    33);
    }

    private void log(String action, String entityType, Long entityId, String entityName,
                     String performedBy, String details, String ip, int hoursAgo) {
        logs.add(AuditLogResponse.builder()
                .id(idGen.getAndIncrement())
                .action(action)
                .entityType(entityType)
                .entityId(entityId)
                .entityName(entityName)
                .performedBy(performedBy)
                .details(details)
                .ipAddress(ip)
                .timestamp(ts(hoursAgo))
                .build());
    }

    public List<AuditLogResponse> getAll() {
        // Return newest first
        List<AuditLogResponse> result = new ArrayList<>(logs);
        result.sort((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()));
        return result;
    }

    public void append(AuditLogResponse entry) {
        entry.setId(idGen.getAndIncrement());
        logs.add(entry);
    }
}
