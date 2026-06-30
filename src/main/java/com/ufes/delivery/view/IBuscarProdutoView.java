package com.ufes.delivery.view;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public interface IBuscarProdutoView {
    
    // --- Getters para Componentes ---
    JButton getBuscarButton();
    JButton getNovoButton();
    JButton getVisualizarButton();
    JButton getFecharButton();
    JComboBox<String> getComboBuscarPor();
    JFrame getJanelaPrincipal();
    JTextField getTxtValor();
    JTable getTabelaResultados();
    DefaultTableModel getModeloResultados();
}