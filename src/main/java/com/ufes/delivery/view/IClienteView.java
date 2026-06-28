package com.ufes.delivery.view;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JTextField;
import java.util.List;

public interface IClienteView {
    
    // --- Getters para o Presenter ler os dados ---
    JTextField getCampoNome();
    JTextField getCampoCpf();
    JTable getTabelaEndereco();

    // --- Métodos "Command" para o Presenter manipular a View ---
    String getNome();
    String getCpf();
    
    // Retorna uma lista de objetos contendo os dados das linhas da tabela
    // Exemplo: List<Object[]> onde cada array tem {isPadrao, logradouro, numero, ...}
    List<Object[]> getDadosEnderecos(); 
    
    void setNome(String nome);
    void setCpf(String cpf);
    
    // Preenche a tabela com uma lista de endereços (vinda do Model)
    void setEnderecosTabela(List<Object[]> enderecos);
    
    // --- Getters para os Botões (Para o Presenter anexar os listeners) ---
    JButton getBotaoSalvar();
    JButton getBotaoCancelar();

    // --- Utilitários de Feedback ---
    void exibirMensagem(String mensagem, String titulo, int tipoMensagem);
    void fecharTela();
    
    JFrame getJanelaPrincipal();
}