package com.ufes.delivery.view;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JTextField;

public interface IMovimentacaoEstoqueView {

    // --- Getters para o Presenter ler os dados de entrada ---
    String getTermoBuscaProduto();
    String getProdutoSelecionadoCodigo(); // Pega o código da linha selecionada na tabela
    
    String getQuantidadeMovimentar();
    String getMotivoAjuste();
    String getNotaFiscal();
    String getTipoMovimentacaoSelecionado();

    // --- Setters para o Presenter escrever dados (Produto Selecionado / Prévia) ---
    void setProdutoSelecionado(String nomeProduto);
    void setQuantidadeAtual(String quantidade);
    void setEstoqueAposMovimentacao(String quantidade);
    
    // --- Manipulação da Tabela de Busca ---
    void adicionarLinhaTabela(Object[] linha);
    void limparTabelaProdutos();

    // --- Getters para os Botões e Componentes (Para o Presenter anexar os ouvintes) ---
    JButton getBuscarButton();
    JButton getSelecionarButton();
    JButton getConfirmarMovimentacaoButton();
    JButton getCancelarButton();
    JComboBox<String> getTipoMovimentacaoComboBox();
    JTextField getNotaFiscalTextField(); // Para o Presenter habilitar/desabilitar conforme o tipo

    // --- Utilitários de Feedback e Navegação ---
    void exibirMensagem(String mensagem, String titulo, int tipoMensagem);
    void fecharTela();
    JFrame getJanelaPrincipal();
}