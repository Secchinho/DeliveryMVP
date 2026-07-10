package com.ufes.delivery.view;

import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.ufes.delivery.model.Usuario;

public interface IGerenciarUsuariosView {
    JTextField getNomeBuscaTxt();
    JTable getUsuariosTable();
    JButton getAutorizarBtn();
    JButton getDesautorizarBtn();
    JButton getExcluirBtn();
    JButton getNovoBtn();
    JButton getFecharBtn();
    JFrame getJanelaPrincipal();
    List<Usuario> getUsuariosSelecionados();
    void atualizarTabela(List<Usuario> usuarios);
    void exibirMensagem(String mensagem, String titulo, int tipoMensagem);
    void abrirTelaCadastro();
    void fecharTela();
}