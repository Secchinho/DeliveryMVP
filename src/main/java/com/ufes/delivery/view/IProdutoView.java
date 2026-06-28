package com.ufes.delivery.view;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import java.util.List;

public interface IProdutoView {

    // --- Getters para o Presenter ler os dados da View ---
    String getCodigo();
    String getNome();
    String getCategoriaSelecionada();
    String getPrecoUnitario();
    String getQuantidadeInicial();

    // --- Setters para o Presenter preencher a View (Modo Edição) ---
    void setCodigo(String codigo);
    void setNome(String nome);
    void setPrecoUnitario(String preco);
    void setQuantidadeInicial(String quantidade);

    // --- Manipulação do ComboBox de Categorias ---
    void setCategorias(List<String> categorias);
    void selecionarCategoria(String categoria);

    // --- Getters para os Botões (Para o Presenter anexar os ouvintes) ---
    JButton getSalvarButton();
    JButton getFecharButton(); // Mapeado para o botão Cancelar do diagrama

    // --- Utilitários de Feedback e Navegação ---
    void exibirMensagem(String mensagem, String titulo, int tipoMensagem);
    void fecharTela();
    JFrame getJanelaPrincipal();
}