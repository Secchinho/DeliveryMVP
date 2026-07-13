/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufes.delivery.command;

import com.ufes.delivery.presenters.PagamentoPresenter;

/**
 *
 * @author lucas
 */
public class PagamentoAprovadoCommand extends PagamentoCommand {

    public PagamentoAprovadoCommand(PagamentoPresenter pagamentoPresenter) {
        super(pagamentoPresenter);
    }

    /**
     * Lógica de fechar para pagamento APROVADO (US11 - Cenário 1).
     *
     * Quando o pagamento é aprovado, todas as informações já foram exibidas na
     * View pelo Presenter antes do usuário clicar em Fechar ("Pagamento
     * aprovado", "Pedido pronto para entrega", identificador da transação,
     * valor pago, prazo estimado de entrega) e o pedido já está em estado
     * "Aguardando entrega". Portanto, basta encerrar a tela de resultado.
     */
    @Override
    public void fechar() {
        this.pagamentoPresenter.getView().fecharTela();
    }

}
