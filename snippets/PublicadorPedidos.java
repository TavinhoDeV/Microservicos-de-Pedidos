package com.portfolio.pedidos;

import com.portfolio.eventos.PedidoCriadoEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class PublicadorPedidos {

    private final RabbitTemplate rabbit;

    public PublicadorPedidos(RabbitTemplate rabbit) {
        this.rabbit = rabbit;
    }

    public void pedidoCriado(PedidoCriadoEvent evento) {
        rabbit.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.ROUTING_KEY, evento);
    }
}
