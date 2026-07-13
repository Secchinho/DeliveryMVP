/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufes.delivery.presenters;

import br.ufes.logauditoria.ILogger;
import com.ufes.delivery.command.SalvarClienteCommand;
import com.ufes.delivery.command.PagamentoCommand;
import com.ufes.delivery.desconto.pedido.AplicadorCupomPedidoService;
import com.ufes.delivery.model.Cliente;
import com.ufes.delivery.model.Endereco;
import com.ufes.delivery.model.Item;
import com.ufes.delivery.model.Pedido;
import com.ufes.delivery.model.Produto;
import com.ufes.delivery.repository.ClienteRepositorySQLite;
import com.ufes.delivery.repository.IClienteRepository;
import com.ufes.delivery.repository.IPedidoRepository;
import com.ufes.delivery.repository.IProdutoRepository;
import com.ufes.delivery.state.CriarPedidoState;
import com.ufes.delivery.state.PedidoState;
import com.ufes.delivery.state.ValidarPedidoState;
import com.ufes.delivery.view.ClienteView;
import com.ufes.delivery.view.IClienteView;
import com.ufes.delivery.view.IPedidoView;
import com.ufes.delivery.view.PagamentoView;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * Presenter da tela de pedido (US09, US10 e US11).
 * <p>
 * Atua como <em>contexto</em> do padrão State: mantém a referência ao
 * {@link PedidoState} corrente e delega a ele todas as ações de UI disparadas
 * pela View. O presenter não decide se uma ação é permitida - apenas
 * encaminha a requisição ao estado, que executa, rejeita ou transita para
 * outro estado.
 *
 * @author lucas
 */
public class PedidoPresenter {

    private final IPedidoView view;
    private final IPedidoRepository repository;
    private final IClienteRepository clienteRepository;
    private final IProdutoRepository produtoRepository;
    private final ILogger logger;
    private final AplicadorCupomPedidoService aplicadorCupomService;
    private final PagamentoService pagamentoService;
    private final List<Pedido> pedidos;

    private Pedido pedido;
    private PedidoState estado;

    /**
     * Resultado da última tentativa simulada de pagamento (US11). Fica
     * disponível após {@link #simularPagamento()} e é consumido por
     * {@link #abrirTelaPagamento()} para popular a tela de resultado.
     */
    private ResultadoPagamento resultadoPagamento;

    private final DecimalFormat formatoMoeda = new DecimalFormat("#,##0.00");

    public PedidoPresenter(IPedidoView view, IPedidoRepository repository,
            IClienteRepository clienteRepository, IProdutoRepository produtoRepository,
            ILogger logger, AplicadorCupomPedidoService aplicadorCupomService) {
        this(view, repository, clienteRepository, produtoRepository, logger,
                aplicadorCupomService, new PagamentoService());
    }

    /**
     * Permite injetar um {@link PagamentoService} próprio - em especial uma
     * instância configurada com {@link ISorteioPagamento} determinístico para
     * testes automatizados, conforme exige o DoD da US11.
     */
    public PedidoPresenter(IPedidoView view, IPedidoRepository repository,
            IClienteRepository clienteRepository, IProdutoRepository produtoRepository,
            ILogger logger, AplicadorCupomPedidoService aplicadorCupomService,
            PagamentoService pagamentoService) {
        this.view = Objects.requireNonNull(view, "View não pode ser nula");
        this.repository = Objects.requireNonNull(repository,
                "Repository não pode ser nulo");
        this.clienteRepository = Objects.requireNonNull(clienteRepository,
                "Repository de clientes não pode ser nulo");
        this.produtoRepository = Objects.requireNonNull(produtoRepository,
                "Repository de produtos não pode ser nulo");
        this.logger = Objects.requireNonNull(logger, "Logger não pode ser nulo");
        this.aplicadorCupomService = Objects.requireNonNull(aplicadorCupomService,
                "Serviço de aplicação de cupom não pode ser nulo");
        this.pagamentoService = Objects.requireNonNull(pagamentoService,
                "Serviço de pagamento não pode ser nulo");
        this.pedidos = repository.listarPedidos();

        // Estado inicial: criação do pedido (US09).
        this.estado = new CriarPedidoState(this);

        this.configurarEventos();
    }

    public ILogger getLogger() {
        return logger;
    }

    public AplicadorCupomPedidoService getAplicadorCupomService() {
        return aplicadorCupomService;
    }

    public IProdutoRepository getProdutoRepository() {
        return produtoRepository;
    }

    public ResultadoPagamento getResultadoPagamento() {
        return resultadoPagamento;
    }

    // ------------------------------------------------------------------
    // Configuração de eventos - delega ao estado corrente
    // ------------------------------------------------------------------

