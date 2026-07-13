/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufes.delivery.presenters;

import java.time.LocalDateTime;

/**
 * Resultado de uma tentativa simulada de pagamento (US11).
 *
 * <p>Contém todos os valores que devem ser apresentados na tela de pagamento,
 * derivados do pedido e da simulação. É produzido por
 * {@code PagamentoService.processar(...)} e consumido pelo
 * {@code PagamentoPresenter} para popular a View.</p>
 *
 * @author lucas
 */
public class ResultadoPagamento {

    private final boolean aprovado;
    private final String formaPagamento;
    private final String identificadorTransacao;
    private final LocalDateTime dataHoraPagamento;
    private final double valorPago;
    private final LocalDateTime prazoEstimadoEntrega;

    public ResultadoPagamento(
            boolean aprovado,
            String formaPagamento,
            String identificadorTransacao,
            LocalDateTime dataHoraPagamento,
            double valorPago,
            LocalDateTime prazoEstimadoEntrega) {

        this.aprovado = aprovado;
        this.formaPagamento = formaPagamento;
        this.identificadorTransacao = identificadorTransacao;
        this.dataHoraPagamento = dataHoraPagamento;
        this.valorPago = valorPago;
        this.prazoEstimadoEntrega = prazoEstimadoEntrega;
    }

    public boolean isAprovado() {
        return aprovado;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public String getIdentificadorTransacao() {
        return identificadorTransacao;
    }

    public LocalDateTime getDataHoraPagamento() {
        return dataHoraPagamento;
    }

    public double getValorPago() {
        return valorPago;
    }

    public LocalDateTime getPrazoEstimadoEntrega() {
        return prazoEstimadoEntrega;
    }
}
