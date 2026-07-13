/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufes.delivery.state;

import com.ufes.delivery.model.Pedido;
import com.ufes.delivery.presenters.PedidoPresenter;
import com.ufes.delivery.view.IPedidoView;
import javax.swing.JOptionPane;

/**
 * Estado responsável pela validação e processamento do pagamento (US10 -
 * Validar pedido, simular pagamento e atualizar estoque).
 * <p>
 * Neste estado o pedido é tratado como pronto para confirmação: os campos de
 * edição são bloqueados e o atendente só pode:
 * <ul>
 *   <li>selecionar "Pagar" para disparar a validação de estoque, a simulação
 *       do pagamento e, se aprovado, a baixa de estoque e a transição para
 *       "Aguardando entrega";</li>
 *   <li>selecionar "Fechar" para retornar ao {@link CriarPedidoState} e
 *       ajustar o pedido.</li>
 * </ul>
 * Ações de edição (adicionar / excluir item, aplicar cupom, novo cliente) são
 * rejeitadas para garantir que o pedido validado não seja alterado sem passar
 * novamente pela criação.
 *
 * @author lucas
 */
public class ValidarPedidoState extends PedidoState {

    public ValidarPedidoState(PedidoPresenter presenter) {
        super(presenter);
    }

    @Override
    public void entrar() {
        IPedidoView view = presenter.getView();
        if (view == null) {
            return;
        }
        // Bloqueia edição: o pedido está em revisão para pagamento.
        view.getNovoClienteButton().setEnabled(false);
        view.getAplicarCupomButton().setEnabled(false);
        view.getTxtCliente().setEnabled(false);
        view.getTxtCupomDesconto().setEnabled(false);
        view.getEnderecoComboBox().setEnabled(false);

        // Mantém Pagar e Fechar habilitados - únicas ações válidas aqui.
        view.getPagarButton().setEnabled(true);
        view.getFecharButton().setEnabled(true);

        // Recalcula e apresenta os valores finais (US10 cenário 1).
        presenter.atualizarTabela();
        presenter.atualizarValores();
    }

    @Override
    public void sair() {
        // Sem limpeza necessária.
    }

    @Override
    public void novoCliente() {
        presenter.exibirMensagem(
                "Não é possível cadastrar cliente na fase de validação. "
                + "Feche a validação para editar o pedido.",
                "Operação indisponível", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void adicionarItem() {
        presenter.exibirMensagem(
                "Não é possível adicionar itens na fase de validação. "
                + "Feche a validação para editar o pedido.",
                "Operação indisponível", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void excluirItem(int linha) {
        presenter.exibirMensagem(
                "Não é possível excluir itens na fase de validação. "
                + "Feche a validação para editar o pedido.",
                "Operação indisponível", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void aplicarCupom() {
        presenter.exibirMensagem(
                "Não é possível aplicar cupom na fase de validação. "
                + "Feche a validação para editar o pedido.",
                "Operação indisponível", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void pagar() {
        Pedido pedido = presenter.getPedido();
        if (pedido == null) {
            presenter.exibirMensagem(
                    "Não há pedido a ser validado.",
                    "Pagamento", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // US10 cenário 3 - Bloquear processamento por estoque insuficiente.
        // A disponibilidade deve ser consultada no instante da confirmação,
        // mesmo que o item tenha sido validado anteriormente.
        if (!presenter.validarDisponibilidadeEstoque()) {
            // A mensagem com o item e a quantidade disponível já foi exibida
            // pelo presenter. Nenhum estoque deve ser alterado.
            return;
        }
        // US10 cenário 4 - Aplicar baixa após resultado aprovado com estoque.
        // US11 - Simular resultado do pagamento.
        boolean aprovado = presenter.simularPagamento();
        if (!aprovado) {
            // Pagamento reprovado: estoque e situação do pedido permanecem
            // inalterados (US11 cenário 2). Permanece no estado de validação
            // para nova tentativa.
            presenter.exibirMensagem(
                    "Pagamento reprovado. O pedido permanece preservado para "
                    + "nova tentativa.",
                    "Pagamento", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Resultado aprovado: baixa o estoque em transação única e atualiza a
        // situação do pedido para "Aguardando entrega" (US10).
        presenter.confirmarPagamento();
        presenter.exibirMensagem(
                "Pagamento aprovado.\nPedido pronto para entrega.",
                "Pagamento", JOptionPane.INFORMATION_MESSAGE);
        // Encerra a tela após a confirmação com sucesso.
        presenter.getView().getJanelaPrincipal().dispose();
    }

    @Override
    public void fechar() {
        // Retorna ao estado de criação para permitir ajustes no pedido
        // (US11 cenário 2 - retorno à tela anterior com dados preservados).
        presenter.setEstado(new CriarPedidoState(presenter));
    }
}
