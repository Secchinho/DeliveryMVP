/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.ufes.delivery.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

/**
 * Tela de Login do sistema.
 * 
 * @author raphael
 */
public class TelaLoginView extends JFrame implements ILoginView {

    // Componentes visuais
    private JPanel dadosDeAcessoPanel;
    private JLabel nomeLbl;
    private JLabel senhaLbl;
    private JTextField nomeTxt;
    private JPasswordField senhaTxt;
    private JButton cadastrarBtn;
    private JButton cancelarBtn;
    private JButton acessarBtn;

    /**
     * Creates new form TelaLoginView
     */
    public TelaLoginView() {
        initComponents();
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        // Layout principal da janela: BorderLayout com margens
        setLayout(new BorderLayout(10, 15));

        // =====================================================================
        // Painel de Dados de Acesso (Borda Titulada) - Fica no CENTRO
        // =====================================================================
        dadosDeAcessoPanel = new JPanel(new GridLayout(2, 2, 10, 15)); // 2 linhas, 2 colunas, gaps
        TitledBorder titulo = javax.swing.BorderFactory.createTitledBorder("Dados de Acesso");
        dadosDeAcessoPanel.setBorder(titulo);

        // Linha 1: Nome de Usuário
        nomeLbl = new JLabel("Nome de usuário");
        nomeTxt = new JTextField(15); // Tamanho aproximado para ficar igual à imagem
        
        // Linha 2: Senha
        senhaLbl = new JLabel("Senha");
        senhaTxt = new JPasswordField(15);

        // Adicionando ao Grid (Label, Campo)
        dadosDeAcessoPanel.add(nomeLbl);
        dadosDeAcessoPanel.add(nomeTxt);
        dadosDeAcessoPanel.add(senhaLbl);
        dadosDeAcessoPanel.add(senhaTxt);

        // =====================================================================
        // Painel de Botões (Centralizado) - Fica na parte SUL (inferior)
        // =====================================================================
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        
        acessarBtn = new JButton("Acessar");
        cancelarBtn = new JButton("Cancelar");
        cadastrarBtn = new JButton("Cadastrar usuário");

        botoesPanel.add(acessarBtn);
        botoesPanel.add(cancelarBtn);
        botoesPanel.add(cadastrarBtn);

        // =====================================================================
        // Adicionando os painéis ao JFrame
        // =====================================================================
        // Adiciona o painel de dados no centro, e os botões embaixo (South)
        add(dadosDeAcessoPanel, BorderLayout.CENTER);
        add(botoesPanel, BorderLayout.SOUTH);

        // Eventos (você pode implementar depois)
        nomeTxt.addActionListener(this::nomeTxtActionPerformed);
        cadastrarBtn.addActionListener(this::cadastrarBtnActionPerformed);
    }

    private void nomeTxtActionPerformed(ActionEvent evt) {
        // TODO: Adicione sua lógica de ação ao pressionar Enter no campo de nome, se necessário
    }

    private void cadastrarBtnActionPerformed(ActionEvent evt) {
        // TODO: Adicione sua lógica para abrir a tela de cadastro de usuários aqui
        // Exemplo: 
        // TelaUsuariosView telaUsuarios = new TelaUsuariosView();
        // telaUsuarios.setVisible(true);
    }

    // =========================================================================
    // MÉTODOS DA INTERFACE ILoginView
    // =========================================================================
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

    // =========================================================================
    // MÉTODO PARA CARREGAR DADOS (TESTE)
    // =========================================================================
    /**
     * Método para carregar dados de exemplo para teste.
     * Descomente e adicione sua lógica real de carregamento (ex: do banco de dados).
     */
    // public void carregarDadosExemplo() {
    //     // TODO: Insira aqui a lógica para preencher a tela com dados.
    //     // Exemplo: preencher campo de usuário automaticamente para testes:
    //     this.nomeTxt.setText("fulano123");
    //     this.senhaTxt.setText("123456");
    // }

    // =========================================================================
    // MAIN PARA TESTAR A TELA
    // =========================================================================
    public static void main(String[] args) {
        // Tenta usar o visual padrão do sistema operacional
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaLoginView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        SwingUtilities.invokeLater(() -> {
            TelaLoginView tela = new TelaLoginView();
            // Descomente a linha abaixo se quiser carregar dados de exemplo ao abrir a tela (para testar o preenchimento):
            // tela.carregarDadosExemplo();
            tela.setVisible(true);
        });
    }
}