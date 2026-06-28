package com.ufes.delivery.view;

import javax.swing.JButton;
import javax.swing.JFrame;

public interface IPagamentoView {

    // --- Setters para o Presenter popular os dados visuais (Read-Only) ---
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

    // --- Getters para os Botões (Para o Presenter anexar os ouvintes) ---
    JButton getFecharButton();

    // --- Utilitários de Feedback e Navegação ---
    void fecharTela();
    JFrame getJanelaPrincipal();
}