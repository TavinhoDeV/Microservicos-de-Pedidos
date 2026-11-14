package com.portfolio.estoque;

import com.portfolio.eventos.PedidoCriadoEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ConsumidorEstoque {

    @RabbitListener(queues = "estoque.pedido-criado")
    public void aoCriarPedido(PedidoCriadoEvent evento) {
        // 1) verifique se esta mensagem já foi processada (idempotência: guarde o pedidoId)
        // 2) baixe o estoque de cada item numa transação
        // 3) se faltar estoque, publique "estoque.reserva-falhou" (padrão Saga)
        // Exceção lançada aqui => retry => DLQ
    }
}
