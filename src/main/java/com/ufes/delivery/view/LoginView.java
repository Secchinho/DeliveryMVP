/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.ufes.delivery.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

/**
 * Tela de Login do sistema.
 *
 * @author fabricio
 */
public class LoginView extends JFrame implements ILoginView {

    private JPanel dadosDeAcessoPanel;
    private JLabel nomeLbl;
    private JLabel senhaLbl;
    private JTextField nomeTxt;
    private JPasswordField senhaTxt;
    private JButton cadastrarBtn;
    private JButton cancelarBtn;
    private JButton acessarBtn;

    @Override
    public JButton getAcessarButton() {
        return this.acessarBtn;
    }

    @Override
    public JButton getCancelarButton() {
        return this.cancelarBtn;
    }

    @Override
    public JButton getCadastrarUsuarioButton() {
        return this.cadastrarBtn;
    }

    @Override
    public JTextField getNomeUsuario() {
        return this.nomeTxt;
    }

    @Override
    public JPasswordField getSenhaUsuario() {
        return this.senhaTxt;
    }

    @Override
    public JFrame getJanelaPrincipal() {
        return this;
    }

    public LoginView() {
        initComponents();
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(480, 320);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void initComponents() {
        // Layout principal da janela
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(new EmptyBorder(15, 15, 15, 15));

        dadosDeAcessoPanel = new JPanel(new GridBagLayout());
        TitledBorder titulo = javax.swing.BorderFactory.createTitledBorder("Dados de Acesso");
        dadosDeAcessoPanel.setBorder(titulo);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weighty = 0;

        // Linha 1: Nome de Usuário
        nomeLbl = new JLabel("Nome de usuário");
        nomeTxt = new JTextField(15);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        dadosDeAcessoPanel.add(nomeLbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        dadosDeAcessoPanel.add(nomeTxt, gbc);

        // Linha 2: Senha
        senhaLbl = new JLabel("Senha");
        senhaTxt = new JPasswordField(15);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        dadosDeAcessoPanel.add(senhaLbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        dadosDeAcessoPanel.add(senhaTxt, gbc);

        // Linha invisível que absorve o espaço vertical sobrando,
        // mantendo os campos colados ao título "Dados de Acesso"
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weighty = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        dadosDeAcessoPanel.add(Box.createGlue(), gbc);

        // =====================================================================
        // Painel de Botões (Centralizado) - Fica na parte SUL (inferior)
        // =====================================================================
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));

        acessarBtn = new JButton("Acessar");
        cancelarBtn = new JButton("Cancelar");
        cadastrarBtn = new JButton("Cadastrar usuário");

        botoesPanel.add(acessarBtn);
        botoesPanel.add(cancelarBtn);
        botoesPanel.add(cadastrarBtn);

        panelPrincipal.add(dadosDeAcessoPanel, BorderLayout.CENTER);
        panelPrincipal.add(botoesPanel, BorderLayout.SOUTH);

        getContentPane().add(panelPrincipal);
    }
}