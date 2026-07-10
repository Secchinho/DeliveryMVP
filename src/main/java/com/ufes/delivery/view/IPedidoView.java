package com.ufes.delivery.view;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTextField;
import java.util.List;

public interface IPedidoView {
    String getNomeCliente();
    String getEnderecoSelecionado();
    String getCupomTexto();
    List<Object[]> getDadosItens();
    void setNomeCliente(String nome);
    void setEnderecosEntrega(List<String> enderecos);
    void setCupomTexto(String cupom);
    void atualizarTotais(String totalDescontos, String descTaxaEntrega, String taxaFinal, String totalPedido);
    void adicionarItemTabela(Object[] item);
    void removerItemTabela(int indiceLinha);
    void limparTabelaItens();
    JButton getNovoClienteButton();
    JButton getPagarButton();
    JButton getFecharButton();
    JButton getAplicarCupomButton();
    JComboBox<String> getEnderecoComboBox();
    void exibirMensagem(String mensagem, String titulo, int tipoMensagem);
    void fecharTela();
    JFrame getJanelaPrincipal();
}