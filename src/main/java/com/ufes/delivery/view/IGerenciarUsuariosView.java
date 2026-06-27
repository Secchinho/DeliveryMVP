package com.ufes.delivery.view;

import java.util.List;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.ufes.delivery.model.Usuario;

public interface IGerenciarUsuariosView {
    
    // Getters para os componentes que o Presenter precisa ler ou manipular
    JTextField getNomeBuscaTxt();
    JTable getUsuariosTable();
    JButton getAutorizarBtn();
    JButton getDesautorizarBtn();
    JButton getExcluirBtn();
    JButton getNovoBtn();
    JButton getFecharBtn();
    
    // Métodos de "Ação" que o Presenter pode invocar para atualizar a tela
    List<Usuario> getUsuariosSelecionados();
    void atualizarTabela(List<Usuario> usuarios);
    void exibirMensagem(String mensagem, String titulo, int tipoMensagem);
    void abrirTelaCadastro();
    void fecharTela();
}