package com.ufes.delivery.view;

import javax.swing.JButton;
// JComboBox import removido — categoria agora é campo de texto livre
import javax.swing.JFrame;
import javax.swing.JTextField;

public interface IProdutoView {
    JButton getSalvarButton();
    JButton getFecharButton();
    JFrame getJanelaPrincipal();
    JTextField getTxtCodigo();
    JTextField getTxtNome();
    JTextField getTxtPrecoUnitario();
    JTextField getTxtQuantidadeInicial();
    JTextField getTxtCategoria();
    void exibirMensagem(String mensagem, String titulo, int tipoMensagem);
}