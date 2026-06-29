/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufes.delivery.presenters;

import com.ufes.delivery.command.ClientePresenterCommand;
import com.ufes.delivery.model.Cliente;
import com.ufes.delivery.repository.IClienteRepository;
import com.ufes.delivery.view.IClienteView;
import java.util.Objects;

/**
 *
 * @author lucas
 */
public class ClientePresenterAntigo {
    private IClienteView view;
    private Cliente cliente;
    private IClienteRepository clienteRepository;
    private ClientePresenterCommand command;

    public ClientePresenterAntigo(IClienteView view, IClienteRepository clienteRepository, ClientePresenterCommand command) {
        this.view = Objects.requireNonNull(view);
        this.clienteRepository = Objects.requireNonNull(clienteRepository);
        this.command = Objects.requireNonNull(command);
        this.configurarEventos();
    }

    private void configurarEventos() {
        this.view.getBotaoCancelar().addActionListener(v -> this.view.getJanelaPrincipal().dispose());
    }
    
    public void iniciar(){
        this.view.getJanelaPrincipal().setVisible(true);
    }
    
    
}
