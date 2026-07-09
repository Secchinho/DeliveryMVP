package com.ufes.delivery.view;

import java.util.function.IntConsumer;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JTable;

public interface IPainelOperacionalView {

    // ---------------------------------------------------------------
    // Janela
    // ---------------------------------------------------------------
    JFrame getJanelaPrincipal();
    void fecharTela();

    // ---------------------------------------------------------------
    // Menu de operações (atalhos para as demais telas do sistema)
    // ---------------------------------------------------------------
    JMenuItem getMenuNovoPedido();
    JMenuItem getMenuBuscarProdutos();
    JMenuItem getMenuNovoProduto();
    JMenuItem getMenuMovimentacaoEstoque();
    JMenuItem getMenuNovoCliente();
    JMenuItem getMenuBuscarClientes();

    // ---------------------------------------------------------------
    // Data de operação
    // ---------------------------------------------------------------
    void exibirDataOperacao(String dataOperacao);

    // ---------------------------------------------------------------
    // Indicadores/contadores de pedidos (cards)
    // ---------------------------------------------------------------
    void exibirIndicadores(
            int pedidosDoDia,
            int novos,
            int aguardandoPagamento,
            int emPreparo,
            int aguardandoEntrega,
            int emTransito,
            int entreguesHoje
    );

    // ---------------------------------------------------------------
    // Lista de pedidos do dia
    // ---------------------------------------------------------------
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

    // ---------------------------------------------------------------
    // Barra de status — sessão do usuário logado
    // ---------------------------------------------------------------
    void exibirSessaoUsuario(String nomeUsuario, String dataHoraLogin, String tipoUsuario);

    // ---------------------------------------------------------------
    // Mensagens genéricas ao usuário
    // ---------------------------------------------------------------
    void exibirMensagem(String mensagem, String titulo, int tipoMensagem);
}