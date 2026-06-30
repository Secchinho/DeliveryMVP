/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufes.delivery.presenters;

import com.ufes.delivery.model.Pedido;
import com.ufes.delivery.repository.IPedidoRepository;
import com.ufes.delivery.view.IPedidoView;
import java.util.Objects;

/**
 *
 * @author lucas
 */
public class PedidoPresenter {
    private IPedidoView view;
    private IPedidoRepository pedidoRepository;
    private Pedido pedido;

    public PedidoPresenter(IPedidoView view, IPedidoRepository pedidoRepository) {
        this.view = Objects.requireNonNull(view);
        this.pedidoRepository = Objects.requireNonNull(pedidoRepository);
        
        this.configurarEventos();
    }

    private void configurarEventos() {
        this.view.getFecharButton().addActionListener(v -> this.view.getJanelaPrincipal().dispose());
    }
    
    
}
