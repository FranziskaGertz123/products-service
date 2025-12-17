package com.example.products.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "product.events";
    public static final String QUEUE = "productmanagement.product-events.q";

    @Bean
    public TopicExchange productExchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }

    @Bean
    public Queue productEventsQueue() {
        return QueueBuilder.durable(QUEUE).build();
    }

    @Bean
    public Binding productEventsBinding(Queue productEventsQueue, TopicExchange productExchange) {
        // this queue receives all routing keys like product.created, product.updated, ...
        return BindingBuilder.bind(productEventsQueue).to(productExchange).with("product.*");
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}

