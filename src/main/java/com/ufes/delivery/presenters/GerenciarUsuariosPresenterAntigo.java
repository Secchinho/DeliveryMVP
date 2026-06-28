/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufes.delivery.presenters;

import com.ufes.delivery.repository.IUsuarioRepository;
import com.ufes.delivery.view.IGerenciarUsuariosView;
import java.util.Objects;

/**
 *
 * @author lucas
 */
public class GerenciarUsuariosPresenterAntigo {
    private IGerenciarUsuariosView view;
    private IUsuarioRepository usuarioRepository;

    public GerenciarUsuariosPresenterAntigo(IGerenciarUsuariosView view, IUsuarioRepository usuarioRepository) {
        this.view = Objects.requireNonNull(view,"Insira uma tela.");
        this.usuarioRepository = Objects.requireNonNull(usuarioRepository,"Insira um repository");
        this.configurarEventos();
    }

    public void iniciar(){
        this.view.getJanelaPrincipal().setVisible(true);
    }
    
    private void configurarEventos() {
        this.view.getFecharBtn().addActionListener(v -> {
            this.view.getJanelaPrincipal().dispose();
        });
    }  
}