    public void configurarEventos() {
        this.view.getNovoClienteButton()
                .addActionListener(e -> this.estado.novoCliente());

        this.view.getBuscarClienteButton()
                .addActionListener(e -> this.estado.buscarCliente());

        this.view.getAplicarCupomButton()
                .addActionListener(e -> this.estado.aplicarCupom());

        if (this.view.getAdicionarItemButton() != null) {
            this.view.getAdicionarItemButton()
                    .addActionListener(e -> this.estado.adicionarItem());
        }

        this.view.getPagarButton()
                .addActionListener(e -> this.estado.pagar());

        this.view.getFecharButton()
                .addActionListener(e -> this.estado.fechar());

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

    public void iniciar() {
        this.view.getJanelaPrincipal().setVisible(true);
        this.estado.entrar();
    }

    // ------------------------------------------------------------------
    // Transição de estado
    // ------------------------------------------------------------------

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
                item.getProduto().getCategoria(),
                item.getProduto().getNome(),
                String.format("R$ %s", formatoMoeda.format(item.getValorUnitario())),
                item.getQuantidade(),
                String.format("R$ %s", formatoMoeda.format(item.valorTotal()))
            });
        }
    }

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

    public void exibirMensagem(String mensagem, String titulo, int tipo) {
        JOptionPane.showMessageDialog(view.getJanelaPrincipal(),
                mensagem, titulo, tipo);
    }

    public void abrirCadastroCliente() {
        IClienteView view = new ClienteView();

        ClientePresenter clientePresenter = new ClientePresenter(view, this.clienteRepository);
        clientePresenter.setCommand(new SalvarClienteCommand(clientePresenter));
        clientePresenter.iniciar();
    }

    public void buscarClientePorCpf() {
        String cpfInformado = view.getTxtCpfCliente().getText().trim();

        if (cpfInformado == null || cpfInformado.isEmpty()) {
            exibirMensagem("Informe o CPF para buscar o cliente.",
                    "Buscar cliente", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String cpf = cpfInformado.replaceAll("\\D", "");

        if (cpf.length() != 11) {
            exibirMensagem("CPF deve conter 11 dígitos numéricos.",
                    "Buscar cliente", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!validarDigitosCpf(cpf)) {
            exibirMensagem("CPF inválido. Verifique os dígitos informados.",
                    "Buscar cliente", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Optional<Cliente> optCliente = clienteRepository.getPorCPF(cpf);
        if (optCliente.isEmpty()) {
            exibirMensagem("Cliente não encontrado para o CPF informado.",
                    "Buscar cliente", JOptionPane.INFORMATION_MESSAGE);
            view.getLblNomeCliente().setText("");
            return;
        }

        Cliente clienteEncontrado = optCliente.get();

        view.getLblNomeCliente().setText(clienteEncontrado.getNome());

        carregarEnderecosCliente(clienteEncontrado);

        if (this.pedido == null) {
            this.pedido = new Pedido(LocalDateTime.now(), clienteEncontrado,
                    pedidos.size() + 1, logger);
        } else {
            this.pedido = new Pedido(LocalDateTime.now(), clienteEncontrado,
                    pedido.getCodigoPedido(), logger);
        }

        atualizarTabela();
        atualizarValores();

        exibirMensagem("Cliente encontrado: " + clienteEncontrado.getNome(),
                "Buscar cliente", JOptionPane.INFORMATION_MESSAGE);
    }

    private void carregarEnderecosCliente(Cliente cliente) {
        JComboBox<String> combo = view.getEnderecoComboBox();
        combo.removeAllItems();
        if (cliente == null || cliente.getEnderecos() == null) {
            return;
        }
        for (Endereco end : cliente.getEnderecos()) {
            String descricao = end.getLogradouro()
                    + ", " + end.getNumero()
                    + " - " + end.getBairro();
            combo.addItem(descricao);
        }
    }

    private boolean validarDigitosCpf(String cpf) {
        if (cpf == null || cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
            return false;
        }
        int soma1 = 0;
        for (int i = 0; i < 9; i++) {
            soma1 += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
        }
        int dig1 = 11 - (soma1 % 11);
        if (dig1 >= 10) {
            dig1 = 0;
        }
        if (dig1 != Character.getNumericValue(cpf.charAt(9))) {
            return false;
        }

        int soma2 = 0;
        for (int i = 0; i < 10; i++) {
            soma2 += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
        }
        int dig2 = 11 - (soma2 % 11);
        if (dig2 >= 10) {
            dig2 = 0;
        }
        return dig2 == Character.getNumericValue(cpf.charAt(10));
    }

    public void abrirBuscaProdutos() {
        exibirMensagem("Abra a busca de produtos a partir do menu Operação.",
                "Adicionar item", JOptionPane.INFORMATION_MESSAGE);
    }

    public void removerItem(int linha) {
        if (pedido == null || linha < 0
                || linha >= pedido.getItens().size()) {
            return;
        }
        pedido.removerItem(pedido.getItens().get(linha));
        atualizarTabela();
        atualizarValores();
    }

    public boolean coletarItensDaTabela() {
        JTable tabela = view.getTabelaItens();
        if (tabela == null || pedido == null) {
            return false;
        }
        DefaultTableModel modelo = (DefaultTableModel) tabela.getModel();
        int totalLinhas = modelo.getRowCount();

        List<Item> itensResolvidos = new java.util.ArrayList<>();

        for (int i = 0; i < totalLinhas; i++) {
            Object valorItem = modelo.getValueAt(i, 1);
            Object valorQtd = modelo.getValueAt(i, 3);

            String nome = valorItem == null ? "" : valorItem.toString().trim();
            String qtdStr = valorQtd == null ? "" : valorQtd.toString().trim();

            if (nome.isEmpty() && qtdStr.isEmpty()) {
                continue;
            }
            if (nome.isEmpty() || qtdStr.isEmpty()) {
                exibirMensagem(
                        "Linha " + (i + 1) + ": preencha o nome do item e a "
                        + "quantidade, ou deixe ambos em branco.",
                        "Itens do pedido", JOptionPane.WARNING_MESSAGE);
                return false;
            }

            List<Produto> produtos = produtoRepository.buscarPorNome(nome);
            Produto produtoSelecionado = null;
            for (Produto p : produtos) {
                if (p.getNome().equalsIgnoreCase(nome)) {
                    produtoSelecionado = p;
                    break;
                }
            }
            if (produtoSelecionado == null) {
                exibirMensagem(
                        "Linha " + (i + 1) + ": produto \"" + nome
                        + "\" não encontrado no catálogo.",
                        "Itens do pedido", JOptionPane.WARNING_MESSAGE);
                return false;
            }

            int quantidade;
            try {
                quantidade = Integer.parseInt(qtdStr);
            } catch (NumberFormatException ex) {
                exibirMensagem(
                        "Linha " + (i + 1) + ": quantidade inválida (\""
                        + qtdStr + "\"). Informe um número inteiro.",
                        "Itens do pedido", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            if (quantidade <= 0) {
                exibirMensagem(
                        "Linha " + (i + 1) + ": quantidade deve ser maior "
                        + "que zero.",
                        "Itens do pedido", JOptionPane.WARNING_MESSAGE);
                return false;
            }

            Item item = new Item(produtoSelecionado.getNome(),
                    quantidade, produtoSelecionado.getPrecoUnitario(),
                    produtoSelecionado.getCategoria());
            item.setProduto(produtoSelecionado);
            itensResolvidos.add(item);
        }

        List<Item> itensAtuais = new java.util.ArrayList<>(pedido.getItens());
        for (Item item : itensAtuais) {
            pedido.removerItem(item);
        }
        for (Item item : itensResolvidos) {
            pedido.adicionarItem(item);
        }

        atualizarTabela();
        atualizarValores();
        return true;
    }

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
     * Simula o resultado do pagamento (US11), delegando ao
     * {@link PagamentoService} injetado - o que garante que a fonte de
     * aleatoriedade ({@link ISorteioPagamento}) possa ser substituída por uma
     * implementação determinística nos testes (DoD item 2). O
     * {@link ResultadoPagamento} produzido é armazenado para uso por
     * {@link #abrirTelaPagamento()}.
     *
     * @return {@code true} se aprovado; {@code false} se reprovado
     */
    public boolean simularPagamento() {
        if (pedido == null) {
            throw new IllegalStateException("Não há pedido para simular pagamento.");
        }
        this.resultadoPagamento = pagamentoService.processar(pedido);
        return this.resultadoPagamento.isAprovado();
    }

    /**
     * Confirma o pagamento: baixa o estoque em transação única (US10 cenário
     * 4), atualiza a situação do pedido para "Aguardando entrega" e persiste
     * o pedido no repositório. Deve ser chamado somente quando
     * {@link #simularPagamento()} retornar {@code true} e o estoque já
     * tiver sido validado.
     */
    public void confirmarPagamento() {
        if (pedido == null) {
            return;
        }
        for (var item : pedido.getItens()) {
            item.getProduto().baixarEstoque(item.getQuantidade());
        }
        pedido.setEstado("Aguardando entrega");
        repository.adicionar(pedido);
    }

    /**
     * Abre a tela de resultado do pagamento (US11 - Figura 12), populada com
     * o {@link ResultadoPagamento} da última tentativa simulada. Deve ser
     * chamada pelo estado (ex.: {@code ValidarPedidoState.pagar()}) após
     * {@link #simularPagamento()}, tanto em caso de aprovação quanto de
     * reprovação, já que a US11 exige que a tela seja exibida em ambos os
     * cenários.
     *
     * @param command comando responsável por tratar o fechamento da tela,
     *                que varia conforme o resultado (aprovado ou reprovado)
     */
    public void abrirTelaPagamento(PagamentoCommand command) {
        if (pedido == null || resultadoPagamento == null) {
            throw new IllegalStateException(
                    "Simule o pagamento antes de abrir a tela de resultado.");
        }
        PagamentoView pagamentoView = new PagamentoView();
        PagamentoPresenter pagamentoPresenter = new PagamentoPresenter(
                pagamentoView, command, pedido, resultadoPagamento);
        pagamentoPresenter.iniciar();
    }
}