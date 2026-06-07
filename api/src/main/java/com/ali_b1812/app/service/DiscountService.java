package com.ali_b1812.app.service;

import com.ali_b1812.app.dto.response.DiscountResponse;
import com.ali_b1812.app.mockdata.DiscountMockData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DiscountService {

    private final DiscountMockData discountMockData;

    public List<DiscountResponse> getAllDiscounts() {
        log.info("Fetching all discounts");
        return discountMockData.getAllDiscounts();
    }

    public DiscountResponse getDiscountById(Long id) {
        log.info("Fetching discount with id: {}", id);
        return discountMockData.getById(id)
                .orElseThrow(() -> new RuntimeException("Discount not found: " + id));
    }

    public DiscountResponse validateCode(String code) {
        log.info("Validating discount code: {}", code);
        return discountMockData.findByCode(code)
                .filter(d -> Boolean.TRUE.equals(d.getIsActive()))
                .orElseThrow(() -> new RuntimeException("Invalid or inactive discount code: " + code));
    }

    public DiscountResponse createDiscount(DiscountResponse request) {
        log.info("Creating discount with code: {}", request.getCode());
        request = DiscountResponse.builder()
                .id(null)
                .code(request.getCode())
                .description(request.getDescription())
                .type(request.getType())
                .value(request.getValue())
                .minimumOrderAmount(request.getMinimumOrderAmount() != null ? request.getMinimumOrderAmount() : 0.0)
                .usageLimit(request.getUsageLimit())
                .usedCount(0)
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();
        return discountMockData.save(request);
    }

    public DiscountResponse updateDiscount(Long id, DiscountResponse request) {
        log.info("Updating discount with id: {}", id);
        DiscountResponse existing = getDiscountById(id);
        DiscountResponse updated = DiscountResponse.builder()
                .id(id)
                .code(request.getCode() != null ? request.getCode() : existing.getCode())
                .description(request.getDescription() != null ? request.getDescription() : existing.getDescription())
                .type(request.getType() != null ? request.getType() : existing.getType())
                .value(request.getValue() != null ? request.getValue() : existing.getValue())
                .minimumOrderAmount(request.getMinimumOrderAmount() != null ? request.getMinimumOrderAmount() : existing.getMinimumOrderAmount())
                .usageLimit(request.getUsageLimit() != null ? request.getUsageLimit() : existing.getUsageLimit())
                .usedCount(existing.getUsedCount())
                .isActive(request.getIsActive() != null ? request.getIsActive() : existing.getIsActive())
                .startDate(request.getStartDate() != null ? request.getStartDate() : existing.getStartDate())
                .endDate(request.getEndDate() != null ? request.getEndDate() : existing.getEndDate())
                .createdAt(existing.getCreatedAt())
                .build();
        return discountMockData.save(updated);
    }

    public void deleteDiscount(Long id) {
        log.info("Deleting discount with id: {}", id);
        if (!discountMockData.delete(id)) {
            throw new RuntimeException("Discount not found: " + id);
        }
    }
}
