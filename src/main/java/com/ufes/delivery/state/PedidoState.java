/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufes.delivery.state;

import com.ufes.delivery.presenters.PedidoPresenter;
import java.util.Objects;

/**
 * Classe abstrata base do padrão State para o fluxo de pedido.
 * <p>
 * Cada estado concreto (CriarPedidoState, ValidarPedidoState) encapsula o
 * comportamento válido para a sua fase do fluxo (US09 - criação; US10 -
 * validação/pagamento). O {@link PedidoPresenter} apenas delega as ações do
 * usuário ao estado corrente, e o estado decide se executa, rejeita ou
 * transita para outro estado.
 * <p>
 * Mantém referência protegida ao presenter para que os estados concretos
 * possam acessar a View, o Pedido corrente e o repositório, além de disparar
 * transições de estado.
 *
 * @author lucas
 */
public abstract class PedidoState {

    /**
     * Referência ao presenter dono do estado. Visibilidade protected para que
     * os estados concretos possam acessar View, Pedido e Repository e disparar
     * transições (alinhado ao atributo {@code pedidoPresenter} do diagrama de
     * classes, análogo ao {@code pagamentoPresenter} de IPagamentoState).
     */
    protected PedidoPresenter presenter;

    public PedidoState(PedidoPresenter presenter) {
        this.presenter = Objects.requireNonNull(presenter,
                "Insira uma Presenter válida");
    }

    public PedidoPresenter getPresenter() {
        return presenter;
    }

    // ------------------------------------------------------------------
    // Ciclo de vida do estado
    // ------------------------------------------------------------------

    /**
     * Executado ao entrar no estado. Deve configurar a View (habilitar /
     * desabilitar botões, atualizar rótulos, focar componentes) de acordo com
     * as ações permitidas na fase corrente.
     */
    public abstract void entrar();

    /**
     * Executado ao sair do estado. Permite liberar recursos, limpar seleções
     * temporárias ou desfazer configurações visuais aplicadas em
     * {@link #entrar()}.
     */
    public abstract void sair();

    // ------------------------------------------------------------------
    // Ações de UI delegadas pelo presenter
    // ------------------------------------------------------------------

    /**
     * Ação "Novo cliente". No estado de criação abre a tela de cadastro; nos
     * demais estados deve ser rejeitada.
     */
    public abstract void novoCliente();

    /**
     * Ação "Buscar cliente". No estado de criação busca o cliente por CPF
     * no repositório e o vincula ao pedido; nos demais estados deve ser
     * rejeitada.
     */
    public abstract void buscarCliente();

    /**
     * Ação "Adicionar item" (geralmente via tela de busca de produtos). No
     * estado de criação adiciona o item ao pedido; nos demais estados deve ser
     * rejeitada.
     */
    public abstract void adicionarItem();

    /**
     * Ação "Excluir item" disparada pelo menu de contexto sobre uma linha da
     * tabela de itens (US09 cenário 4). No estado de criação remove o item e
     * recalcula os valores; nos demais estados deve ser rejeitada.
     *
     * @param linha índice da linha selecionada na tabela de itens
     */
    public abstract void excluirItem(int linha);

    /**
     * Ação "Aplicar cupom". No estado de criação valida o cupom informado e,
     * se válido, aplica-o ao pedido atualizando os totais (US09 cenário 5);
     * nos demais estados deve ser rejeitada.
     */
    public abstract void aplicarCupom();

    /**
     * Ação "Pagar". No estado de criação valida as condições mínimas para
     * avançar (cliente, endereço e ao menos um item - US09 cenário 2) e
     * transita para {@link ValidarPedidoState}; no estado de validação
     * processa o pagamento simulado, valida estoque, dá baixa e atualiza a
     * situação do pedido (US10).
     */
    public abstract void pagar();

    /**
     * Ação "Fechar". No estado de criação encerra a tela; no estado de
     * validação retorna ao estado de criação para permitir ajustes.
     */
    public abstract void fechar();
}
