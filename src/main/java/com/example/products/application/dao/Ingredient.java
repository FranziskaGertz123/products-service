package com.example.products.application.dao;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Embeddable
public class Ingredient {
    private String name;
    private BigDecimal usage;

    @Enumerated(EnumType.STRING)
    private Unit unit;

    private BigDecimal price;
}

