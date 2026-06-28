/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufes.delivery.presenters;

import com.ufes.delivery.model.Cliente;
import com.ufes.delivery.repository.IClienteRepository;
import com.ufes.delivery.view.IBuscarClienteView;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author lucas
 */
public class BuscarClientePresenterAntigo {
    private IBuscarClienteView view;
    private IClienteRepository clienteRepository;
    private List<Cliente> clientes;

    public BuscarClientePresenterAntigo(IBuscarClienteView view, IClienteRepository clienteRepository) {
        this.view = Objects.requireNonNull(view);
        this.clienteRepository = Objects.requireNonNull(clienteRepository);
        this.clientes = new ArrayList<>();
        this.configurarEventos();
    }

    public void iniciar(){
        this.view.getJanelaPrincipal().setVisible(true);
    }
    
    private void configurarEventos() {
        this.view.getFecharButton().addActionListener(v -> this.fechar());
    }
    
    private void fechar(){
        this.view.getJanelaPrincipal().dispose();
    }
}
