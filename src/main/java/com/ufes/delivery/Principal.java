/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.ufes.delivery;

import com.ufes.delivery.presenters.LoginPresenter;
import com.ufes.delivery.repository.UsuarioRepositorySQLite;
import com.ufes.delivery.view.ILoginView;
import com.ufes.delivery.view.TelaLoginView;
import com.ufes.delivery.view.TelaLoginViewAntiga;
import com.ufes.util.AutenticacaoUsuarioService;

/**
 *
 * @author lucas
 */
public class Principal {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ILoginView view = new TelaLoginView();
        AutenticacaoUsuarioService a = new AutenticacaoUsuarioService(new UsuarioRepositorySQLite());
        LoginPresenter login = new LoginPresenter(view,a);
        
        login.iniciar();
    }
    
}
