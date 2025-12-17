package com.example.products.messaging.events;

import java.time.Instant;
import java.util.UUID;

public record ProductDeletedEvent(
        UUID eventId,
        Instant occurredAt,
        UUID productId) {}
