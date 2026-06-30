/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufes.delivery.presenters;

import com.ufes.delivery.model.Produto;
import com.ufes.delivery.repository.IProdutoRepository;
import com.ufes.delivery.view.IBuscarProdutoView;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author lucas
 */
public class BuscarProdutoPresenter {
    private IBuscarProdutoView view;
    private IProdutoRepository produtoRepository;
    private List<Produto> produtos;

    public BuscarProdutoPresenter(IBuscarProdutoView view, IProdutoRepository produtoRepository) {
        this.view = Objects.requireNonNull(view);
        this.produtoRepository = Objects.requireNonNull(produtoRepository);
        
        this.configurarEventos();
    }

    private void configurarEventos() {
        this.view.getFecharButton().addActionListener(v -> this.view.getJanelaPrincipal().dispose());
    }
    
    
}
