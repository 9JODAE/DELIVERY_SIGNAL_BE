package com.delivery_signal.eureka.client.delivery.infrastructure.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
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

    @Value("${spring.rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${spring.rabbitmq.exchange.queue.name}")
    private String queueName;

    @Value("${spring.rabbitmq.exchange.routing.key}")
    private String routingKey; // Exchange 에서 Queue로 메시지를 전달할 때 사용되는 라우팅 패턴

    // DLQ 관련 상수
    public static final String DELIVERY_DLQ_QUEUE = "delivery.dlq.queue";
    public static final String DELIVERY_DLX_EXCHANGE = "delivery.dlx.exchange";

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(exchangeName);
    }

    /**
     * 메인 큐
     * durable(true): 서버가 재시작되어도 큐가 유지되도록 설정
     * 메인 큐에 Dead Letter Exchange와 Routing Key 설정
     * @return
     */
    @Bean
    public Queue queue() {
        return QueueBuilder.durable(queueName)
            .withArgument("x-dead-letter-exchange", DELIVERY_DLX_EXCHANGE) // DLX 지정
            .withArgument("x-dead-letter-routing-key", DELIVERY_DLQ_QUEUE)  // DLQ의 라우팅 키 지정
            .build();
    }

    /**
     * Binding: Exchange가 어떤 Routing Key를 가진 메시지를 특정 Queue로 보낼지 정의
     * 지정된 Routing Key를 가진 메시지만 해당 큐로 라우팅
     */
    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue)
            .to(exchange)
            .with(routingKey);
    }

    /**
     * Dead Letter Exchange (DLE) 생성
     */
    @Bean
    public TopicExchange deadLetterExchange() {
        return new TopicExchange(DELIVERY_DLX_EXCHANGE, true, false);
    }

    /**
     * Dead Letter Queue (DLQ)
     */
    @Bean
    public Queue deliveryDLQ() {
        return new Queue(DELIVERY_DLQ_QUEUE, true);
    }

    /**
     * DLQ를 DLE에 바인딩
     */
    @Bean
    public Binding deliveryDLQBinding(Queue deliveryDLQ, TopicExchange deadLetterExchange) {
        // DLQ 바인딩 시 라우팅 키를 큐 이름과 동일하게 설정
        return BindingBuilder
            .bind(deliveryDLQ)
            .to(deadLetterExchange)
            .with("#");
    }

    /**
     * JSON 메시지 컨버터
     */
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

//    /**
//     * Listener Container Factory
//     */
//    @Bean
//    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
//        ConnectionFactory connectionFactory,
//        Jackson2JsonMessageConverter messageConverter
//    ) {
//        SimpleRabbitListenerContainerFactory factory =
//            new SimpleRabbitListenerContainerFactory();
//        factory.setConnectionFactory(connectionFactory);
//        factory.setMessageConverter(messageConverter);
//        factory.setDefaultRequeueRejected(false); // DLQ로 전송
//        return factory;
//    }
}
