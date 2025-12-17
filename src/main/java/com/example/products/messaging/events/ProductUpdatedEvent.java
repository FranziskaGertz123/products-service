package com.example.products.messaging.events;

import com.example.products.api.dto.IngredientDto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ProductUpdatedEvent(
        UUID eventId,
        Instant occurredAt,
        UUID productId,
        String productName,
        int productVersion,
        List<IngredientDto> ingredients
) {
}
