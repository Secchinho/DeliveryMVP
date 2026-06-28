/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufes.delivery.presenters;

import com.ufes.delivery.model.Usuario;
import com.ufes.delivery.repository.IUsuarioRepository;
import com.ufes.delivery.view.ICadastrarUsuarioView;
import com.ufes.delivery.view.IGerenciarUsuariosView;
import java.util.List;
import java.util.Objects;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author lucas
 */
public class GerenciarUsuariosPresenter {

    private IGerenciarUsuariosView view;
    private IUsuarioRepository usuarioRepository;
    private ICadastrarUsuarioView cadastrarUsuarioView;

    public GerenciarUsuariosPresenter(IGerenciarUsuariosView view, 
            IUsuarioRepository usuarioRepository,
            ICadastrarUsuarioView cadastrarUsuarioView) {
        this.view = Objects.requireNonNull(view, "Insira uma tela.");
        this.usuarioRepository = Objects.requireNonNull(usuarioRepository, "Insira um repository.");
        this.cadastrarUsuarioView = Objects.requireNonNull(cadastrarUsuarioView,"Insira uma tela de Cadastro.");
        this.configurarEventos();
    }

    public void iniciar() {
        this.listarUsuarios();
        this.view.getJanelaPrincipal().setVisible(true);
    }

    private void configurarEventos() {
        this.view.getFecharBtn().addActionListener(v -> this.cancelar());

        this.view.getAutorizarBtn().addActionListener(v -> this.autorizarUsuario());

        this.view.getDesautorizarBtn().addActionListener(v -> this.desautorizarUsuario());

        this.view.getExcluirBtn().addActionListener(v -> this.excluirUsuario());

        this.view.getNovoBtn().addActionListener(v -> this.novoUsuario());

        this.view.getNomeBuscaTxt().addActionListener(v -> this.buscarUsuario());
    }

    private void autorizarUsuario() {
        List<Usuario> selecionados = this.view.getUsuariosSelecionados();
        if (selecionados == null || selecionados.isEmpty()) {
            this.exibirMensagem("Selecione pelo menos um usuário para autorizar.");
            return;
        }

        for (Usuario usuario : selecionados) {
            usuario.setSituacao("Autorizado");
            this.usuarioRepository.atualizar(usuario);

        }

        this.buscarUsuario();
        this.exibirMensagem("Usuário(s) autorizado(s) com sucesso.");
    }

    private void desautorizarUsuario() {
        List<Usuario> selecionados = this.view.getUsuariosSelecionados();
        if (selecionados == null || selecionados.isEmpty()) {
            this.exibirMensagem("Selecione pelo menos um usuário para desautorizar.");
            return;
        }

        for (Usuario usuario : selecionados) {
            usuario.setSituacao("Não autorizado");
            this.usuarioRepository.atualizar(usuario);

        }

        this.buscarUsuario();
        this.exibirMensagem("Usuário(s) desautorizado(s) com sucesso.");
    }

    private void excluirUsuario() {
        List<Usuario> selecionados = this.view.getUsuariosSelecionados();
        if (selecionados == null || selecionados.isEmpty()) {
            this.exibirMensagem("Selecione pelo menos um usuário para excluir.");
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(
                this.view.getJanelaPrincipal(),
                "Deseja realmente excluir o(s) usuário(s) selecionado(s)?",
                "Confirmação de exclusão",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacao == JOptionPane.YES_OPTION) {
            for (Usuario usuario : selecionados) {
                this.usuarioRepository.removerPorNomeUsuario(usuario.getUserName());
            }
            this.buscarUsuario();
            this.exibirMensagem("Usuário(s) excluído(s) com sucesso.");
        }
    }

    private void novoUsuario() {
        // Abre a tela de cadastro de usuário (mesma acessível via tela de login)
        CadastrarUsuarioPresenter cadastrarPresenter = new CadastrarUsuarioPresenter(this.cadastrarUsuarioView, this.usuarioRepository);
        cadastrarPresenter.iniciar();
    }

    private void buscarUsuario() {
        String nomeBusca = this.view.getNomeBuscaTxt().getText().trim();

        List<Usuario> usuarios;
        if (nomeBusca.isEmpty() || nomeBusca.length() < 2) {
            usuarios = this.usuarioRepository.listarUsuarios();
        } else {
            usuarios = this.usuarioRepository.buscarPorNomeContendo(nomeBusca);
        }

        this.atualizarTabela(usuarios);
    }

    private void cancelar() {
        this.view.getJanelaPrincipal().dispose();
    }

    private void listarUsuarios() {
        List<Usuario> usuarios = this.usuarioRepository.listarUsuarios();
        this.atualizarTabela(usuarios);
    }

    private void atualizarTabela(List<Usuario> usuarios) {
        JTable tabela = this.view.getUsuariosTable();
        DefaultTableModel modelo = (DefaultTableModel) tabela.getModel();
        modelo.setRowCount(0);

        for (Usuario usuario : usuarios) {
            modelo.addRow(new Object[]{
                usuario.getUserName(),
                usuario.getNome(),
                usuario.getSituacao(),
                usuario.getTipo()
            });
        }
    }

    private void exibirMensagem(String mensagem) {
        JOptionPane.showMessageDialog(
                this.view.getJanelaPrincipal(),
                mensagem,
                "Gerenciar Usuários",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
