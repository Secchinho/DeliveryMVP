/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufes.delivery.presenters;

import com.ufes.delivery.command.AtualizarProdutoCommand;
import com.ufes.delivery.command.ProdutoPresenterCommand;
import com.ufes.delivery.command.SalvarProdutoCommand;
import com.ufes.delivery.model.Produto;
import com.ufes.delivery.repository.IProdutoRepository;
import com.ufes.delivery.view.IProdutoView;
import java.util.Objects;

/**
 * Presenter responsável pela tela de cadastro/edição de produto.
 *
 * Segue o padrão Command: o Presenter apenas escolhe e aciona o Command
 * adequado (SalvarProdutoCommand para inclusão, AtualizarProdutoCommand
 * para edição). Toda a regra de validação e persistência do produto fica
 * isolada dentro das classes de Command, mantendo o Presenter responsável
 * somente por orquestrar a View e disponibilizar acesso a ela e ao
 * repositório.
 *
 * @author lucas
 */
public class ProdutoPresenter {

    private final IProdutoView view;
    private final IProdutoRepository produtoRepository;
    private Produto produto;
    private ProdutoPresenterCommand command;

    /**
     * Construtor utilizado no modo de inclusão (cadastro de novo produto).
     */
    public ProdutoPresenter(IProdutoView view, IProdutoRepository produtoRepository) {
        this.view = Objects.requireNonNull(view);
        this.produtoRepository = Objects.requireNonNull(produtoRepository);

        this.configurarEventos();
    }

    /**
     * Construtor utilizado no modo de edição/visualização de um produto
     * já existente.
     */
    public ProdutoPresenter(IProdutoView view, IProdutoRepository produtoRepository, Produto produto) {
        this.view = Objects.requireNonNull(view);
        this.produtoRepository = Objects.requireNonNull(produtoRepository);
        this.produto = Objects.requireNonNull(produto);

        this.configurarEventos();
        this.preencherCampos();
    }

    private void configurarEventos() {
        this.view.getFecharButton().addActionListener(v -> this.view.getJanelaPrincipal().dispose());
        this.view.getSalvarButton().addActionListener(v -> this.command.salvar());
    }

    public void setCommand(ProdutoPresenterCommand command){
        this.command = Objects.requireNonNull(command,"Insira um Command válido.");
    }
    
    /**
     * Preenche os campos da View com os dados do produto, quando o
     * Presenter for iniciado em modo de edição/visualização.
     */
    private void preencherCampos() {
        this.view.getTxtCodigo().setText(String.valueOf(produto.getCodigo()));
        this.view.getTxtNome().setText(produto.getNome());
        this.view.getComboCategoria().setSelectedItem(produto.getCategoria());
        this.view.getTxtPrecoUnitario().setText(String.valueOf(produto.getPrecoUnitario()));
        this.view.getTxtQuantidadeInicial().setText(String.valueOf(produto.getQuantidadeInicial()));

        // Código é a chave de negócio do produto e não deve ser alterado em uma edição.
        this.view.getTxtCodigo().setEnabled(false);
    }

    // ---------------------------------------------------------------
    // Acesso disponibilizado aos Commands que concentram a regra de salvar.
    // ---------------------------------------------------------------

    public IProdutoView getView() {
        return this.view;
    }

    public IProdutoRepository getProdutoRepository() {
        return this.produtoRepository;
    }

    public Produto getProduto() {
        return this.produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }
}