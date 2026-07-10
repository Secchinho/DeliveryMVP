package com.ufes.delivery.view;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import java.util.List;

public interface IBuscarClienteView {
    String getValorBusca();
    String getAtributoBusca();
    String getCpfClienteSelecionado();
    void exibirClientes(List<Object[]> dadosClientes);
    void exibirMensagem(String mensagem, String titulo, int tipo);
    JFrame getJanelaPrincipal();
    JButton getBuscarClienteButton();
    JButton getNovoClienteButton();
    JButton getVisualizarClienteButton();
    JButton getFecharButton();
}