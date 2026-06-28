package com.ufes.delivery.view;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTextField;
import java.util.List;

public interface IPedidoView {
    
    // --- Getters para o Presenter ler os dados de entrada ---
    String getNomeCliente();
    String getEnderecoSelecionado();
    String getCupomTexto();
    
    // Retorna uma lista de objetos representando as linhas da tabela de itens
    // Exemplo: Object[] = { "Categoria", "Item", Preco, Quantidade, PrecoTotal }
    List<Object[]> getDadosItens();

    // --- Métodos para o Presenter atualizar a View ---
    void setNomeCliente(String nome);
    void setEnderecosEntrega(List<String> enderecos);
    void setCupomTexto(String cupom);
    
    // Atualiza os labels de totais sem expor os JLabels diretamente
    void atualizarTotais(String totalDescontos, String descTaxaEntrega, String taxaFinal, String totalPedido);
    
    // Manipulação da Tabela de Itens
    void adicionarItemTabela(Object[] item);
    void removerItemTabela(int indiceLinha);
    void limparTabelaItens();

    // --- Getters para os Botões (Para o Presenter anexar os listeners) ---
    JButton getNovoClienteButton();
    JButton getPagarButton();
    JButton getFecharButton();
    JButton getAplicarCupomButton();
    JComboBox<String> getEnderecoComboBox(); // Necessário para o Presenter escutar mudanças de endereço se precisar

    // --- Utilitários de Feedback e Navegação ---
    void exibirMensagem(String mensagem, String titulo, int tipoMensagem);
    void fecharTela();
    JFrame getJanelaPrincipal();
}