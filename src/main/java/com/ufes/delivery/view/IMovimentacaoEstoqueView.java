package com.ufes.delivery.view;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public interface IMovimentacaoEstoqueView {
    JButton getBuscarButton();
    JButton getSelecionarButton();
    JButton getConfirmarMovimentacaoButton();
    JButton getCancelarButton();
    
    JComboBox<String> getTipoMovimentacaoComboBox();
    JFrame getJanelaPrincipal();
    
    JTextField getNotaFiscalTextField();
    JTextField getTxtBuscarProduto();
    JTextField getTxtProdutoSelecionado();
    JTextField getTxtQuantidadeAtual();
    JTextField getTxtQuantidadeMovimentar();
    JTextField getTxtMotivoAjuste();
    JTextField getTxtEstoqueAposMovimentacao();
    JTextField getTxtNotaFiscal();
    
    JLabel getLblAvisoPrevisualizacao();
    JLabel getLblAvisoRegras();
    
}