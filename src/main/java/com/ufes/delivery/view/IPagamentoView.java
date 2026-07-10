package com.ufes.delivery.view;

import javax.swing.JButton;
import javax.swing.JFrame;

public interface IPagamentoView {
    void setStatusPagamento(String status, boolean aprovado);
    void setStatusPedido(String status);
    void setPedidoNumero(String numero);
    void setClienteNome(String nome);
    void setEnderecoEntrega(String endereco);
    void setTotalPedido(String total);
    void setSituacaoPagamento(String situacao);
    void setFormaPagamento(String forma);
    void setDataHoraPagamento(String dataHora);
    void setIdentificadorTransacao(String id);
    void setValorPago(String valor);
    void setSituacaoPedido(String situacao);
    void setPrazoEstimado(String prazo);
    void setObservacao(String obs);
    JButton getFecharButton();
    void fecharTela();
    JFrame getJanelaPrincipal();
    void exibirMensagem(String mensagem, String titulo, int tipoMensagem);
}