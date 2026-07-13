package com.ufes.delivery.view;

import javax.swing.*;
import java.awt.*;

public class CadastrarUsuarioView extends JFrame implements ICadastrarUsuarioView {
    private JButton confirmarBtn;
    private JButton cancelarBtn;
    private JTextField nomeCivilTxt;
    private JTextField nomeUsuarioTxt;
    private JPasswordField senhaTxt;

    public CadastrarUsuarioView() {
        initComponents();
        setTitle("Cadastrar Novo Usuário");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(420, 260);
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        this.nomeCivilTxt = new JTextField(15);
        this.nomeUsuarioTxt = new JTextField(15);
        this.senhaTxt = new JPasswordField(15);
        
        this.confirmarBtn = new JButton("Confirmar");
        this.cancelarBtn = new JButton("Cancelar");

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Nome Civil:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        panel.add(this.nomeCivilTxt, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Nome Usuário:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(this.nomeUsuarioTxt, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Senha:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(this.senhaTxt, gbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(this.confirmarBtn);
        buttonPanel.add(this.cancelarBtn);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);

        this.add(panel);
    }

    @Override
    public JButton getConfirmarButton() {
        return this.confirmarBtn;
    }

    @Override
    public JButton getCancelarButton() {
        return this.cancelarBtn;
    }

    @Override
    public JTextField getCampoNomeUsuario() {
        return this.nomeUsuarioTxt;
    }

    @Override
    public JTextField getCampoNomeCivil() {
        return this.nomeCivilTxt;
    }

    @Override
    public JPasswordField getCampoSenha() {
        return this.senhaTxt;
    }
    
    @Override
    public JFrame getJanelaPrincipal(){
        return this;
    }

    @Override
    public void exibirMensagem(String mensagem, String titulo, int tipo) {
        JOptionPane.showMessageDialog(this, mensagem, titulo, tipo);
    }
}