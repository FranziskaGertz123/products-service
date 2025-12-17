package com.example.products.application.dao;

import jakarta.persistence.ElementCollection;
import jakarta.persistence. Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence. Id;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util. UUID;
import lombok.Data;

@Data
@Entity
public class Product {
    @Id
    @GeneratedValue private UUID id;
    private String name;
    private BigDecimal price;

    @ElementCollection
    private List<Ingredient> ingredients = new ArrayList<>();
}
