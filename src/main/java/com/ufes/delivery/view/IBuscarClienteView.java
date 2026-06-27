package com.ufes.delivery.view;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import java.util.List;

public interface IBuscarClienteView {
    
    // Métodos para o Presenter ler os dados da tela
    String getValorBusca();
    String getAtributoBusca();
    String getCpfClienteSelecionado();

    // Métodos para o Presenter atualizar a tela
    void exibirClientes(List<Object[]> dadosClientes);
    void exibirMensagem(String mensagem, String titulo, int tipo);
    JFrame getJanelaPrincipal();

    // Getters para os botões (para o Presenter adicionar os ActionListeners)
    JButton getBuscarClienteButton();
    JButton getNovoClienteButton();
    JButton getVisualizarClienteButton();
    JButton getFecharButton();
}