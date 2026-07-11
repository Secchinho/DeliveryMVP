/*
 * TipoMovimentacaoCommand.java
 *
 * Classe abstrata base do Command Pattern para movimentacao de estoque.
 * Conforme diagrama de classes:
 *   - Atributo protegido: movimentacaoEstoquePresenter (1..1)
 *   - Operacao abstrata: confirmar()
 *
 * O command encapsula a logica especifica de confirmacao conforme o tipo
 * de movimentacao (Entrada ou Ajuste), separando a responsabilidade do
 * presenter (MVP Passive View).
 *
 * Regra US08: o tipo "Saida" nao integra o dominio desta funcionalidade;
 * a baixa de estoque por venda ocorre exclusivamente apos aprovacao do
 * pagamento.
 */
package com.ufes.delivery.command;

import com.ufes.delivery.presenters.MovimentacaoEstoquePresenter;

public abstract class TipoMovimentacaoCommand {

    protected MovimentacaoEstoquePresenter movimentacaoEstoquePresenter;

    public TipoMovimentacaoCommand(MovimentacaoEstoquePresenter presenter) {
        this.movimentacaoEstoquePresenter = presenter;
    }

    /**
     * Confirma a movimentacao de estoque conforme as regras do tipo especifico.
     * Cada subclasse deve validar suas regras condicionais antes de efetivar.
     *
     * Regras comuns (validadas pelo Presenter antes de chamar o Command):
     *   - Produto selecionado
     *   - Quantidade inteira e diferente de zero
     *   - Estoque resultante nao negativo
     *
     * Regras especificas (validadas por cada Command):
     *   - TipoEntradaCommand: nota fiscal obrigatoria
     *   - TipoAjusteCommand: motivo do ajuste obrigatorio
     */
    public abstract void confirmar();
}
