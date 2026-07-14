/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufes.delivery.presenters;

import br.ufes.logauditoria.ILogger;
import com.ufes.delivery.command.SalvarClienteCommand;
import com.ufes.delivery.command.SalvarProdutoCommand;
import com.ufes.delivery.desconto.pedido.AplicadorCupomPedidoService;
import com.ufes.delivery.model.Cliente;
import com.ufes.delivery.model.Pedido;
import com.ufes.delivery.repository.IClienteRepository;
import com.ufes.delivery.repository.IPedidoRepository;
import com.ufes.delivery.repository.IProdutoRepository;
import com.ufes.delivery.state.CriarPedidoState;
import com.ufes.delivery.state.ValidarPedidoState;
import com.ufes.delivery.view.BuscaClienteView;
import com.ufes.delivery.view.BuscarProdutoView;
import com.ufes.delivery.view.ClienteView;
import com.ufes.delivery.view.IBuscarClienteView;
import com.ufes.delivery.view.IBuscarProdutoView;
import com.ufes.delivery.view.IClienteView;
import com.ufes.delivery.view.IMovimentacaoEstoqueView;
import com.ufes.delivery.view.IPainelOperacionalView;
import com.ufes.delivery.view.IPedidoView;
import com.ufes.delivery.view.IProdutoView;
import com.ufes.delivery.view.MovimentacaoEstoqueView;
import com.ufes.delivery.view.PedidoView;
import com.ufes.delivery.view.ProdutoView;
import com.ufes.util.UsuarioLogadoService;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Presenter responsável pelo painel operacional (US04).
 * <p>
 * Atua como a "porta de entrada" da aplicação após o login: exibe a data de
 * operação, as métricas por estado do pedido, a lista de pedidos do dia, a
 * barra de status da sessão e concentra a abertura das demais telas a partir
 * do menu Operação (Novo pedido, Buscar produtos, Novo produto, Movimentação
 * de estoque, Novo cliente e Buscar clientes), conforme US04, US05, US06,
 * US07, US08 e US09.
 *
 * @author lucas
 */
public class PainelOperacionalPresenter {

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FORMATO_DATA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final int TIPO_ADMINISTRADOR = 1;

    private final IPainelOperacionalView view;
    private final IPedidoRepository pedidoRepository;
    private final IClienteRepository clienteRepository;
    private final IProdutoRepository produtoRepository;
    private final ILogger logger;
    private final AplicadorCupomPedidoService aplicadorCupomService;
    private final PagamentoService pagamentoService;

    /**
     * Instante em que a sessão foi iniciada, usado para compor a barra de
     * status (US04 - Cenário 3). Idealmente esse dado seria mantido pelo
     * próprio {@link UsuarioLogadoService}; na ausência desse atributo na
     * sessão, o painel registra o instante em que é efetivamente exibido
     * (ver {@link #iniciar()}), chamado pelo LoginPresenter imediatamente
     * após uma autenticação bem sucedida - o que corresponde, na prática,
     * ao horário do login.
     */
    private LocalDateTime dataHoraLogin;

    /**
     * Cache dos pedidos atualmente exibidos na tabela, na mesma ordem das
     * linhas, para permitir mapear o índice da linha clicada em "Visualizar"
     * (US04 - Cenário 4) de volta ao {@link Pedido} correspondente.
     */
    private List<Pedido> pedidosExibidos = new ArrayList<>();

    public PainelOperacionalPresenter(IPainelOperacionalView view,
            IPedidoRepository pedidoRepository,
            IClienteRepository clienteRepository,
            IProdutoRepository produtoRepository,
            ILogger logger,
            AplicadorCupomPedidoService aplicadorCupomService,
            PagamentoService pagamentoService) {
        this.view = Objects.requireNonNull(view, "Insira uma tela");
        this.pedidoRepository = Objects.requireNonNull(pedidoRepository, "Insira um PedidoRepository");
        this.clienteRepository = Objects.requireNonNull(clienteRepository, "Insira um ClienteRepository");
        this.produtoRepository = Objects.requireNonNull(produtoRepository, "Insira um ProdutoRepository");
        this.logger = Objects.requireNonNull(logger, "Insira um Logger");
        this.aplicadorCupomService = Objects.requireNonNull(aplicadorCupomService,
                "Insira um AplicadorCupomPedidoService");
        this.pagamentoService = Objects.requireNonNull(pagamentoService, "Insira um PagamentoService");

        this.configurarEventos();
    }

    /**
     * Exibe o painel operacional. Deve ser chamado logo após uma
     * autenticação bem sucedida (ver {@code LoginPresenter}), momento em que
     * o usuário já está registrado no {@link UsuarioLogadoService}: é nesse
     * instante que a sessão é considerada iniciada para fins da barra de
     * status (US04 - Cenário 3).
     */
    public void iniciar() {
        this.dataHoraLogin = LocalDateTime.now();
        this.carregarPainel();
        this.view.getJanelaPrincipal().setVisible(true);
    }

