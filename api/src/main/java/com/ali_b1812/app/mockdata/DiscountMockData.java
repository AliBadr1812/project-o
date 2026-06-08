package com.ali_b1812.app.mockdata;

import com.ali_b1812.app.dto.response.DiscountResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class DiscountMockData {

    private final List<DiscountResponse> discounts = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @PostConstruct
    public void init() {
        discounts.add(build(idGenerator.getAndIncrement(),
                "SUMMER20", "20% off all summer items", "PERCENTAGE",
                20.0, 30.0, 500, 142, true,
                "2025-06-01T00:00:00", "2025-08-31T23:59:59"));

        discounts.add(build(idGenerator.getAndIncrement(),
                "WELCOME10", "$10 off your first order", "FIXED",
                10.0, 50.0, null, 87, true,
                "2025-01-01T00:00:00", "2025-12-31T23:59:59"));

        discounts.add(build(idGenerator.getAndIncrement(),
                "FLASH15", "15% flash sale – today only", "PERCENTAGE",
                15.0, 0.0, 200, 200, false,
                "2025-05-10T00:00:00", "2025-05-10T23:59:59"));

        discounts.add(build(idGenerator.getAndIncrement(),
                "VIP25", "25% off for VIP members", "PERCENTAGE",
                25.0, 100.0, null, 34, true,
                "2025-01-01T00:00:00", "2025-12-31T23:59:59"));

        discounts.add(build(idGenerator.getAndIncrement(),
                "FREESHIP", "Free shipping on orders $75+", "FIXED",
                0.0, 75.0, 1000, 310, true,
                "2025-03-01T00:00:00", "2025-09-30T23:59:59"));

        discounts.add(build(idGenerator.getAndIncrement(),
                "BACK2SCHOOL", "Back-to-school 12% discount", "PERCENTAGE",
                12.0, 40.0, 300, 0, false,
                "2025-08-15T00:00:00", "2025-09-15T23:59:59"));
    }

    // ── Internal helpers ──────────────────────────────────────────────────

    private DiscountResponse build(long id, String code, String description, String type,
                                   double value, double minOrder, Integer limit, int used,
                                   boolean active, String startDate, String endDate) {
        return DiscountResponse.builder()
                .id(id)
                .code(code)
                .description(description)
                .type(type)
                .value(value)
                .minimumOrderAmount(minOrder)
                .usageLimit(limit)
                .usedCount(used)
                .isActive(active)
                .startDate(startDate)
                .endDate(endDate)
                .createdAt(LocalDateTime.now().minusDays(id * 5).toString())
                .build();
    }

    // ── Public CRUD ───────────────────────────────────────────────────────

    public List<DiscountResponse> getAllDiscounts() {
        return new ArrayList<>(discounts);
    }

    public Optional<DiscountResponse> getById(Long id) {
        return discounts.stream().filter(d -> d.getId().equals(id)).findFirst();
    }

    public Optional<DiscountResponse> findByCode(String code) {
        return discounts.stream()
                .filter(d -> d.getCode().equalsIgnoreCase(code))
                .findFirst();
    }

    public DiscountResponse save(DiscountResponse discount) {
        if (discount.getId() == null) {
            // create
            discount = DiscountResponse.builder()
                    .id(idGenerator.getAndIncrement())
                    .code(discount.getCode())
                    .description(discount.getDescription())
                    .type(discount.getType())
                    .value(discount.getValue())
                    .minimumOrderAmount(discount.getMinimumOrderAmount())
                    .usageLimit(discount.getUsageLimit())
                    .usedCount(0)
                    .isActive(discount.getIsActive() != null ? discount.getIsActive() : true)
                    .startDate(discount.getStartDate())
                    .endDate(discount.getEndDate())
                    .createdAt(LocalDateTime.now().toString())
                    .build();
            discounts.add(discount);
        } else {
            // update
            final DiscountResponse updated = discount;
            discounts.replaceAll(d -> d.getId().equals(updated.getId()) ? updated : d);
        }
        return discount;
    }

    public boolean delete(Long id) {
        return discounts.removeIf(d -> d.getId().equals(id));
    }

}
