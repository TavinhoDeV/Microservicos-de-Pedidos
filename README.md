 Microsserviços com Mensageria (RabbitMQ) 

> **Estado do zip:** aqui você recebe a **infraestrutura** (`docker-compose.yml`) e **trechos-chave** em `snippets/`
> (contrato do evento, configuração do RabbitMQ, publicador e consumidor). Os dois serviços Spring Boot você cria do zero
> (use o `pom.xml` do projeto 06 como base, trocando as dependências). Nada aqui foi compilado — é um guia, não um projeto pronto.

**Conceitos:** arquitetura orientada a eventos, mensageria, consistência eventual, idempotência, Dead Letter Queue, Saga, Docker Compose, observabilidade.
**Tempo estimado:** 40–70 h

## Arquitetura
```
Cliente ──HTTP──> pedidos-service ──(PedidoCriado)──> RabbitMQ ──> estoque-service
                      │ Postgres (pedidos)                            │ Postgres (estoque)
                      └<──────────(EstoqueReservado / ReservaFalhou)──┘
```

## Passo a passo
1. `docker compose up -d` e abra o painel do RabbitMQ em http://localhost:15672 (guest/guest).
2. Crie o módulo `pedidos-service` (Web, JPA, Validation, AMQP, PostgreSQL): `POST /api/pedidos` salva o pedido com status `PENDENTE`.
3. Crie o módulo `estoque-service` (JPA, AMQP, PostgreSQL): tabela de produtos com quantidade disponível.
4. Compartilhe o contrato do evento (`snippets/PedidoCriadoEvent.java`) — em um módulo `eventos` ou copiando o `record`.
5. `pedidos-service` publica `PedidoCriadoEvent` ao criar o pedido (`PublicadorPedidos`).
6. `estoque-service` consome (`ConsumidorEstoque`), reserva o estoque e publica o resultado.
7. `pedidos-service` consome o resultado e muda o status para `CONFIRMADO` ou `CANCELADO`.
8. **Idempotência:** a mesma mensagem pode chegar duas vezes — grave os `pedidoId` já processados.
9. **Falhas:** configure retry com backoff e DLQ; simule erro e veja a mensagem na fila `.dlq`.
10. **Consistência:** estude o padrão **Transactional Outbox** (salvar o pedido e o evento na mesma transação do banco) e implemente.
11. Observabilidade: logs com `correlationId`, Spring Actuator, e (opcional) tracing com Micrometer + Zipkin.
12. Empacote cada serviço com `Dockerfile` e rode tudo no `docker-compose.yml`.

## Desafios extras
- API Gateway (Spring Cloud Gateway) na frente dos serviços.
- Testes de integração com **Testcontainers** (RabbitMQ + PostgreSQL).
- Comparar com **Kafka** e escrever no README quando escolher um ou outro.
- Diagrama da arquitetura (Mermaid) no README.
