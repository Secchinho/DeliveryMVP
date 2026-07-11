/*
 * TipoEntradaCommand.java
 *
 * Command concreto para movimentacao de estoque do tipo Entrada.
 * Herda de TipoMovimentacaoCommand e implementa confirmar().
 *
 * Regra especifica (US08, Cenario 3):
 *   - O tipo Entrada exige numero de nota fiscal obrigatorio.
 *   - Se a nota fiscal estiver ausente, a movimentacao nao deve ser
 *     persistida e a interface deve informar que o numero da nota fiscal
 *     de entrada e obrigatorio.
 *
 * Regras comuns ja validadas pelo Presenter:
 *   - Produto selecionado, quantidade valida, estoque resultante >= 0
 */
package com.ufes.delivery.command;

import com.ufes.delivery.presenters.MovimentacaoEstoquePresenter;

public class TipoEntradaCommand extends TipoMovimentacaoCommand {

    public TipoEntradaCommand(MovimentacaoEstoquePresenter presenter) {
        super(presenter);
    }

    @Override
    public void confirmar() {
        String notaFiscal = this.movimentacaoEstoquePresenter.getNotaFiscal();

        // Cenario 3 - Exigir nota fiscal na entrada
        if (notaFiscal.isEmpty()) {
            this.movimentacaoEstoquePresenter.exibirErro(
                    "O número da nota fiscal de entrada é obrigatório para movimentação do tipo Entrada.");
            return;
        }

        // Todas as validacoes foram atendidas: efetiva a movimentacao
        Integer quantidade = this.movimentacaoEstoquePresenter.getQuantidadeMovimentar();
        if (quantidade == null || quantidade == 0) {
            this.movimentacaoEstoquePresenter.exibirErro("Quantidade inválida para movimentação.");
            return;
        }

        // A quantidade de entrada e sempre positiva; o presenter ja validou
        // que o estoque resultante nao sera negativo.
        this.movimentacaoEstoquePresenter.efetivarMovimentacao(quantidade);
    }
}
