package com.ali_b1812.app.model.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShippingAddress {
    private String name;
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    private String phone;
}