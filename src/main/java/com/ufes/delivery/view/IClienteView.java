package com.ufes.delivery.view;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ButtonGroup;
import javax.swing.table.DefaultTableModel;

public interface IClienteView {
    JTextField getCampoNome();
    JTextField getCampoCpf();
    JTable getTabelaEndereco();
    JButton getBotaoSalvar();
    JButton getBotaoCancelar();
    JFrame getJanelaPrincipal();
    DefaultTableModel getModeloEnderecos();
    void exibirMensagem(String mensagem, String titulo, int tipoMensagem);
}