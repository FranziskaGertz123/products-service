package com.example.products.application.dao;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Embeddable
public class Ingredient {
    private String name;
    private BigDecimal usage;
    private Unit unit;
    private BigDecimal price;
}

