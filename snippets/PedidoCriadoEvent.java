package com.portfolio.eventos;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/** Contrato do evento publicado pelo pedidos-service e consumido pelo estoque-service. */
public record PedidoCriadoEvent(UUID pedidoId, List<Item> itens, BigDecimal total) {

    public record Item(Long produtoId, int quantidade) {}
}
