/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufes.delivery.repository;

import com.ufes.delivery.model.Pedido;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Implementação em memória de {@link IPedidoRepository}.
 * <p>
 * Mantém os pedidos em uma lista interna. Útil para testes do fluxo de UI
 * (US09/US10) antes de dispormos de persistência em SQLite. Quando a
 * persistência for necessária, criar {@code PedidoRepositorySQLite}
 * espelhando o padrão de {@code CupomPedidoRepositorySQLite} e demais
 * repositórios SQLite do projeto.
 *
 * @author lucas
 */
public class PedidoRepositoryEmMemoria implements IPedidoRepository {

    private final List<Pedido> pedidos = new ArrayList<>();

    @Override
    public Optional<Pedido> buscarPorId(int id) {
        return pedidos.stream()
                .filter(p -> p.getCodigoPedido() == id)
                .findFirst();
    }

    @Override
    public void adicionar(Pedido pedido) {
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido não pode ser nulo");
        }
        if (buscarPorId(pedido.getCodigoPedido()).isPresent()) {
            throw new IllegalStateException(
                    "Já existe pedido com código " + pedido.getCodigoPedido());
        }
        pedidos.add(pedido);
    }

    @Override
    public void atualizar(Pedido pedido) {
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido não pode ser nulo");
        }
        for (int i = 0; i < pedidos.size(); i++) {
            if (pedidos.get(i).getCodigoPedido() == pedido.getCodigoPedido()) {
                pedidos.set(i, pedido);
                return;
            }
        }
        throw new IllegalArgumentException(
                "Pedido inexistente: " + pedido.getCodigoPedido());
    }

    @Override
    public List<Pedido> listarPedidos() {
        return Collections.unmodifiableList(pedidos);
    }

    @Override
    public void removerPedido(int id) {
        if (!pedidos.removeIf(p -> p.getCodigoPedido() == id)) {
            throw new IllegalArgumentException("Pedido inexistente: " + id);
        }
    }
}
