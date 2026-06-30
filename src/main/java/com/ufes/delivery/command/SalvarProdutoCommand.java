/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufes.delivery.command;

import com.ufes.delivery.model.Produto;
import com.ufes.delivery.presenters.ProdutoPresenter;
import com.ufes.delivery.repository.IProdutoRepository;
import com.ufes.delivery.view.IProdutoView;
import javax.swing.JOptionPane;

/**
 * Command responsável por validar e persistir a inclusão de um novo
 * produto (US07 - Buscar e cadastrar produto).
 *
 * @author lucas
 */
public class SalvarProdutoCommand extends ProdutoPresenterCommand {

    public SalvarProdutoCommand(ProdutoPresenter produtoPresenter) {
        super(produtoPresenter);
    }

    @Override
    public void salvar() {
        IProdutoView view = produtoPresenter.getView();
        IProdutoRepository produtoRepository = produtoPresenter.getProdutoRepository();

        String codigo = view.getTxtCodigo().getText().trim();
        String nome = view.getTxtNome().getText().trim();
        Object categoriaSelecionada = view.getComboCategoria().getSelectedItem();
        String categoria = categoriaSelecionada == null ? "" : categoriaSelecionada.toString().trim();
        String precoTexto = view.getTxtPrecoUnitario().getText().trim();
        String quantidadeTexto = view.getTxtQuantidadeInicial().getText().trim();

        if (codigo.isEmpty()) {
            this.exibirErro("O código do produto é obrigatório.");
            return;
        }

        if (produtoRepository.getPorCodigo(codigo) != null) {
            this.exibirErro("O código informado já está em uso.");
            return;
        }

        if (nome.length() < 2 || nome.length() > 120) {
            this.exibirErro("O nome do produto deve conter de 2 a 120 caracteres.");
            return;
        }

        if (categoria.isEmpty()) {
            this.exibirErro("A categoria do produto é obrigatória.");
            return;
        }

        double precoUnitario;
        try {
            precoUnitario = Double.parseDouble(precoTexto.replace(",", "."));
        } catch (NumberFormatException ex) {
            this.exibirErro("O preço unitário informado é inválido.");
            return;
        }

        if (precoUnitario <= 0) {
            this.exibirErro("O preço unitário deve ser maior que R$ 0,00.");
            return;
        }

        int quantidadeInicial;
        try {
            quantidadeInicial = Integer.parseInt(quantidadeTexto);
        } catch (NumberFormatException ex) {
            this.exibirErro("A quantidade inicial em estoque deve ser um número inteiro.");
            return;
        }

        if (quantidadeInicial < 0) {
            this.exibirErro("A quantidade inicial em estoque não pode ser negativa.");
            return;
        }

        Produto produto = new Produto(nome, codigo, categoria, precoUnitario, quantidadeInicial);

        produtoRepository.adicionar(produto);
        produtoPresenter.setProduto(produto);

        JOptionPane.showMessageDialog(view.getJanelaPrincipal(), "Produto salvo com sucesso.");
        view.getJanelaPrincipal().dispose();
    }

    private void exibirErro(String mensagem) {
        JOptionPane.showMessageDialog(
                produtoPresenter.getView().getJanelaPrincipal(),
                mensagem,
                "Erro de validação",
                JOptionPane.ERROR_MESSAGE);
    }
}