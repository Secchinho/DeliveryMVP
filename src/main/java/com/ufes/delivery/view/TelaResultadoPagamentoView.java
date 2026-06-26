package com.ufes.delivery.view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * Tela de Resultado do Pagamento
 * --------------------------------
 * Esta classe implementa apenas a parte VISUAL da tela "Pagamento"
 * (resultado do pagamento).
 *
 * Mantém:
 *  - Faixa de status "Pagamento aprovado" / "Pagamento reprovado"
 *  - Faixa de status "Pedido pronto para entrega"
 *  - Resumo do Pedido (Pedido, Cliente, Endereço de entrega, Total do pedido)
 *  - Informações do Pagamento (Situação, Forma de pagamento, Data e hora,
 *    Identificador da transação, Valor pago)
 *  - Entrega (Situação do pedido, Prazo estimado de entrega, Observação)
 *  - Botão "Fechar"
 *
 * IMPORTANTE: esta tela NÃO contém dados de exemplo (mock). Todos os
 * campos/labels de valor são criados vazios. Os pontos exatos onde os
 * dados reais devem ser inseridos estão marcados com comentários
 * "// TODO: inserir dado real aqui" ao longo do código, junto com um
 * exemplo de como a inserção poderia ser feita.
 */
public class TelaResultadoPagamentoView extends JFrame {

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

    public TelaResultadoPagamentoView() {
        super("Pagamento");
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        // Texto inicial vazio: o texto e as cores (verde para aprovado,
        // vermelho para reprovado) devem ser definidos de acordo com o
        // resultado real do pagamento.
        lblStatusPagamento = new JLabel("", SwingConstants.CENTER);
        lblStatusPagamento.setFont(lblStatusPagamento.getFont().deriveFont(Font.BOLD, 20f));
        lblStatusPagamento.setOpaque(true);
        lblStatusPagamento.setBorder(new EmptyBorder(12, 12, 12, 12));

        /*
         * TODO: inserir dado real aqui.
         *
         * Exemplo de como preencher esta faixa de acordo com o resultado
         * do pagamento retornado pelo serviço de pagamento:
         *
         * if (resultadoPagamento.isAprovado()) {
         *     lblStatusPagamento.setText("Pagamento aprovado");
         *     lblStatusPagamento.setBackground(new Color(226, 245, 222)); // verde claro
         *     lblStatusPagamento.setForeground(new Color(30, 110, 30));  // verde escuro
         * } else {
         *     lblStatusPagamento.setText("Pagamento reprovado");
         *     lblStatusPagamento.setBackground(new Color(250, 224, 224)); // vermelho claro
         *     lblStatusPagamento.setForeground(new Color(150, 30, 30));   // vermelho escuro
         * }
         */

        JPanel painel = new JPanel(new BorderLayout());
        painel.add(lblStatusPagamento, BorderLayout.CENTER);
        return painel;
    }

