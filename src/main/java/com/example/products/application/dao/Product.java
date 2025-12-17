package com.example.products.application.dao;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util. UUID;
import lombok.Data;

@Data
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;
    private BigDecimal price;
    private int recipeVersion = 1;

    @ElementCollection
    @CollectionTable(
            name = "product_ingredients",
            joinColumns = @JoinColumn(name = "product_id")
    )
    private List<Ingredient> ingredients = new ArrayList<>();
}
