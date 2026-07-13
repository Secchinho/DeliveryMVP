/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufes.delivery.presenters;

import br.ufes.logauditoria.ILogger;
import com.ufes.delivery.desconto.pedido.AplicadorCupomPedidoService;
import com.ufes.delivery.model.Pedido;
import com.ufes.delivery.repository.IPedidoRepository;
import com.ufes.delivery.state.CriarPedidoState;
import com.ufes.delivery.state.PedidoState;
import com.ufes.delivery.state.ValidarPedidoState;
import com.ufes.delivery.view.IPedidoView;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * Presenter da tela de pedido (US09 e US10).
 * <p>
 * Atua como <em>contexto</em> do padrão State: mantém a referência ao
 * {@link PedidoState} corrente e delega a ele todas as ações de UI disparadas
 * pela View. O presenter não decide se uma ação é permitida - apenas
 * encaminha a requisição ao estado, que executa, rejeita ou transita para
 * outro estado.
 * <p>
 * Responsabilidades:
 * <ul>
 *   <li>configurar os listeners de eventos da View e delegá-los ao estado;</li>
 *   <li>expor ao estado os objetos de domínio (View, Pedido, Repository);</li>
 *   <li>fornecer operações utilitárias (atualizar tabela, atualizar valores,
 *       validar estoque, simular pagamento, etc.) usadas pelos estados;</li>
 *   <li>permitir a troca de estado via {@link #setEstado(PedidoState)}.</li>
 * </ul>
 *
 * @author lucas
 */
public class PedidoPresenter {

    private final IPedidoView view;
    private final IPedidoRepository repository;
    private final ILogger logger;
    private final AplicadorCupomPedidoService aplicadorCupomService;
    private final List<Pedido> pedidos;

    /**
     * Pedido corrente em elaboração / validação. Pode ser {@code null} quando
     * a tela foi aberta em modo inclusão e o cliente ainda não foi informado.
     */
    private Pedido pedido;

    /**
     * Estado corrente do fluxo de pedido (Criar / Validar).
     */
    private PedidoState estado;

    private final DecimalFormat formatoMoeda = new DecimalFormat("#,##0.00");

    public PedidoPresenter(IPedidoView view, IPedidoRepository repository,
            ILogger logger, AplicadorCupomPedidoService aplicadorCupomService) {
        this.view = Objects.requireNonNull(view, "View não pode ser nula");
        this.repository = Objects.requireNonNull(repository,
                "Repository não pode ser nulo");
        this.logger = Objects.requireNonNull(logger, "Logger não pode ser nulo");
        this.aplicadorCupomService = Objects.requireNonNull(aplicadorCupomService,
                "Serviço de aplicação de cupom não pode ser nulo");
        this.pedidos = repository.listarPedidos();

        // Estado inicial: criação do pedido (US09).
        this.estado = new ValidarPedidoState(this);

        this.configurarEventos();
    }

    public ILogger getLogger() {
        return logger;
    }

    public AplicadorCupomPedidoService getAplicadorCupomService() {
        return aplicadorCupomService;
    }

    // ------------------------------------------------------------------
    // Configuração de eventos - delega ao estado corrente
    // ------------------------------------------------------------------

    /**
     * Configura os listeners de UI da tela de pedido. Cada ação é delegada ao
     * {@link PedidoState} corrente, que decide como tratá-la.
     * <p>
     * Visibilidade {@code public} conforme o diagrama de classes.
     */
    public void configurarEventos() {
        // Botão Novo Cliente -> estado.novoCliente()
        this.view.getNovoClienteButton()
                .addActionListener(e -> this.estado.novoCliente());

        // Botão Aplicar Cupom -> estado.aplicarCupom()
        this.view.getAplicarCupomButton()
                .addActionListener(e -> this.estado.aplicarCupom());

        // Botão Pagar -> estado.pagar()
        this.view.getPagarButton()
                .addActionListener(e -> this.estado.pagar());

        // Botão Fechar -> estado.fechar()
        this.view.getFecharButton()
                .addActionListener(e -> this.estado.fechar());

        // Tabela de itens: duplo clique para adicionar e menu de contexto
        // (botão direito) para excluir - US09 cenário 4.
        JTable tabela = this.view.getTabelaItens();
        if (tabela != null) {
            tabela.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        estado.adicionarItem();
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    mostrarMenuContexto(e);
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    mostrarMenuContexto(e);
                }

                private void mostrarMenuContexto(MouseEvent e) {
                    if (!e.isPopupTrigger()) {
                        return;
                    }
                    int linha = tabela.rowAtPoint(e.getPoint());
                    if (linha < 0) {
                        return;
                    }
                    tabela.setRowSelectionInterval(linha, linha);
                    estado.excluirItem(linha);
                }
            });
        }
    }

    // ------------------------------------------------------------------
    // Inicialização
    // ------------------------------------------------------------------

    /**
     * Exibe a janela do pedido e dispara a entrada do estado inicial.
     */
    public void iniciar() {
        this.view.getJanelaPrincipal().setVisible(true);
        this.estado.entrar();
    }

    // ------------------------------------------------------------------
    // Transição de estado
    // ------------------------------------------------------------------

    /**
     * Troca o estado corrente. Executa {@link PedidoState#sair()} do estado
     * atual e {@link PedidoState#entrar()} do novo estado.
     *
     * @param novoEstado próximo estado do fluxo de pedido
     */
    public void setEstado(PedidoState novoEstado) {
        if (this.estado != null) {
            this.estado.sair();
        }
        this.estado = Objects.requireNonNull(novoEstado,
                "Novo estado não pode ser nulo");
        this.estado.entrar();
    }

    public PedidoState getEstado() {
        return estado;
    }

    // ------------------------------------------------------------------
    // Getters expostos aos estados concretos
    // ------------------------------------------------------------------

    public IPedidoView getView() {
        return view;
    }

    public IPedidoRepository getRepository() {
        return repository;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    // ------------------------------------------------------------------
    // Operações utilitárias usadas pelos estados
    // ------------------------------------------------------------------

    /**
     * Atualiza a tabela de itens a partir do pedido corrente.
     */
    public void atualizarTabela() {
        JTable tabela = view.getTabelaItens();
        if (tabela == null) {
            return;
        }
        DefaultTableModel modelo = (DefaultTableModel) tabela.getModel();
        modelo.setRowCount(0);
        if (pedido == null) {
            return;
        }
        for (var item : pedido.getItens()) {
            modelo.addRow(new Object[]{
                item.getProduto().getNome(),
                String.format("R$ %s", formatoMoeda.format(item.getValorUnitario())),
                item.getQuantidade(),
                String.format("R$ %s", formatoMoeda.format(item.valorTotal()))
            });
        }
    }

    /**
     * Recalcula e atualiza os rótulos de resumo financeiro com base no pedido
     * corrente. Os valores são sempre apresentados em campos não editáveis
     * (US09 - totais calculados).
     */
    public void atualizarValores() {
        if (pedido == null) {
            view.getLblTotalDescontosValor().setText("R$ 0,00");
            view.getLblDescontoTaxaEntregaValor().setText("R$ 0,00");
            view.getLblTaxaEntregaFinalValor().setText("R$ 0,00");
            view.getLblTotalPedidoValor().setText("R$ 0,00");
            return;
        }
        double totalDescontos = pedido.getTotalDescontosTaxaEntrega();
        double taxaEntregaFinal = pedido.getTaxaEntregaComDesconto();
        double totalPedido = pedido.calcularValorTotal();

        view.getLblTotalDescontosValor().setText(
                String.format("R$ %s", formatoMoeda.format(totalDescontos)));
        view.getLblDescontoTaxaEntregaValor().setText(
                String.format("R$ %s", formatoMoeda.format(totalDescontos)));
        view.getLblTaxaEntregaFinalValor().setText(
                String.format("R$ %s", formatoMoeda.format(taxaEntregaFinal)));
        view.getLblTotalPedidoValor().setText(
                String.format("R$ %s", formatoMoeda.format(totalPedido)));
    }

    /**
     * Exibe uma mensagem modal na janela do pedido.
     *
     * @param mensagem texto a exibir
     * @param titulo   título da janela
     * @param tipo     um dos {@code JOptionPane.*_MESSAGE}
     */
    public void exibirMensagem(String mensagem, String titulo, int tipo) {
        JOptionPane.showMessageDialog(view.getJanelaPrincipal(),
                mensagem, titulo, tipo);
    }

    /**
     * Abre a tela de cadastro de cliente. Implementação concreta depende do
     * presenter coordenador da aplicação; aqui apenas expõe o ponto de
     * extensão para o estado.
     */
    public void abrirCadastroCliente() {
        // Hook - integrar com o coordenador de telas da aplicação.
        exibirMensagem("Abra a tela de cadastro de cliente a partir do menu.",
                "Novo cliente", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Abre a tela de busca de produtos para seleção de item a adicionar.
     * Implementação concreta depende do coordenador de telas da aplicação.
     */
    public void abrirBuscaProdutos() {
        // Hook - integrar com o coordenador de telas da aplicação.
        exibirMensagem("Abra a busca de produtos a partir do menu Operação.",
                "Adicionar item", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Remove o item da linha informada do pedido corrente e recalcula os
     * valores (US09 cenário 4).
     *
     * @param linha índice da linha na tabela (equivale ao índice do item na
     *              lista do pedido)
     */
    public void removerItem(int linha) {
        if (pedido == null || linha < 0
                || linha >= pedido.getItens().size()) {
            return;
        }
        // Remove da lista interna do pedido. Como getItens() é não
        // modificável, é preciso gerenciar a remoção pelo próprio Pedido.
        pedido.removerItem(pedido.getItens().get(linha));
        atualizarTabela();
        atualizarValores();
    }

    /**
     * Aplica o cupom informado ao pedido corrente. Cupom inválido não aplica
     * desconto e exibe mensagem (US09 cenário 5).
     *
     * @param codigo código do cupom de desconto
     */
    public void aplicarCupom(String codigo) {
        if (pedido == null) {
            exibirMensagem("Informe o cliente antes de aplicar o cupom.",
                    "Cupom de desconto", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            aplicadorCupomService.aplicarCupom(pedido, codigo,
                    java.time.LocalDateTime.now());
            atualizarValores();
            exibirMensagem("Cupom aplicado com sucesso.",
                    "Cupom de desconto", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            exibirMensagem(
                    "Cupom inválido para o pedido: " + ex.getMessage(),
                    "Cupom de desconto", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Valida a disponibilidade de estoque de todos os itens no instante da
     * confirmação do pagamento (US10 cenário 3).
     *
     * @return {@code true} se todos os itens estiverem disponíveis;
     *         {@code false} caso contrário (neste caso a mensagem com o item e
     *         a quantidade disponível já terá sido exibida)
     */
    public boolean validarDisponibilidadeEstoque() {
        if (pedido == null) {
            return false;
        }
        for (var item : pedido.getItens()) {
            int disponivel = item.getProduto().getQuantidadeDisponivel();
            if (item.getQuantidade() > disponivel) {
                exibirMensagem(
                        "Estoque insuficiente para o item \""
                        + item.getProduto().getNome()
                        + "\". Quantidade disponível: " + disponivel + ".",
                        "Validação de estoque", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }
        return true;
    }

    /**
     * Simula o resultado do pagamento (US11). A probabilidade é de 50% para
     * aprovação e 50% para reprovação no MVP.
     *
     * @return {@code true} se aprovado; {@code false} se reprovado
     */
    public boolean simularPagamento() {
        return Math.random() < 0.5;
    }

    /**
     * Confirma o pagamento: baixa o estoque em transação única (US10 cenário
     * 4), atualiza a situação do pedido para "Aguardando entrega" e persiste
     * o pedido no repositório.
     */
    public void confirmarPagamento() {
        if (pedido == null) {
            return;
        }
        // Baixa de estoque em transação única - nenhum item pode ser
        // parcialmente atualizado se outro falhar.
        for (var item : pedido.getItens()) {
            item.getProduto().baixarEstoque(item.getQuantidade());
        }
        // Situação do pedido após resultado aprovado (US10).
        pedido.setEstado("Aguardando entrega");
        // Persiste o pedido confirmado.
        repository.adicionar(pedido);
    }
}