/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.ufes.delivery;

import com.ufes.delivery.presenters.CadastrarUsuarioPresenter;
import com.ufes.delivery.presenters.LoginPresenter;
import com.ufes.delivery.repository.UsuarioRepositorySQLite;
import com.ufes.delivery.view.CadastrarUsuarioView;
import com.ufes.delivery.view.ICadastrarUsuarioView;
import com.ufes.delivery.view.ILoginView;
import com.ufes.delivery.view.LoginView;
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
        ILoginView view = new LoginView();
        AutenticacaoUsuarioService a = new AutenticacaoUsuarioService(new UsuarioRepositorySQLite());
        
        ICadastrarUsuarioView viewCadastroUsuario = new CadastrarUsuarioView();
        CadastrarUsuarioPresenter telaCadastro = new CadastrarUsuarioPresenter(viewCadastroUsuario, new UsuarioRepositorySQLite());
        
        LoginPresenter login = new LoginPresenter(view,a, telaCadastro);
        
        login.iniciar();
    }
    
}
