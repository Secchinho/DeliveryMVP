/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ufes.delivery.view;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author lucas
 */
public interface ILoginView {
    public JButton getAcessarButton();
    public JButton getCancelarButton();
    public JButton getCadastrarUsuarioButton();
    
    public JTextField getNomeUsuario();
    public JPasswordField getSenhaUsuario();
}
