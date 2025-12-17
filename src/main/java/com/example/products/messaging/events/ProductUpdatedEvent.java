package com.example.products.messaging.events;

import com.example.products.api.dto.IngredientDto;

import java.util.List;
import java.util.UUID;

public record ProductUpdatedEvent(UUID productId, String productName, List<IngredientDto> ingredients) {
}
