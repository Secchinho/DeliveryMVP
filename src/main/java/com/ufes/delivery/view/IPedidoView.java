package com.ufes.delivery.view;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JTable;
import javax.swing.JTextField;

public interface IPedidoView {
    
    JButton getNovoClienteButton();
    JButton getBuscarClienteButton();
    JButton getPagarButton();
    JButton getFecharButton();
    JButton getAplicarCupomButton();
    
    JTextField getTxtCpfCliente();
    JLabel getLblNomeCliente();
    JTextField getTxtCupomDesconto();
    
    JTable getTabelaItens();
    
    JLabel getLblTotalDescontosValor();
    JLabel getLblDescontoTaxaEntregaValor();
    JLabel getLblTaxaEntregaFinalValor();
    JLabel getLblTotalPedidoValor();
    
    JComboBox<String> getEnderecoComboBox();
    JFrame getJanelaPrincipal();

    JMenuItem getMenuItemExcluirItem();

    void setTabelaItensEditable(boolean editable);
}