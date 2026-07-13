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
 * Estado responsável pela criação do pedido (US09 - Criar pedido, administrar
 * itens e aplicar cupom).
 * <p>
 * Neste estado o atendente pode:
 * <ul>
 *   <li>selecionar / cadastrar cliente e endereço de entrega;</li>
 *   <li>adicionar e remover itens da tabela;</li>
 *   <li>aplicar cupom de desconto;</li>
 *   <li>selecionar "Pagar" para avançar ao {@link ValidarPedidoState}.</li>
 * </ul>
 * As ações de edição são permitidas exclusivamente aqui; nos demais estados
 * elas devem ser rejeitadas para preservar a consistência do pedido.
 *
 * @author lucas
 */
public class CriarPedidoState extends PedidoState {

    public CriarPedidoState(PedidoPresenter presenter) {
        super(presenter);
    }

    @Override
    public void entrar() {
        IPedidoView view = presenter.getView();
        if (view == null) {
            return;
        }
        // Habilita todas as ações de edição permitidas na criação.
        view.getNovoClienteButton().setEnabled(true);
        view.getAplicarCupomButton().setEnabled(true);
        view.getPagarButton().setEnabled(true);
        view.getFecharButton().setEnabled(true);
        view.getTxtCpfCliente().setEnabled(true);
        view.getTxtCupomDesconto().setEnabled(true);
        view.getEnderecoComboBox().setEnabled(true);

        // Tabela de itens editável no estado de criação
        view.setTabelaItensEditable(true);

        // O botão Pagar aqui significa "ir para validação", não "confirmar
        // pagamento" - o rótulo continua o mesmo pois a View é fixa.
        presenter.atualizarTabela();
        presenter.atualizarValores();
    }

    @Override
    public void sair() {
        // Nada a limpar neste estado; os dados do pedido são preservados.
    }

    @Override
    public void novoCliente() {
        // Abre a tela de cadastro de cliente. A integração concreta depende do
        // Presenter principal da aplicação; o presenter do pedido apenas
        // solicita a abertura.
        presenter.abrirCadastroCliente();
    }

    @Override
    public void adicionarItem() {
        // Abre a busca de produtos para seleção de item. O presenter cuida de
        // obter o produto e a quantidade e adicioná-lo ao Pedido corrente.
        presenter.abrirBuscaProdutos();
    }

    @Override
    public void excluirItem(int linha) {
        if (linha < 0) {
            presenter.exibirMensagem("Selecione um item para excluir.",
                    "Excluir item", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Pedido pedido = presenter.getPedido();
        if (pedido == null || pedido.getItens().isEmpty()) {
            presenter.exibirMensagem("Não há item a ser excluído.",
                    "Excluir item", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        // Confirmação explícita quando já existem itens/valores (US09 - Cancelar).
        int opcao = JOptionPane.showConfirmDialog(
                presenter.getView().getJanelaPrincipal(),
                "Deseja remover o item selecionado do pedido?",
                "Excluir item", JOptionPane.YES_NO_OPTION);
        if (opcao != JOptionPane.YES_OPTION) {
            return;
        }
        presenter.removerItem(linha);
    }

    @Override
    public void aplicarCupom() {
        String codigo = presenter.getView().getTxtCupomDesconto().getText();
        if (codigo == null || codigo.trim().isEmpty()) {
            // Cupom vazio é permitido pela US09 - apenas não aplica desconto.
            return;
        }
        presenter.aplicarCupom(codigo.trim());
    }

    @Override
    public void pagar() {
        // US09 cenário 2 - Rejeitar pedido sem item.
        Pedido pedido = presenter.getPedido();
        if (pedido == null) {
            presenter.exibirMensagem(
                    "Informe o cliente antes de prosseguir para o pagamento.",
                    "Pedido", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (pedido.getItens().isEmpty()) {
            presenter.exibirMensagem(
                    "Pelo menos um item é obrigatório para prosseguir.",
                    "Pedido", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (presenter.getView().getEnderecoComboBox().getSelectedItem() == null) {
            presenter.exibirMensagem(
                    "Selecione o endereço de entrega antes de prosseguir.",
                    "Pedido", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Transição de estado: Criar -> Validar.
        presenter.setEstado(new ValidarPedidoState(presenter));
    }

    @Override
    public void fechar() {
        // Encerra a janela do pedido descartando as alterações não confirmadas.
        // A regra de confirmação explícita (US09 - Cancelar) é aplicada quando
        // já existem itens ou valores informados.
        Pedido pedido = presenter.getPedido();
        if (pedido != null && !pedido.getItens().isEmpty()) {
            int opcao = JOptionPane.showConfirmDialog(
                    presenter.getView().getJanelaPrincipal(),
                    "Existem alterações não confirmadas. Deseja descartá-las?",
                    "Fechar pedido", JOptionPane.YES_NO_OPTION);
            if (opcao != JOptionPane.YES_OPTION) {
                return;
            }
        }
        presenter.getView().getJanelaPrincipal().dispose();
    }

    @Override
    public void buscarCliente() {
        presenter.buscarClientePorCpf();
    }
}
