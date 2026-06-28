package com.ufes.delivery.view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class PagamentoView extends JFrame implements IPagamentoView {

    // ----- Faixas de status -----
    private JLabel lblStatusPagamento;
    private JLabel lblStatusPedido;

    // ----- Resumo do Pedido -----
    private JLabel lblPedidoValor;
    private JLabel lblClienteValor;
    private JLabel lblEnderecoEntregaValor;
    private JLabel lblTotalPedidoValor;

    // ----- Informações do Pagamento -----
    private JLabel lblSituacaoPagamentoValor;
    private JLabel lblFormaPagamentoValor;
    private JLabel lblDataHoraPagamentoValor;
    private JLabel lblIdentificadorTransacaoValor;
    private JLabel lblValorPagoValor;

    // ----- Entrega -----
    private JLabel lblSituacaoPedidoValor;
    private JLabel lblPrazoEstimadoValor;
    private JLabel lblObservacaoValor;

    // ----- Botão -----
    private JButton btnFechar;

    // ----- Atributo do Presenter (Comentado para uso futuro) -----
    // private PagamentoPresenter presenter;

    public PagamentoView() {
        super("Pagamento");
        initComponents();
    }

    // =========================================================================
    // IMPLEMENTAÇÃO DA INTERFACE (IPagamentoView)
    // =========================================================================

    @Override
    public void setStatusPagamento(String status, boolean aprovado) {
        this.lblStatusPagamento.setText(status);
        this.lblStatusPagamento.setOpaque(true);
        if (aprovado) {
            this.lblStatusPagamento.setBackground(new Color(226, 245, 222)); // Verde claro
            this.lblStatusPagamento.setForeground(new Color(30, 110, 30));    // Verde escuro
        } else {
            this.lblStatusPagamento.setBackground(new Color(250, 224, 224)); // Vermelho claro
            this.lblStatusPagamento.setForeground(new Color(150, 30, 30));    // Vermelho escuro
        }
    }

    @Override
    public void setStatusPedido(String status) {
        this.lblStatusPedido.setText(status);
    }

    @Override
    public void setPedidoNumero(String numero) {
        this.lblPedidoValor.setText(numero);
    }

    @Override
    public void setClienteNome(String nome) {
        this.lblClienteValor.setText(nome);
    }

    @Override
    public void setEnderecoEntrega(String endereco) {
        this.lblEnderecoEntregaValor.setText(endereco);
    }

    @Override
    public void setTotalPedido(String total) {
        this.lblTotalPedidoValor.setText(total);
    }

    @Override
    public void setSituacaoPagamento(String situacao) {
        this.lblSituacaoPagamentoValor.setText(situacao);
    }

    @Override
    public void setFormaPagamento(String forma) {
        this.lblFormaPagamentoValor.setText(forma);
    }

    @Override
    public void setDataHoraPagamento(String dataHora) {
        this.lblDataHoraPagamentoValor.setText(dataHora);
    }

    @Override
    public void setIdentificadorTransacao(String id) {
        this.lblIdentificadorTransacaoValor.setText(id);
    }

    @Override
    public void setValorPago(String valor) {
        this.lblValorPagoValor.setText(valor);
        // Destaque de fundo na linha "Valor pago", se desejar:
        // this.lblValorPagoValor.setOpaque(true);
        // this.lblValorPagoValor.setBackground(new Color(226, 245, 222));
    }

    @Override
    public void setSituacaoPedido(String situacao) {
        this.lblSituacaoPedidoValor.setText(situacao);
    }

    @Override
    public void setPrazoEstimado(String prazo) {
        this.lblPrazoEstimadoValor.setText(prazo);
    }

    @Override
    public void setObservacao(String obs) {
        this.lblObservacaoValor.setText(obs);
    }

    @Override
    public JButton getFecharButton() {
        return this.btnFechar;
    }

    @Override
    public void fecharTela() {
        this.dispose();
    }

    @Override
    public JFrame getJanelaPrincipal() {
        return this;
    }

    // =========================================================================
    // FIM DA IMPLEMENTAÇÃO DA INTERFACE
    // =========================================================================

    /**
     * Método para ligar a View ao Presenter.
     * Descomente e utilize quando criar a classe PagamentoPresenter.
     */
    // public void setPresenter(PagamentoPresenter presenter) {
    //     this.presenter = presenter;
    // }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(770, 620);
        setMinimumSize(new Dimension(720, 580));
        setLocationRelativeTo(null);

        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(painelPrincipal);

        JPanel painelCentro = new JPanel();
        painelCentro.setLayout(new BoxLayout(painelCentro, BoxLayout.Y_AXIS));

        painelCentro.add(criarFaixaStatusPagamento());
        painelCentro.add(Box.createVerticalStrut(8));
        painelCentro.add(criarFaixaStatusPedido());
        painelCentro.add(Box.createVerticalStrut(12));
        painelCentro.add(criarPainelResumoPedido());
        painelCentro.add(Box.createVerticalStrut(10));
        painelCentro.add(criarPainelInformacoesPagamento());
        painelCentro.add(Box.createVerticalStrut(10));
        painelCentro.add(criarPainelEntrega());

        painelPrincipal.add(painelCentro, BorderLayout.CENTER);
        painelPrincipal.add(criarPainelBotoes(), BorderLayout.SOUTH);
    }

    // ---------------------------------------------------------------
    // Faixa "Pagamento aprovado" / "Pagamento reprovado"
    // ---------------------------------------------------------------
    private JPanel criarFaixaStatusPagamento() {
        lblStatusPagamento = new JLabel("", SwingConstants.CENTER);
        lblStatusPagamento.setFont(lblStatusPagamento.getFont().deriveFont(Font.BOLD, 20f));
        lblStatusPagamento.setOpaque(true);
        lblStatusPagamento.setBorder(new EmptyBorder(12, 12, 12, 12));

        JPanel painel = new JPanel(new BorderLayout());
        painel.add(lblStatusPagamento, BorderLayout.CENTER);
        return painel;
    }

    // ---------------------------------------------------------------
    // Faixa "Pedido pronto para entrega" (ou outra situação do pedido)
    // ---------------------------------------------------------------
    private JPanel criarFaixaStatusPedido() {
        lblStatusPedido = new JLabel("", SwingConstants.CENTER);
        lblStatusPedido.setFont(lblStatusPedido.getFont().deriveFont(Font.BOLD, 18f));
        lblStatusPedido.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(30, 110, 30), 2),
                new EmptyBorder(8, 12, 8, 12)));
        lblStatusPedido.setForeground(new Color(30, 110, 30));

        JPanel painel = new JPanel(new BorderLayout());
        painel.add(lblStatusPedido, BorderLayout.CENTER);
        return painel;
    }

    // ---------------------------------------------------------------
    // Painel "Resumo do Pedido"
    // ---------------------------------------------------------------
    private JPanel criarPainelResumoPedido() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBorder(new TitledBorder("Resumo do Pedido"));

        GridBagConstraints gbc = criarGbcPadrao();

        gbc.gridy = 0;
        lblPedidoValor = adicionarLinha(painel, gbc, "Pedido:", false);

        gbc.gridy = 1;
        lblClienteValor = adicionarLinha(painel, gbc, "Cliente:", false);

        gbc.gridy = 2;
        lblEnderecoEntregaValor = adicionarLinha(painel, gbc, "Endereço de entrega:", false);

        gbc.gridy = 3;
        lblTotalPedidoValor = adicionarLinha(painel, gbc, "Total do pedido:", true);

        return painel;
    }

    // ---------------------------------------------------------------
    // Painel "Informações do Pagamento"
    // ---------------------------------------------------------------
    private JPanel criarPainelInformacoesPagamento() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBorder(new TitledBorder("Informações do Pagamento"));

        GridBagConstraints gbc = criarGbcPadrao();

        gbc.gridy = 0;
        lblSituacaoPagamentoValor = adicionarLinha(painel, gbc, "Situação do pagamento:", false);

        gbc.gridy = 1;
        lblFormaPagamentoValor = adicionarLinha(painel, gbc, "Forma de pagamento:", false);

        gbc.gridy = 2;
        lblDataHoraPagamentoValor = adicionarLinha(painel, gbc, "Data e hora do pagamento:", false);

        gbc.gridy = 3;
        lblIdentificadorTransacaoValor = adicionarLinha(painel, gbc, "Identificador da transação:", false);

        gbc.gridy = 4;
        lblValorPagoValor = adicionarLinha(painel, gbc, "Valor pago:", true);

        return painel;
    }

    // ---------------------------------------------------------------
    // Painel "Entrega"
    // ---------------------------------------------------------------
    private JPanel criarPainelEntrega() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBorder(new TitledBorder("Entrega"));

        GridBagConstraints gbc = criarGbcPadrao();

        gbc.gridy = 0;
        lblSituacaoPedidoValor = adicionarLinha(painel, gbc, "Situação do pedido:", false);

        gbc.gridy = 1;
        lblPrazoEstimadoValor = adicionarLinha(painel, gbc, "Prazo estimado de entrega:", true);

        gbc.gridy = 2;
        lblObservacaoValor = adicionarLinha(painel, gbc, "Observação:", false);

        return painel;
    }

    // ---------------------------------------------------------------
    // Métodos auxiliares de layout
    // ---------------------------------------------------------------

    private GridBagConstraints criarGbcPadrao() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 8, 4, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.weightx = 0;
        return gbc;
    }

    private JLabel adicionarLinha(JPanel painel, GridBagConstraints gbc, String rotulo, boolean destaque) {
        gbc.gridx = 0;
        gbc.weightx = 0;
        JLabel lblRotulo = new JLabel(rotulo);
        painel.add(lblRotulo, gbc);

        JLabel lblValor = new JLabel("");
        if (destaque) {
            Font fonteNegrito = lblValor.getFont().deriveFont(Font.BOLD);
            lblValor.setFont(fonteNegrito);
            lblRotulo.setFont(fonteNegrito);
        }

        gbc.gridx = 1;
        gbc.weightx = 1;
        painel.add(lblValor, gbc);

        return lblValor;
    }

    // ---------------------------------------------------------------
    // Painel de botão "Fechar"
    // ---------------------------------------------------------------
    private JPanel criarPainelBotoes() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        btnFechar = new JButton("Fechar");

        // --- REMOVIDO: Listener do botão Fechar ---
        // A lógica de navegação será tratada no Presenter.

        painel.add(btnFechar);
        return painel;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            PagamentoView view = new PagamentoView();

            // ---------------------------------------------------------------
            // FLUXO DE CRIAÇÃO DO MVP (Descomente quando criar o Presenter)
            // ---------------------------------------------------------------
            // PagamentoPresenter presenter = new PagamentoPresenter(view);
            // view.setPresenter(presenter);
            // presenter.iniciar(); // Para popular todos os dados do pagamento!
            // ---------------------------------------------------------------

            view.setVisible(true);
        });
    }
}