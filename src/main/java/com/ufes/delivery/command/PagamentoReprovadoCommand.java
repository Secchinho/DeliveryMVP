/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufes.delivery.command;

import com.ufes.delivery.presenters.PagamentoPresenter;
import com.ufes.delivery.view.IPagamentoView;
import javax.swing.JOptionPane;

/**
 *
 * @author lucas
 */
public class PagamentoReprovadoCommand extends PagamentoCommand {

    public PagamentoReprovadoCommand(PagamentoPresenter pagamentoPresenter) {
        super(pagamentoPresenter);
    }

    /**
     * Lógica de fechar para pagamento REPROVADO (US11 - Cenário 2).
     *
     * Quando o pagamento é reprovado, ao acionar Fechar a aplicação deve
     * retornar ao pedido sem remover itens, cliente, endereço ou valores
     * calculados, preservando o pedido para uma nova tentativa. O estoque e a
     * situação do pedido permanecem inalterados (US11 - regras de reprovação).
     *
     * Por isso, antes de encerrar a tela de resultado, é exibida uma mensagem
     * informativa indicando que os dados do pedido foram preservados, e em
     * seguida a View é fechada, devolvendo o atendente à tela do pedido.
     */
    @Override
    public void fechar() {
        IPagamentoView view = this.pagamentoPresenter.getView();
        view.exibirMensagem(
            "Pagamento reprovado. Os dados do pedido (itens, cliente, endereço e valores calculados) foram preservados para uma nova tentativa.",
            "Pagamento reprovado",
            JOptionPane.INFORMATION_MESSAGE
        );
        view.fecharTela();
    }

}
