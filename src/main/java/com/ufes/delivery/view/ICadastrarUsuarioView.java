package com.ufes.delivery.view;

import javax.swing.JButton;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public interface ICadastrarUsuarioView {
    JButton getConfirmarButton();
    JButton getCancelarButton();
    JTextField getCampoNomeUsuario();
    JTextField getCampoNomeCivil();
    JPasswordField getCampoSenha();
}
