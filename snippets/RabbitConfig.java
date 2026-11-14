package com.portfolio.pedidos;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Use nos dois serviços (dependência: spring-boot-starter-amqp). */
@Configuration
public class RabbitConfig {

    public static final String EXCHANGE = "pedidos.exchange";
    public static final String FILA_ESTOQUE = "estoque.pedido-criado";
    public static final String FILA_ESTOQUE_DLQ = "estoque.pedido-criado.dlq";
    public static final String ROUTING_KEY = "pedido.criado";

    @Bean TopicExchange exchange() { return new TopicExchange(EXCHANGE); }

    @Bean Queue filaEstoque() {
        return QueueBuilder.durable(FILA_ESTOQUE)
                .withArgument("x-dead-letter-exchange", "")
                .withArgument("x-dead-letter-routing-key", FILA_ESTOQUE_DLQ)  // mensagens com erro vão para a DLQ
                .build();
    }

    @Bean Queue filaEstoqueDlq() { return QueueBuilder.durable(FILA_ESTOQUE_DLQ).build(); }

    @Bean Binding binding(Queue filaEstoque, TopicExchange exchange) {
        return BindingBuilder.bind(filaEstoque).to(exchange).with(ROUTING_KEY);
    }

    @Bean Jackson2JsonMessageConverter conversorJson() { return new Jackson2JsonMessageConverter(); }
}
