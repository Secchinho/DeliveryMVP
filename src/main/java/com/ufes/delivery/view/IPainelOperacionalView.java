package com.ufes.delivery.view;

import java.util.function.IntConsumer;
import javax.swing.JComboBox;
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
    JMenuItem getMenuGerenciarUsuarios();
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
    void atualizarListaPedidos(Object[][] linhas);
    void setAcaoVisualizarPedidoListener(IntConsumer callback);
    void exibirSessaoUsuario(String nomeUsuario, String dataHoraLogin, String tipoUsuario);
    void exibirMensagem(String mensagem, String titulo, int tipoMensagem);
}