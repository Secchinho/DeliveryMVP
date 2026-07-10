package com.ufes.delivery.view;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JTextField;

public interface IMovimentacaoEstoqueView {
    String getTermoBuscaProduto();
    String getProdutoSelecionadoCodigo(); 
    String getQuantidadeMovimentar();
    String getMotivoAjuste();
    String getNotaFiscal();
    String getTipoMovimentacaoSelecionado();
    void setProdutoSelecionado(String nomeProduto);
    void setQuantidadeAtual(String quantidade);
    void setEstoqueAposMovimentacao(String quantidade);
    void adicionarLinhaTabela(Object[] linha);
    void limparTabelaProdutos();
    JButton getBuscarButton();
    JButton getSelecionarButton();
    JButton getConfirmarMovimentacaoButton();
    JButton getCancelarButton();
    JComboBox<String> getTipoMovimentacaoComboBox();
    JTextField getNotaFiscalTextField();
    void exibirMensagem(String mensagem, String titulo, int tipoMensagem);
    void fecharTela();
    JFrame getJanelaPrincipal();
}