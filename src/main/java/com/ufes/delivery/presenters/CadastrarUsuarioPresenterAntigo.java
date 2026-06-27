/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufes.delivery.presenters;

import com.ufes.delivery.view.ICadastrarUsuarioView;
import com.ufes.util.AutenticacaoUsuarioService;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

/**
 *
 * @author lucas
 */
public class CadastrarUsuarioPresenterAntigo {
    private ICadastrarUsuarioView view;
    private AutenticacaoUsuarioService autenticacaoService;

    public CadastrarUsuarioPresenterAntigo(ICadastrarUsuarioView view, AutenticacaoUsuarioService autenticacaoService) {
        
        this.view = Objects.requireNonNull(view);
        this.autenticacaoService = Objects.requireNonNull(autenticacaoService);
    }
    
    public void configurarEventos(){
        this.view.getCancelarButton().addActionListener(v -> {
            this.cancelar();
        });
    }

    private void cancelar() {
        this.view.getCampoNomeCivil().setText("");
        this.view.getCampoNomeUsuario().setText("");
        this.view.getCampoSenha().setText("");
        this.view.getJanelaPrincipal().dispose();
    }
    
    public void iniciar() {
        this.view.getJanelaPrincipal().setVisible(true);
    }
    
}
