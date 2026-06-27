/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufes.delivery.presenters;

import com.ufes.delivery.view.ILoginView;
import com.ufes.util.AutenticacaoUsuarioService;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/**
 *
 * @author lucas
 */
public class LoginPresenterAntiga {
    private ILoginView view;
    private AutenticacaoUsuarioService autenticacaoService;
    
    public LoginPresenterAntiga(ILoginView view,AutenticacaoUsuarioService service){
        this.view = view;
        this.autenticacaoService = service;
        this.configurarEvento();
    }

    private void configurarEvento() {
        this.view.getAcessarButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                realizarLogin();
            }
        });
        
        this.view.getCancelarButton().addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            }
            
        });
        
        this.view.getCadastrarUsuarioButton().addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            }
        });
    }
    
    public void iniciar(){
        this.view.getJanelaPrincipal().setVisible(true);
    }
    
    public void realizarLogin(){
        try{
            
        }catch(IllegalArgumentException e){
            JOptionPane.showMessageDialog(this.view.getJanelaPrincipal(), e, "Erro de autenticação", JOptionPane.ERROR_MESSAGE);
            System.out.println("Erro ao realizar login: " + e.getMessage());
        }
        
    }
}
