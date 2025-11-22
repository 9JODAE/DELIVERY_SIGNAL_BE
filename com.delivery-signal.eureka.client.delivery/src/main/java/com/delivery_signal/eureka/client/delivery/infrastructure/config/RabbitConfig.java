package com.delivery_signal.eureka.client.delivery.infrastructure.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @EnableRabbit: RabbitMQ 관련 설정을 읽고 Bean 으로 관리할 수 있도록 해줌
 * @RabbitListener 애너테이션이 붙은 메서드에서 메시지를 수신할 수 있도록 함.
 */
@Configuration
@EnableRabbit
public class RabbitConfig {

    @Value("${spring.rabbitmq.username}")
    private String rabbitUser;

    @Value("${spring.rabbitmq.password}")
    private String rabbitPassword;

    @Value("${spring.rabbitmq.port}")
    private int rabbitPort;

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.queue.name}")
    private String queueName;

    @Value("${rabbitmq.routing.key}")
    private String routingKey; // Exchange 에서 Queue로 메시지를 전달할 때 사용되는 라우팅 패턴

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(exchangeName);
    }

    /**
     * durable(true): 서버가 재시작되어도 큐가 유지되도록 설정
     * @return
     */
    @Bean
    public Queue queue() {
        return new Queue(queueName, true);
    }

    /**
     * Binding: Exchange가 어떤 Routing Key를 가진 메시지를 특정 Queue로 보낼지 정의
     */
    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue)
            .to(exchange)
            .with(routingKey);
    }
}
