package com.ali_b1812.app.mockdata;

import com.ali_b1812.app.dto.response.SegmentResponse;
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
public class SegmentMockData {

    private final List<SegmentResponse> segments = new ArrayList<>();
    private final AtomicLong idGen = new AtomicLong(1);
    private static final DateTimeFormatter FMT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private String ts(int daysAgo) {
        return LocalDateTime.now().minusDays(daysAgo).format(FMT);
    }

    @PostConstruct
    public void init() {
        segments.add(SegmentResponse.builder()
                .id(idGen.getAndIncrement())
                .name("VIP Customers")
                .description("High-value customers with 5+ orders and $500+ total spend")
                .color("#7c5ef0")
                .icon("fas fa-crown")
                .criteria(Arrays.asList(
                        SegmentResponse.SegmentCriteria.builder()
                                .type("CUSTOMER_TYPE").value("vip").label("Type: VIP").build(),
                        SegmentResponse.SegmentCriteria.builder()
                                .type("ORDER_COUNT_MIN").value("5").label("Orders ≥ 5").build(),
                        SegmentResponse.SegmentCriteria.builder()
                                .type("TOTAL_SPENT_MIN").value("500").label("Spent ≥ $500").build()
                ))
                .customerCount(24)
                .totalRevenue(28450.00)
                .avgOrderValue(237.08)
                .isSystem(true)
                .createdAt(ts(90))
                .updatedAt(ts(1))
                .build());

        segments.add(SegmentResponse.builder()
                .id(idGen.getAndIncrement())
                .name("New Customers")
                .description("Customers who joined in the last 30 days")
                .color("#22c55e")
                .icon("fas fa-user-plus")
                .criteria(Arrays.asList(
                        SegmentResponse.SegmentCriteria.builder()
                                .type("LAST_ORDER_DAYS").value("30").label("First order ≤ 30 days ago").build()
                ))
                .customerCount(18)
                .totalRevenue(3240.00)
                .avgOrderValue(180.00)
                .isSystem(true)
                .createdAt(ts(90))
                .updatedAt(ts(0))
                .build());

        segments.add(SegmentResponse.builder()
                .id(idGen.getAndIncrement())
                .name("Returning Customers")
                .description("Customers who have placed 2 or more orders")
                .color("#3b82f6")
                .icon("fas fa-repeat")
                .criteria(Arrays.asList(
                        SegmentResponse.SegmentCriteria.builder()
                                .type("CUSTOMER_TYPE").value("returning").label("Type: Returning").build(),
                        SegmentResponse.SegmentCriteria.builder()
                                .type("ORDER_COUNT_MIN").value("2").label("Orders ≥ 2").build()
                ))
                .customerCount(67)
                .totalRevenue(19820.00)
                .avgOrderValue(295.82)
                .isSystem(true)
                .createdAt(ts(90))
                .updatedAt(ts(2))
                .build());

        segments.add(SegmentResponse.builder()
                .id(idGen.getAndIncrement())
                .name("High Spenders")
                .description("Customers with lifetime spend over $1,000")
                .color("#f59e0b")
                .icon("fas fa-dollar-sign")
                .criteria(Arrays.asList(
                        SegmentResponse.SegmentCriteria.builder()
                                .type("TOTAL_SPENT_MIN").value("1000").label("Total spent ≥ $1,000").build()
                ))
                .customerCount(31)
                .totalRevenue(51300.00)
                .avgOrderValue(1654.84)
                .isSystem(false)
                .createdAt(ts(30))
                .updatedAt(ts(0))
                .build());

        segments.add(SegmentResponse.builder()
                .id(idGen.getAndIncrement())
                .name("At-Risk Customers")
                .description("Active customers who haven't ordered in 60+ days")
                .color("#ef4444")
                .icon("fas fa-triangle-exclamation")
                .criteria(Arrays.asList(
                        SegmentResponse.SegmentCriteria.builder()
                                .type("STATUS").value("active").label("Status: Active").build(),
                        SegmentResponse.SegmentCriteria.builder()
                                .type("LAST_ORDER_DAYS").value("60").label("No order in 60+ days").build()
                ))
                .customerCount(12)
                .totalRevenue(4890.00)
                .avgOrderValue(407.50)
                .isSystem(false)
                .createdAt(ts(14))
                .updatedAt(ts(1))
                .build());
    }

    public List<SegmentResponse> getAll() {
        return new ArrayList<>(segments);
    }

    public Optional<SegmentResponse> getById(Long id) {
        return segments.stream().filter(s -> s.getId().equals(id)).findFirst();
    }

    public SegmentResponse save(SegmentResponse s) {
        if (s.getId() == null) {
            s.setId(idGen.getAndIncrement());
            s.setCreatedAt(LocalDateTime.now().format(FMT));
            s.setUpdatedAt(s.getCreatedAt());
            segments.add(s);
        } else {
            for (int i = 0; i < segments.size(); i++) {
                if (segments.get(i).getId().equals(s.getId())) {
                    s.setUpdatedAt(LocalDateTime.now().format(FMT));
                    segments.set(i, s);
                    return s;
                }
            }
        }
        return s;
    }

    public void delete(Long id) {
        segments.removeIf(s -> s.getId().equals(id));
    }
}
