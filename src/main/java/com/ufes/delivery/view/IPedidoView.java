package com.ufes.delivery.view;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JTable;
import javax.swing.JTextField;

public interface IPedidoView {
    
    JButton getNovoClienteButton();
    JButton getBuscarClienteButton();
    JButton getPagarButton();
    JButton getFecharButton();
    JButton getAplicarCupomButton();
    JButton getAdicionarItemButton();
    
    JTextField getTxtCpfCliente();
    JLabel getLblNomeCliente();
    JTextField getTxtCupomDesconto();
    
    JTable getTabelaItens();
    
    JLabel getLblTotalDescontosValor();
    JLabel getLblDescontoTaxaEntregaValor();
    JLabel getLblTaxaEntregaFinalValor();
    JLabel getLblTotalPedidoValor();
    
    JComboBox<String> getEnderecoComboBox();
    JFrame getJanelaPrincipal();

    JMenuItem getMenuItemExcluirItem();

    void setTabelaItensEditable(boolean editable);

    /**
     * Insere uma nova linha em branco na tabela de itens para que o usuário
     * possa digitar manualmente os dados do item. Só deve ser invocado quando
     * o estado corrente do presenter é o CriarPedidoState.
     */
    void adicionarLinhaItemVazia();

    /**
     * Controla a visibilidade das colunas da tabela de itens conforme o
     * estado corrente do presenter.
     * <p>
     * Em modo criação ({@code true}), apenas as colunas "Item" e
     * "Quantidade" são exibidas, permitindo que o atendente informe
     * rapidamente o produto e a quantidade desejada. Em modo validação
     * ({@code false}), todas as colunas ("Categoria", "Item",
     * "Preço unitário", "Quantidade", "Preço total") são exibidas para
     * revisão completa antes do pagamento.
     *
     * @param modoCriacao {@code true} para exibir apenas "Item" e
     *                    "Quantidade"; {@code false} para exibir todas
     */
    void setModoCriacaoPedido(boolean modoCriacao);
}