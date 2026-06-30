/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufes.delivery.presenters;

import com.ufes.delivery.model.Produto;
import com.ufes.delivery.repository.IProdutoRepository;
import com.ufes.delivery.view.IBuscarProdutoView;
import com.ufes.delivery.view.IProdutoView;
import com.ufes.delivery.view.ProdutoView;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.Objects;

/**
 * Presenter responsável pela tela de busca de produtos (US07).
 *
 * Regras aplicadas (Especificação de Requisitos - US07):
 *  - "Buscar por" aceita Código, Nome ou Categoria, e o critério selecionado
 *    determina a validação/consulta executada (Regras de validação de dados de
 *    entrada - Atributo de busca).
 *  - O campo Valor é obrigatório para a execução da busca.
 *  - A tabela de resultados deve exibir código, nome, categoria, preço unitário
 *    e estoque atual.
 *  - "Visualizar" deve abrir o produto correspondente à linha acionada
 *    (Cenário 5 - Abrir produto pela ação da linha), exigindo uma linha
 *    selecionada.
 *  - "Novo" deve abrir o cadastro de produto em branco.
 *
 * @author lucas
 */
public class BuscarProdutoPresenter {

    private static final int COLUNA_CODIGO = 0;
    private static final int COLUNA_NOME = 1;
    private static final int COLUNA_CATEGORIA = 2;
    private static final int COLUNA_PRECO = 3;
    private static final int COLUNA_ESTOQUE = 4;

    private final IBuscarProdutoView view;
    private final IProdutoRepository produtoRepository;
    private List<Produto> produtos;

    public BuscarProdutoPresenter(IBuscarProdutoView view, IProdutoRepository produtoRepository) {
        this.view = Objects.requireNonNull(view);
        this.produtoRepository = Objects.requireNonNull(produtoRepository);

        this.iniciar();
        this.configurarEventos();
    }

    /**
     * Carrega o estado inicial da tela: lista todos os produtos cadastrados
     * e popula a tabela de resultados.
     */
    public void iniciar() {
        this.produtos = this.produtoRepository.listarProdutos();
        this.popularTabela(this.produtos);
    }

    /**
     * Liga os eventos da view às operações do presenter.
     */
    public void configurarEventos() {
        this.view.getBuscarButton().addActionListener(e -> this.buscarProdutos());
        this.view.getNovoButton().addActionListener(e -> this.abrirNovoProduto());
        this.view.getVisualizarButton().addActionListener(e -> this.visualizarProdutoSelecionado());
        this.view.getFecharButton().addActionListener(e -> this.view.getJanelaPrincipal().dispose());
    }

    /**
     * Executa a busca de produtos de acordo com o atributo selecionado
     * (Código, Nome ou Categoria) e o valor informado.
     */
    private void buscarProdutos() {
        String criterio = (String) this.view.getComboBuscarPor().getSelectedItem();
        String valor = this.view.getTxtValor().getText() == null
                ? ""
                : this.view.getTxtValor().getText().trim();

        if (criterio == null) {
            this.exibirMensagem("Selecione o atributo de busca.");
            return;
        }

        if (valor.isEmpty()) {
            this.exibirMensagem("O valor da busca é obrigatório.");
            return;
        }

        List<Produto> resultado;

        switch (criterio) {
            case "Nome":
                resultado = this.produtoRepository.buscarPorNome(valor);
                break;
            case "Categoria":
                resultado = this.produtoRepository.buscarPorCategoria(valor);
                break;
            case "Código":
                resultado = this.buscarPorCodigo(valor);
                break;
            default:
                this.exibirMensagem("Critério de busca inválido.");
                return;
        }

        this.produtos = resultado;
        this.popularTabela(this.produtos);

        if (this.produtos == null || this.produtos.isEmpty()) {
            this.exibirMensagem("Nenhum produto encontrado para o critério informado.");
        }
    }

    /**
     * Trata a busca por código, validando que o valor informado seja um
     * inteiro positivo (Regras de validação - Código: inteiro positivo).
     */
    private List<Produto> buscarPorCodigo(String valor) {
        if (!valor.matches("\\d+")) {
            this.exibirMensagem("O código deve ser um valor inteiro positivo.");
            return List.of();
        }

        return this.produtoRepository.getPorCodigo(valor)
                .map(List::of)
                .orElseGet(List::of);
    }

    /**
     * Preenche a tabela de resultados com código, nome, categoria, preço
     * unitário e estoque atual de cada produto, conforme as colunas definidas
     * na BuscarProdutoView.
     */
    private void popularTabela(List<Produto> produtosParaExibir) {
        DefaultTableModel modelo = this.view.getModeloResultados();
        modelo.setRowCount(0);

        if (produtosParaExibir == null) {
            return;
        }

        for (Produto produto : produtosParaExibir) {
            modelo.addRow(new Object[]{
                produto.getCodigo(),
                produto.getNome(),
                produto.getCategoria(),
                produto.getPrecoUnitario(),
                produto.getQuantidadeInicial(),
                "Visualizar"
            });
        }
    }

    /**
     * Abre a tela de cadastro de produto em branco (comando Novo).
     */
    private void abrirNovoProduto() {
        //ProdutoPresenter produtoPresenter = new ProdutoPresenter(this.produtoView, this.produtoRepository);
        //produtoPresenter.iniciar();
    }

    /**
     * Abre o produto correspondente à linha selecionada na tabela
     * (Cenário 5 - Abrir produto pela ação da linha).
     */
    private void visualizarProdutoSelecionado() {
        int linhaSelecionada = this.view.getTabelaResultados().getSelectedRow();

        if (linhaSelecionada < 0) {
            this.exibirMensagem("Selecione um produto para visualizar.");
            return;
        }

        int linhaModelo = this.view.getTabelaResultados().convertRowIndexToModel(linhaSelecionada);
        String codigo = String.valueOf(this.view.getModeloResultados().getValueAt(linhaModelo, COLUNA_CODIGO));

        this.produtoRepository.getPorCodigo(codigo).ifPresentOrElse(produto -> {
            //ProdutoPresenter produtoPresenter = new ProdutoPresenter(new ProdutoView, this.produtoRepository, produto);
            //produtoPresenter.iniciar();
        }, () -> this.exibirMensagem("Produto não encontrado para a linha selecionada."));
    }

    /**
     * Exibe mensagem de validação/erro ao usuário, associada à operação que a
     * originou, em português.
     */
    private void exibirMensagem(String mensagem) {
        JOptionPane.showMessageDialog(
                this.view.getJanelaPrincipal(),
                mensagem,
                "Produtos",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}