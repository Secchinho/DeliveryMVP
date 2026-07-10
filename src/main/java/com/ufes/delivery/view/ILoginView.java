package com.ufes.delivery.view;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public interface ILoginView {
    JButton getAcessarButton();
    JButton getCancelarButton();
    JButton getCadastrarUsuarioButton();
    JTextField getNomeUsuario();
    JPasswordField getSenhaUsuario();
    JFrame getJanelaPrincipal();
    void exibirMensagem(String mensagem, String titulo, int tipoMensagem);
}
