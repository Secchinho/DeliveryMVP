/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.ufes.delivery;

import com.ufes.delivery.presenters.BuscarClientePresenter;
import com.ufes.delivery.presenters.BuscarProdutoPresenter;
import com.ufes.delivery.presenters.CadastrarUsuarioPresenter;
import com.ufes.delivery.presenters.ClientePresenter;
import com.ufes.delivery.presenters.LoginPresenter;
import com.ufes.delivery.presenters.MovimentacaoEstoquePresenter;
import com.ufes.delivery.repository.ClienteRepositorySQLite;
import com.ufes.delivery.repository.IClienteRepository;
import com.ufes.delivery.repository.IProdutoRepository;
import com.ufes.delivery.repository.ProdutoRepositorySQLite;
import com.ufes.delivery.repository.UsuarioRepositorySQLite;
import com.ufes.delivery.view.BuscaClienteView;
import com.ufes.delivery.view.BuscarProdutoView;
import com.ufes.delivery.view.CadastrarUsuarioView;
import com.ufes.delivery.view.ClienteView;
import com.ufes.delivery.view.IBuscarClienteView;
import com.ufes.delivery.view.IBuscarProdutoView;
import com.ufes.delivery.view.ICadastrarUsuarioView;
import com.ufes.delivery.view.IClienteView;
import com.ufes.delivery.view.ILoginView;
import com.ufes.delivery.view.IMovimentacaoEstoqueView;
import com.ufes.delivery.view.IProdutoView;
import com.ufes.delivery.view.LoginView;
import com.ufes.delivery.view.MovimentacaoEstoqueView;
import com.ufes.delivery.view.ProdutoView;
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
        IMovimentacaoEstoqueView view = new MovimentacaoEstoqueView();
        IProdutoRepository rep = new ProdutoRepositorySQLite();
        MovimentacaoEstoquePresenter presenter = new MovimentacaoEstoquePresenter(view,rep);
        
        presenter.iniciar();
    }
    
}
