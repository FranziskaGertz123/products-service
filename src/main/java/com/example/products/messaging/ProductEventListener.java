package com.example.products.messaging;

import com.example.products.config.RabbitMQConfig;
import com.example.products.messaging.events.ProductCreatedEvent;
import com.example.products.messaging.events.ProductDeletedEvent;
import com.example.products.messaging.events.ProductUpdatedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ProductEventListener {

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void onCreated(ProductCreatedEvent event) {
        System.out.println("🐇 CREATED: " + event);
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void onUpdated(ProductUpdatedEvent event) {
        System.out.println("UPDATED: " + event);
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void onDeleted(ProductDeletedEvent event) {
        System.out.println("DELETED: " + event);
    }
}
