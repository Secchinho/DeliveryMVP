/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ufes.delivery.repository;

import com.ufes.delivery.model.Pedido;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author raphael
 */
public interface IPedidoRepository {
    public Optional<Pedido> buscarPorId(int id);
    public void adicionar(Pedido pedido);
    public void atualizar(Pedido pedido);
    public List<Pedido> listarPedidos();
    public void removerPedido(int id);
}
