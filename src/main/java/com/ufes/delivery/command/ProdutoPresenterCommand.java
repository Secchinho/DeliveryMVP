/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufes.delivery.command;

import com.ufes.delivery.presenters.ProdutoPresenter;

/**
 *
 * @author lucas
 */
public abstract class ProdutoPresenterCommand {
    protected ProdutoPresenter produtoPresenter;

    public ProdutoPresenterCommand(ProdutoPresenter produtoPresenter) {
        this.produtoPresenter = produtoPresenter;
    }
    
    public abstract void salvar();
}