    // ------------------------------------------------------------------
    // Configuração de eventos do menu Operação e da tabela de pedidos
    // ------------------------------------------------------------------

    private void configurarEventos() {
        this.view.getMenuNovoPedido().addActionListener(v -> this.abrirNovoPedido());
        this.view.getMenuBuscarProdutos().addActionListener(v -> this.abrirBuscarProdutos());
        this.view.getMenuNovoProduto().addActionListener(v -> this.abrirNovoProduto());
        this.view.getMenuMovimentacaoEstoque().addActionListener(v -> this.abrirMovimentacaoEstoque());
        this.view.getMenuNovoCliente().addActionListener(v -> this.abrirNovoCliente());
        this.view.getMenuBuscarClientes().addActionListener(v -> this.abrirBuscarClientes());

        this.view.setAcaoVisualizarPedidoListener(this::visualizarPedido);
    }

    // ------------------------------------------------------------------
    // Carregamento do painel (US04)
    // ------------------------------------------------------------------

    /**
     * Recalcula a data de operação, as métricas e a lista de pedidos, além
     * de atualizar a barra de status com os dados da sessão corrente.
     */
    private void carregarPainel() {
        LocalDate dataOperacao = LocalDate.now();

        this.view.exibirDataOperacao(dataOperacao.format(FORMATO_DATA));

        List<Pedido> todosOsPedidos = this.pedidoRepository.listarPedidos();
        List<Pedido> pedidosDoDia = new ArrayList<>();
        for (Pedido pedido : todosOsPedidos) {
            if (pedido.getData() != null && pedido.getData().toLocalDate().isEqual(dataOperacao)) {
                pedidosDoDia.add(pedido);
            }
        }

        this.pedidosExibidos = pedidosDoDia;

        int novos = 0;
        int aguardandoPagamento = 0;
        int emPreparo = 0;
        int aguardandoEntrega = 0;
        int emTransito = 0;
        int entreguesHoje = 0;

        for (Pedido pedido : pedidosDoDia) {
            switch (pedido.getEstado()) {
                case "Novo":
                    novos++;
                    break;
                case "Aguardando pagamento":
                    aguardandoPagamento++;
                    break;
                case "Em preparo":
                    emPreparo++;
                    break;
                case "Aguardando entrega":
                    aguardandoEntrega++;
                    break;
                case "Em trânsito":
                    emTransito++;
                    break;
                case "Entregue":
                    entreguesHoje++;
                    break;
                default:
                    break;
            }
        }

        this.view.exibirIndicadores(pedidosDoDia.size(), novos, aguardandoPagamento,
                emPreparo, aguardandoEntrega, emTransito, entreguesHoje);

        this.view.atualizarListaPedidos(this.montarLinhasTabela(pedidosDoDia));

        String tipoUsuario = UsuarioLogadoService.getInstance().getTipo() == TIPO_ADMINISTRADOR
                ? "Administrador" : "Atendente";
        this.view.exibirSessaoUsuario(
                UsuarioLogadoService.getInstance().getUserName(),
                this.dataHoraLogin.format(FORMATO_DATA_HORA),
                tipoUsuario);
    }

    private Object[][] montarLinhasTabela(List<Pedido> pedidos) {
        DecimalFormat formatoMoeda = new DecimalFormat("#,##0.00");
        Object[][] linhas = new Object[pedidos.size()][6];
        for (int i = 0; i < pedidos.size(); i++) {
            Pedido pedido = pedidos.get(i);
            linhas[i][0] = pedido.getCodigoPedido();
            linhas[i][1] = pedido.getCliente() != null ? pedido.getCliente().getNome() : "";
            linhas[i][2] = pedido.getData() != null ? pedido.getData().toLocalDate().format(FORMATO_DATA) : "";
            // O modelo de Pedido ainda não mantém um atributo de data de
            // conclusão; enquanto ele não existir a coluna permanece vazia,
            // inclusive para pedidos entregues (ver observação ao final da
            // implementação sobre alterações sugeridas em outras classes).
            linhas[i][3] = "-";
            linhas[i][4] = pedido.getEstado();
            linhas[i][5] = "R$ " + formatoMoeda.format(pedido.calcularValorTotal());
        }
        return linhas;
    }

    // ------------------------------------------------------------------
    // Ações do menu Operação
    // ------------------------------------------------------------------

