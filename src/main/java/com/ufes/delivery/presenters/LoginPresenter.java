/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufes.delivery.presenters;

import com.ufes.delivery.view.ILoginView;
import com.ufes.delivery.model.Usuario;
import com.ufes.util.AutenticacaoUsuarioService;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/**
 * Presenter responsável por mediar a interação entre a ILoginView e o
 * AutenticacaoUsuarioService, seguindo o padrão MVP Passive View.
 *
 * Responsabilidades:
 * - Configurar os eventos dos botões Acessar, Cancelar e Cadastrar
 * - Validar o formato dos dados de entrada antes de delegar ao service
 * - Tratar as exceções do service e apresentar mensagens adequadas ao usuário
 * - Navegar para a tela de cadastro ou painel operacional conforme o resultado
 *
 * @author lucas
 */
public class LoginPresenter {

    private ILoginView view;
    private AutenticacaoUsuarioService autenticacaoService;

    public LoginPresenter(ILoginView view, AutenticacaoUsuarioService service) {
        this.view = view;
        this.autenticacaoService = service;
        this.configurarEventos();
    }

    public void configurarEventos() {
        this.view.getAcessarButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                realizarLogin();
            }
        });

        this.view.getCancelarButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelar();
            }
        });

        this.view.getCadastrarUsuarioButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirCadastroUsuario();
            }
        });
    }

    public void iniciar() {
        this.view.getJanelaPrincipal().setVisible(true);
    }

    public void realizarLogin() {
        try {
            String nomeUsuario = this.view.getNomeUsuario().getText().trim();
            String senha = new String(this.view.getSenhaUsuario().getPassword());

            // US01 - Cenário 3: Validar formato do nome de usuário ANTES de chamar o service
            if (!validarFormatoNomeUsuario(nomeUsuario)) {
                JOptionPane.showMessageDialog(
                        this.view.getJanelaPrincipal(),
                        "O nome de usuário deve conter de 3 a 30 caracteres, "
                        + "usando apenas letras minúsculas e algarismos, sem espaços.",
                        "Erro de validação",
                        JOptionPane.ERROR_MESSAGE
                );
                this.view.getSenhaUsuario().setText("");
                return;
            }

            // Validar tamanho da senha ANTES de chamar o service
            if (senha == null || senha.length() < 8 || senha.length() > 64) {
                JOptionPane.showMessageDialog(
                        this.view.getJanelaPrincipal(),
                        "A senha deve conter de 8 a 64 caracteres.",
                        "Erro de validação",
                        JOptionPane.ERROR_MESSAGE
                );
                this.view.getSenhaUsuario().setText("");
                return;
            }

            // Delegar autenticação ao service — retorna Usuario, não boolean
            Usuario usuario = this.autenticacaoService.autenticarUsuario(nomeUsuario, senha);

            // Login bem-sucedido: fechar tela de login e abrir painel operacional
            this.view.getJanelaPrincipal().setVisible(false);
            // TODO: Navegar para PainelOperacionalPresenter com o usuario autenticado
            // Exemplo: new PainelOperacionalPresenter(usuario, ...).iniciar();

        } catch (IllegalArgumentException e) {
            // Campos obrigatórios ausentes ou em branco (validação do service)
            JOptionPane.showMessageDialog(
                    this.view.getJanelaPrincipal(),
                    e.getMessage(),
                    "Erro de validação",
                    JOptionPane.ERROR_MESSAGE
            );
            this.view.getSenhaUsuario().setText("");
        } catch (RuntimeException e) {
            String mensagem = e.getMessage();

            if (mensagem != null && mensagem.contains("não autorizado")) {
                // US01 - Cenário 5: Usuário pendente ou não autorizado
                JOptionPane.showMessageDialog(
                        this.view.getJanelaPrincipal(),
                        "O acesso depende de autorização administrativa.",
                        "Acesso negado",
                        JOptionPane.WARNING_MESSAGE
                );
            } else {
                // US01 - Cenário 4: Credenciais inválidas — não identificar qual dado falhou
                JOptionPane.showMessageDialog(
                        this.view.getJanelaPrincipal(),
                        "Credenciais inválidas.",
                        "Erro de autenticação",
                        JOptionPane.ERROR_MESSAGE
                );
            }
            this.view.getSenhaUsuario().setText("");
        }
    }

    private void cancelar() {
        this.view.getNomeUsuario().setText("");
        this.view.getSenhaUsuario().setText("");
        this.view.getJanelaPrincipal().dispose();
    }

    private void abrirCadastroUsuario() {
        // Abre o CadastrarUsuarioPresenter para cadastro de novo usuário
        // TODO: instanciar CadastrarUsuarioPresenter com as dependências necessárias
        // Exemplo: new CadastrarUsuarioPresenter(new CadastrarUsuarioView(), new CadastrarUsuarioService(repository)).iniciar();
    }

    private boolean validarFormatoNomeUsuario(String nomeUsuario) {
        if (nomeUsuario == null || nomeUsuario.isBlank()) {
            return false;
        }
        // US01: 3 a 30 caracteres, somente letras minúsculas e algarismos, sem espaços
        return nomeUsuario.matches("^[a-z0-9]{3,30}$");
    }
}