    // ---------------------------------------------------------------
    // Faixa "Pedido pronto para entrega" (ou outra situação do pedido)
    // ---------------------------------------------------------------
    private JPanel criarFaixaStatusPedido() {
        // Texto inicial vazio: deve refletir a situação atual do pedido
        // após a confirmação do pagamento.
        lblStatusPedido = new JLabel("", SwingConstants.CENTER);
        lblStatusPedido.setFont(lblStatusPedido.getFont().deriveFont(Font.BOLD, 18f));
        lblStatusPedido.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(30, 110, 30), 2),
                new EmptyBorder(8, 12, 8, 12)));
        lblStatusPedido.setForeground(new Color(30, 110, 30));

        /*
         * TODO: inserir dado real aqui.
         *
         * Exemplo de como preencher esta faixa de acordo com a situação
         * do pedido retornada após o processamento do pagamento:
         *
         * lblStatusPedido.setText(pedido.getSituacao().getDescricao());
         * // Ex.: "Pedido pronto para entrega", "Pedido em preparação",
         * //      "Pedido cancelado", etc.
         *
         * Caso a situação não seja positiva, considerar ajustar também
         * a cor da borda/texto (por exemplo, para vermelho ou cinza).
         */

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
        /*
         * TODO: inserir dado real aqui.
         * lblPedidoValor.setText(String.valueOf(pedido.getNumero()));
         */

        gbc.gridy = 1;
        lblClienteValor = adicionarLinha(painel, gbc, "Cliente:", false);
        /*
         * TODO: inserir dado real aqui.
         * lblClienteValor.setText(pedido.getCliente().getNome());
         */

        gbc.gridy = 2;
        lblEnderecoEntregaValor = adicionarLinha(painel, gbc, "Endereço de entrega:", false);
        /*
         * TODO: inserir dado real aqui.
         * lblEnderecoEntregaValor.setText(pedido.getEnderecoEntrega().getDescricaoCompleta());
         */

        gbc.gridy = 3;
        lblTotalPedidoValor = adicionarLinha(painel, gbc, "Total do pedido:", true);
        /*
         * TODO: inserir dado real aqui.
         * lblTotalPedidoValor.setText(formatarMoeda(pedido.getTotalPedido()));
         */

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
        /*
         * TODO: inserir dado real aqui.
         * lblSituacaoPagamentoValor.setText(resultadoPagamento.getSituacao()); // Ex.: "Aprovado" / "Reprovado"
         * lblSituacaoPagamentoValor.setForeground(...); // verde se aprovado, vermelho se reprovado
         */

        gbc.gridy = 1;
        lblFormaPagamentoValor = adicionarLinha(painel, gbc, "Forma de pagamento:", false);
        /*
         * TODO: inserir dado real aqui.
         * lblFormaPagamentoValor.setText(resultadoPagamento.getFormaPagamento()); // Ex.: "PIX QR Code", "Cartão de crédito"
         */

        gbc.gridy = 2;
        lblDataHoraPagamentoValor = adicionarLinha(painel, gbc, "Data e hora do pagamento:", false);
        /*
         * TODO: inserir dado real aqui.
         * lblDataHoraPagamentoValor.setText(
         *     resultadoPagamento.getDataHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
         */

        gbc.gridy = 3;
        lblIdentificadorTransacaoValor = adicionarLinha(painel, gbc, "Identificador da transação:", false);
        /*
         * TODO: inserir dado real aqui.
         * lblIdentificadorTransacaoValor.setText(resultadoPagamento.getIdentificadorTransacao());
         */

        gbc.gridy = 4;
        lblValorPagoValor = adicionarLinha(painel, gbc, "Valor pago:", true);
        /*
         * TODO: inserir dado real aqui.
         * lblValorPagoValor.setText(formatarMoeda(resultadoPagamento.getValorPago()));
         */

        // Destaque de fundo na linha "Valor pago", como na imagem de referência.
        // Pode ser aplicado diretamente ao JLabel de valor, caso se deseje
        // reproduzir o fundo esverdeado mostrado na figura de referência:
        //
        // lblValorPagoValor.setOpaque(true);
        // lblValorPagoValor.setBackground(new Color(226, 245, 222));

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
        /*
         * TODO: inserir dado real aqui.
         * lblSituacaoPedidoValor.setText(pedido.getSituacaoEntrega().getDescricao());
         * // Ex.: "Pronto para entrega", "Em preparação", "Em rota de entrega"
         */

        gbc.gridy = 1;
        lblPrazoEstimadoValor = adicionarLinha(painel, gbc, "Prazo estimado de entrega:", true);
        /*
         * TODO: inserir dado real aqui.
         * lblPrazoEstimadoValor.setText(
         *     pedido.getPrazoEstimadoEntrega().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
         */

        gbc.gridy = 2;
        lblObservacaoValor = adicionarLinha(painel, gbc, "Observação:", false);
        /*
         * TODO: inserir dado real aqui.
         * lblObservacaoValor.setText(pedido.getObservacaoEntrega());
         * // Ex.: "Prazo gerado de forma simulada para o MVP" (texto fixo de negócio),
         * // ou alguma observação dinâmica vinda do backend.
         */

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

    /**
     * Adiciona uma linha "rótulo: valor" ao painel informado, na linha
     * (gridy) já configurada em gbc, e retorna o JLabel do valor (vazio),
     * para que o chamador guarde a referência e a preencha posteriormente
     * com o dado real.
     */
    private JLabel adicionarLinha(JPanel painel, GridBagConstraints gbc, String rotulo, boolean destaque) {
        gbc.gridx = 0;
        gbc.weightx = 0;
        JLabel lblRotulo = new JLabel(rotulo);
        painel.add(lblRotulo, gbc);

        // Valor inicial vazio - o texto deve ser preenchido com o dado real
        // (ver comentários "TODO: inserir dado real aqui" em cada chamada).
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

        // ---------------------------------------------------------------
        // LÓGICA DO BOTÃO "Fechar" (apenas comentada / não implementada)
        // ---------------------------------------------------------------
        btnFechar.addActionListener(e -> {
            /*
             * TODO: inserir dado real / lógica real aqui, se necessário.
             *
             * Exemplo de fluxo que poderia ser implementado:
             *
             * 1. Fechar esta tela de resultado de pagamento.
             * 2. Opcionalmente, voltar para a tela de listagem de pedidos
             *    ou para a tela inicial do sistema, já atualizada com o
             *    novo status do pedido:
             *
             *    TelaBuscaPedidoView telaPedidos = new TelaBuscaPedidoView();
             *    telaPedidos.setVisible(true);
             */
            dispose();
        });

        painel.add(btnFechar);
        return painel;
    }

    public static void main(String[] args) {
        // Look and feel padrão do sistema (apenas estético)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // Mantém o look and feel padrão do Swing em caso de falha
        }

        SwingUtilities.invokeLater(() -> {
            TelaResultadoPagamentoView tela = new TelaResultadoPagamentoView();
            tela.setVisible(true);
        });
    }
}
