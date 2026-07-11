/*
 * TipoAjusteCommand.java
 *
 * Command concreto para movimentacao de estoque do tipo Ajuste.
 * Herda de TipoMovimentacaoCommand e implementa confirmar().
 *
 * Regra especifica (US08, Cenario 2):
 *   - O tipo Ajuste de estoque exige motivo obrigatorio.
 *   - Se o motivo estiver ausente, a movimentacao nao deve ser
 *     persistida e a interface deve informar que o motivo do ajuste
 *     e obrigatorio.
 *
 * O ajuste pode aumentar ou reduzir o estoque (quantidade positiva ou
 * negativa), porem o estoque resultante nunca pode ser negativo
 * (validado pelo Presenter).
 *
 * Regras comuns ja validadas pelo Presenter:
 *   - Produto selecionado, quantidade valida, estoque resultante >= 0
 */
package com.ufes.delivery.command;

import com.ufes.delivery.presenters.MovimentacaoEstoquePresenter;

public class TipoAjusteCommand extends TipoMovimentacaoCommand {

    public TipoAjusteCommand(MovimentacaoEstoquePresenter presenter) {
        super(presenter);
    }

    @Override
    public void confirmar() {
        String motivo = this.movimentacaoEstoquePresenter.getMotivoAjuste();

        // Cenario 2 - Rejeitar ajuste sem motivo
        if (motivo.isEmpty()) {
            this.movimentacaoEstoquePresenter.exibirErro(
                    "O motivo do ajuste é obrigatório para movimentação do tipo Ajuste de estoque.");
            return;
        }

        // Todas as validacoes foram atendidas: efetiva a movimentacao
        Integer quantidade = this.movimentacaoEstoquePresenter.getQuantidadeMovimentar();
        if (quantidade == null || quantidade == 0) {
            this.movimentacaoEstoquePresenter.exibirErro("Quantidade inválida para movimentação.");
            return;
        }

        // O presenter ja validou que o estoque resultante nao sera negativo.
        // O ajuste pode ser positivo (acrescimo) ou negativo (reducao).
        this.movimentacaoEstoquePresenter.efetivarMovimentacao(quantidade);
    }
}
