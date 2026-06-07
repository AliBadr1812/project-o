package com.ali_b1812.app.controller;

import com.ali_b1812.app.dto.response.ApiResponse;
import com.ali_b1812.app.dto.response.DiscountResponse;
import com.ali_b1812.app.service.DiscountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/discounts")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Discounts", description = "Discount / coupon code management")
public class DiscountController {

    private final DiscountService discountService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<DiscountResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success("OK", discountService.getAllDiscounts()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DiscountResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("OK", discountService.getDiscountById(id)));
    }

    @GetMapping("/validate/{code}")
    public ResponseEntity<ApiResponse<DiscountResponse>> validate(@PathVariable String code) {
        return ResponseEntity.ok(ApiResponse.success("Valid discount code", discountService.validateCode(code)));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse<DiscountResponse>> create(@RequestBody DiscountResponse request) {
        DiscountResponse created = discountService.createDiscount(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Discount created", created, "/discounts/" + created.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DiscountResponse>> update(
            @PathVariable Long id,
            @RequestBody DiscountResponse request) {
        return ResponseEntity.ok(ApiResponse.success("Discount updated", discountService.updateDiscount(id, request)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        discountService.deleteDiscount(id);
        return ResponseEntity.noContent().build();
    }
}
