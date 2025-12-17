package com.example.products.messaging;

import com.example.products.config.RabbitMQConfig;
import com.example.products.messaging.events.ProductCreatedEvent;
import com.example.products.messaging.events.ProductDeletedEvent;
import com.example.products.messaging.events.ProductUpdatedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class ProductEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public ProductEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishProductCreated(ProductCreatedEvent event) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, "product.created", event);
    }

    public void publishProductUpdated(ProductUpdatedEvent event) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, "product.updated", event);
    }

    public void publishProductDeleted(ProductDeletedEvent event) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, "product.deleted", event);
    }
}
