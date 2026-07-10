package com.ufes.delivery.view;

import java.util.function.IntConsumer;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JTable;

public interface IPainelOperacionalView {
    JFrame getJanelaPrincipal();
    void fecharTela();
    JMenuItem getMenuNovoPedido();
    JMenuItem getMenuBuscarProdutos();
    JMenuItem getMenuNovoProduto();
    JMenuItem getMenuMovimentacaoEstoque();
    JMenuItem getMenuNovoCliente();
    JMenuItem getMenuBuscarClientes();
    void exibirDataOperacao(String dataOperacao);
    void exibirIndicadores(
            int pedidosDoDia,
            int novos,
            int aguardandoPagamento,
            int emPreparo,
            int aguardandoEntrega,
            int emTransito,
            int entreguesHoje
    );
    JTable getTabelaPedidos();
    /**
     * Substitui o conteúdo da tabela de pedidos.
     * Cada linha deve conter: {numero, cliente, dataPedido, dataConclusao,
     * estado, valorTotal} — a coluna "Ação" (botão Visualizar) é adicionada
     * automaticamente pela View.
     */
    void atualizarListaPedidos(Object[][] linhas);
    /**
     * Registra o callback a ser chamado quando o usuário clicar em
     * "Visualizar" na linha de um pedido. O parâmetro é o índice da
     * linha (na tabela) do pedido clicado.
     */
    void setAcaoVisualizarPedidoListener(IntConsumer callback);
    void exibirSessaoUsuario(String nomeUsuario, String dataHoraLogin, String tipoUsuario);
    void exibirMensagem(String mensagem, String titulo, int tipoMensagem);
}