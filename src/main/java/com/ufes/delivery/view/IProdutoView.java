package com.ufes.delivery.view;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import java.util.List;
import javax.swing.JTextField;

public interface IProdutoView {

// --- Getters para Componentes ---
    JButton getSalvarButton();
    JButton getFecharButton();
    
    JFrame getJanelaPrincipal();
    
    JTextField getTxtCodigo();
    JTextField getTxtNome();
    JTextField getTxtPrecoUnitario();
    JTextField getTxtQuantidadeInicial();
    
    JComboBox<String> getComboCategoria();
    

    
}