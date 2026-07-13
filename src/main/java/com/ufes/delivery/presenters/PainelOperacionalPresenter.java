/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufes.delivery.presenters;

import com.ufes.delivery.repository.IPedidoRepository;
import com.ufes.delivery.view.IPainelOperacionalView;
import java.util.Objects;

/**
 *
 * @author lucas
 */
public class PainelOperacionalPresenter {
    private IPainelOperacionalView view;
    private IPedidoRepository pedidoRepository;
    
    public PainelOperacionalPresenter(IPainelOperacionalView view, IPedidoRepository pedidoRepository){
        this.view = Objects.requireNonNull(view, "Insira uma tela");
        this.pedidoRepository = Objects.requireNonNull(pedidoRepository, "Insira um PedidoRepository");
        
        this.configurarEventos();
    }

    public void iniciar(){
        this.view.getJanelaPrincipal().setVisible(true);
    }
    
    private void configurarEventos() {
        
    }
}
