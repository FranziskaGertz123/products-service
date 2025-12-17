package com.example.products.api.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record ProductDto(
        UUID id,
        String name,
        BigDecimal price,
        List<IngredientDto> ingredients
) {}
