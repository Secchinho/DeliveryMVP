package com.ufes.delivery.view;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTextField;

public interface IBuscarProdutoView {

    // --- Getters para o Presenter ler os dados de entrada ---
    String getValorBusca();
    String getAtributoBusca();

    // --- Métodos para o Presenter manipular a tabela de resultados ---
    void limparTabelaResultados();
    void adicionarLinhaResultado(Object[] linha);
    String getCodigoProdutoSelecionado();

    // --- Getters para os Botões (para o Presenter anexar os ouvintes) ---
    JButton getBuscarButton();
    JButton getNovoButton();
    JButton getVisualizarButton();
    JButton getFecharButton();
    JComboBox<String> getComboBuscarPor();

    // --- Utilitários de Feedback e Navegação ---
    void exibirMensagem(String mensagem, String titulo, int tipoMensagem);
    void fecharTela();
    JFrame getJanelaPrincipal();
}