    private void abrirNovoPedido() {
        IPedidoView pedidoView = new PedidoView();
        PedidoPresenter pedidoPresenter = new PedidoPresenter(pedidoView,
                this.pedidoRepository, this.clienteRepository, this.produtoRepository,
                this.logger, this.aplicadorCupomService, this.pagamentoService);

        pedidoPresenter.setEstado(new CriarPedidoState(pedidoPresenter));

        this.atualizarPainelAoFechar(pedidoView.getJanelaPrincipal());
        pedidoPresenter.iniciar();
    }

    private void abrirBuscarProdutos() {
        IProdutoView produtoView = new ProdutoView();
        IBuscarProdutoView buscarProdutoView = new BuscarProdutoView();
        BuscarProdutoPresenter buscarProdutoPresenter = new BuscarProdutoPresenter(
                buscarProdutoView, this.produtoRepository, produtoView);

        this.atualizarPainelAoFechar(buscarProdutoView.getJanelaPrincipal());
        buscarProdutoPresenter.iniciar();
    }

    private void abrirNovoProduto() {
        IProdutoView produtoView = new ProdutoView();
        ProdutoPresenter produtoPresenter = new ProdutoPresenter(produtoView, this.produtoRepository);
        produtoPresenter.setCommand(new SalvarProdutoCommand(produtoPresenter));

        this.atualizarPainelAoFechar(produtoView.getJanelaPrincipal());
        produtoPresenter.iniciar();
    }

    private void abrirMovimentacaoEstoque() {
        // US08 - Acesso: a movimentação de estoque deve ser executada
        // somente por Administrador.
        if (UsuarioLogadoService.getInstance().getTipo() != TIPO_ADMINISTRADOR) {
            this.view.exibirMensagem(
                    "Funcionalidade restrita ao perfil Administrador.",
                    "Acesso negado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        IMovimentacaoEstoqueView movimentacaoEstoqueView = new MovimentacaoEstoqueView();
        MovimentacaoEstoquePresenter movimentacaoEstoquePresenter
                = new MovimentacaoEstoquePresenter(movimentacaoEstoqueView, this.produtoRepository);

        this.atualizarPainelAoFechar(movimentacaoEstoqueView.getJanelaPrincipal());
        movimentacaoEstoquePresenter.iniciar();
    }

    private void abrirNovoCliente() {
        IClienteView clienteView = new ClienteView();
        ClientePresenter clientePresenter = new ClientePresenter(clienteView, this.clienteRepository);
        clientePresenter.setCommand(new SalvarClienteCommand(clientePresenter));

        this.atualizarPainelAoFechar(clienteView.getJanelaPrincipal());
        clientePresenter.iniciar();
    }

    private void abrirBuscarClientes() {
        IBuscarClienteView buscarClienteView = new BuscaClienteView();
        BuscarClientePresenter buscarClientePresenter
                = new BuscarClientePresenter(buscarClienteView, this.clienteRepository, new ClienteView());

        this.atualizarPainelAoFechar(buscarClienteView.getJanelaPrincipal());
        buscarClientePresenter.iniciar();
    }

    /**
     * Abre o pedido correspondente à linha clicada em "Visualizar" na lista
     * de pedidos (US04 - Cenário 4).
     */
    private void visualizarPedido(int linha) {
        if (linha < 0 || linha >= this.pedidosExibidos.size()) {
            return;
        }
        Pedido pedido = this.pedidosExibidos.get(linha);

        IPedidoView pedidoView = new PedidoView();
        PedidoPresenter pedidoPresenter = new PedidoPresenter(pedidoView,
                this.pedidoRepository, this.clienteRepository, this.produtoRepository,
                this.logger, this.aplicadorCupomService, this.pagamentoService);

        pedidoPresenter.setPedido(pedido);

        Cliente cliente = pedido.getCliente();
        if (cliente != null) {
            pedidoView.getLblNomeCliente().setText(cliente.getNome());
            pedidoView.getTxtCpfCliente().setText(cliente.getCPF());
        }

        // Pedidos ainda em elaboração ("Novo") permanecem editáveis; os
        // demais são apresentados em modo de revisão (somente leitura),
        // já que já passaram pela etapa de validação/pagamento.
        if ("Novo".equals(pedido.getEstado())) {
            pedidoPresenter.setEstado(new CriarPedidoState(pedidoPresenter));
        } else {
            pedidoPresenter.setEstado(new ValidarPedidoState(pedidoPresenter));
            pedidoView.getPagarButton().setEnabled(false);
        }

        this.atualizarPainelAoFechar(pedidoView.getJanelaPrincipal());
        pedidoPresenter.iniciar();
    }

    /**
     * Registra um listener para que, ao fechar a janela filha informada, o
     * painel operacional seja recarregado - refletindo eventuais pedidos,
     * clientes, produtos ou estoques criados/alterados na tela filha.
     */
    private void atualizarPainelAoFechar(JFrame janela) {
        janela.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                carregarPainel();
            }
        });
    }
}