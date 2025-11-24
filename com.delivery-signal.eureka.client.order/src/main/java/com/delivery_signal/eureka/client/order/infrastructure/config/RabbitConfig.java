package com.delivery_signal.eureka.client.order.infrastructure.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 주문 서비스 RabbitMQ 설정.
 */
@Configuration
public class RabbitConfig {

    public static final String DELIVERY_EXCHANGE = "delivery.exchange";

    public static final String ROUTING_KEY_DELIVERY_CREATE = "delivery.create";
    public static final String ROUTING_KEY_DELIVERY_CANCEL = "delivery.cancel";

    public static final String QUEUE_DELIVERY_CREATED_RESPONSE = "delivery.created.response.queue";

    @Bean
    public TopicExchange deliveryExchange() {
        return new TopicExchange(DELIVERY_EXCHANGE);
    }

    @Bean
    public Queue deliveryCreatedResponseQueue() {
        // durable: true 로 설정
        return new Queue(QUEUE_DELIVERY_CREATED_RESPONSE, true);
    }

    @Bean
    public Binding deliveryCreatedResponseBinding(Queue deliveryCreatedResponseQueue,
        TopicExchange deliveryExchange) {
        // 예시: 배송 서비스에서 "delivery.created"로 응답 보내준다고 가정
        return BindingBuilder
            .bind(deliveryCreatedResponseQueue)
            .to(deliveryExchange)
            .with("delivery.created");
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
        Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }
}
