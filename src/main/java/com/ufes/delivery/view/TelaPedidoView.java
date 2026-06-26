package com.ufes.delivery.view;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Tela de Inclusão de Pedido
 * --------------------------------
 * Esta classe implementa apenas a parte VISUAL da tela "Pedido".
 *
 * Mantém:
 *  - Dados do Pedido (Cliente / Novo Cliente / Endereço de entrega)
 *  - Tabela de itens do pedido (Categoria, Item, Preço unitário,
 *    Quantidade, Preço total), com remoção de item via menu de
 *    contexto (botão direito -> "Excluir")
 *  - Cupom de desconto + botão Aplicar
 *  - Totais: Total de descontos, Desconto na taxa de entrega,
 *    Taxa de entrega final, Total do pedido
 *  - Botões: Pagar / Cancelar
 *
 * Toda a lógica de busca de cliente/produto, cálculo de totais,
 * aplicação de cupom e persistência está comentada como sugestão
 * de implementação, mas não é executada. Os dados exibidos na
 * tabela e nos campos são apenas exemplos ilustrativos (mock),
 * iguais aos da Figura 11, para fins de demonstração visual.
 */
public class TelaPedidoView extends JFrame {

    // ----- Componentes "Dados do Pedido" -----
    private JTextField txtCliente;
    private JButton btnNovoCliente;
    private JComboBox<String> cmbEnderecoEntrega;

    // ----- Tabela de itens do pedido -----
    private JTable tabelaItens;
    private DefaultTableModel modeloItens;

    // ----- Cupom de desconto -----
    private JTextField txtCupomDesconto;
    private JButton btnAplicarCupom;

    // ----- Totais -----
    private JLabel lblTotalDescontosValor;
    private JLabel lblDescontoTaxaEntregaValor;
    private JLabel lblTaxaEntregaFinalValor;
    private JLabel lblTotalPedidoValor;
    private JPanel painelDescontoEntregaLinha;
    private JPanel painelTaxaEntregaLinha;

    // ----- Botões finais -----
    private JButton btnPagar;
    private JButton btnCancelar;

    public TelaPedidoView() {
        super("Pedido");
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

        JPanel painelCentro = new JPanel(new BorderLayout(10, 10));
        painelCentro.add(criarPainelDadosPedido(), BorderLayout.NORTH);
        painelCentro.add(criarPainelItens(), BorderLayout.CENTER);
        painelCentro.add(criarPainelTotais(), BorderLayout.SOUTH);

        painelPrincipal.add(painelCentro, BorderLayout.CENTER);
        painelPrincipal.add(criarPainelBotoes(), BorderLayout.SOUTH);
    }

    // ---------------------------------------------------------------
    // Painel "Dados do Pedido"
    // ---------------------------------------------------------------
    private JPanel criarPainelDadosPedido() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBorder(new TitledBorder("Dados do Pedido"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Linha Cliente
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        painel.add(new JLabel("Cliente"), gbc);

        // Valor de exemplo (mock), igual à Figura 11.
        txtCliente = new JTextField("Fulano de Tal");
        gbc.gridx = 1; gbc.weightx = 1;
        painel.add(txtCliente, gbc);

        btnNovoCliente = new JButton("Novo Cliente");
        gbc.gridx = 2; gbc.weightx = 0;
        painel.add(btnNovoCliente, gbc);

        // ---------------------------------------------------------------
        // LÓGICA DO BOTÃO "Novo Cliente" (apenas comentada)
        // ---------------------------------------------------------------
        btnNovoCliente.addActionListener(e -> {
            /*
             * Exemplo de fluxo que seria implementado aqui:
             *
             * Abrir a tela de cadastro de cliente (TelaCliente) para
             * permitir o cadastro de um novo cliente sem sair do pedido.
             * Ao salvar, o cliente recém-criado seria automaticamente
             * selecionado no campo "Cliente" desta tela.
             *
             * TelaCliente telaCliente = new TelaCliente();
             * telaCliente.setVisible(true);
             *
             * telaCliente.addClienteSalvoListener(cliente -> {
             *     txtCliente.setText(cliente.getNome());
             *     carregarEnderecosDoCliente(cliente);
             * });
             */
        });

        // Linha Endereço de entrega
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        painel.add(new JLabel("Endereço de entrega"), gbc);

        // Valor de exemplo (mock), igual à Figura 11.
        cmbEnderecoEntrega = new JComboBox<>(new String[] {
            "Rua Fulano, 123, Apto 101, Centro, Cidade Exemplo - ES, CEP 29000-000"
        });
        /*
         * Em uma implementação real, este combo seria populado com os
         * endereços cadastrados para o cliente selecionado:
         *
         * cmbEnderecoEntrega.removeAllItems();
         * for (Endereco end : cliente.getEnderecos()) {
         *     cmbEnderecoEntrega.addItem(end.getDescricaoCompleta());
         * }
         */
        gbc.gridx = 1; gbc.gridwidth = 2; gbc.weightx = 1;
        painel.add(cmbEnderecoEntrega, gbc);
        gbc.gridwidth = 1;

        return painel;
    }

    // ---------------------------------------------------------------
    // Painel da tabela de itens do pedido
    // ---------------------------------------------------------------
    private JPanel criarPainelItens() {
        JPanel painel = new JPanel(new BorderLayout());

        String[] colunas = { "Categoria", "Item", "Preço unitário", "Quantidade", "Preço total" };
        modeloItens = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Apenas a coluna "Quantidade" poderia ser editável em uma
                // implementação real, para permitir alterar a quantidade
                // diretamente na tabela. Aqui, mantemos tudo somente leitura,
                // pois é apenas a representação visual.
                return false;
            }
        };

        // Dados de exemplo (mock) apenas para representar visualmente a tela,
        // iguais aos exibidos na Figura 11.
        modeloItens.addRow(new Object[] { "Educação", "Livro de Matemática Básica", "R$ 45,00", "1", "R$ 45,00" });
        modeloItens.addRow(new Object[] { "Educação", "Caderno Universitário", "R$ 18,50", "2", "R$ 37,00" });
        modeloItens.addRow(new Object[] { "Lazer", "Jogo de Xadrez", "R$ 32,90", "1", "R$ 32,90" });
        modeloItens.addRow(new Object[] { "Entretenimento", "Quebra-cabeça 500 peças", "R$ 27,40", "1", "R$ 27,40" });
        // Linhas em branco extras para manter o espaço visual da tabela,
        // como exibido nas figuras de referência (10 e 11).
        modeloItens.addRow(new Object[] { "", "", "", "", "" });
        modeloItens.addRow(new Object[] { "", "", "", "", "" });

        tabelaItens = new JTable(modeloItens);
        tabelaItens.setRowHeight(26);
        tabelaItens.getTableHeader().setReorderingAllowed(false);
        tabelaItens.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        // ---------------------------------------------------------------
        // MENU DE CONTEXTO "Excluir" (clique com o botão direito do mouse)
        // ---------------------------------------------------------------
        JPopupMenu menuContexto = new JPopupMenu();
        JMenuItem itemExcluir = new JMenuItem("Excluir");
        menuContexto.add(itemExcluir);

        // ---------------------------------------------------------------
        // LÓGICA DE EXCLUSÃO DE ITEM (apenas comentada / não implementada)
        // ---------------------------------------------------------------
        itemExcluir.addActionListener(e -> {
            /*
             * Exemplo de fluxo que seria implementado aqui:
             *
             * 1. Obter a linha que estava selecionada quando o menu foi
             *    aberto (guardada em uma variável ao tratar o clique direito,
             *    ver MouseAdapter abaixo):
             *
             *    if (linhaClicada == -1) return;
             *
             * 2. Remover o item correspondente da lista de itens do pedido
             *    (modelo de domínio) e da tabela:
             *
             *    pedido.removerItem(linhaClicada);
             *    modeloItens.removeRow(linhaClicada);
             *
             * 3. Recalcular os totais do pedido (subtotal, descontos,
             *    taxa de entrega, total final):
             *
             *    recalcularTotais();
             */
        });

        // Captura a linha clicada com o botão direito para exibir o menu
        // de contexto sobre ela (apenas comportamento visual / de UI).
        tabelaItens.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (javax.swing.SwingUtilities.isRightMouseButton(e)) {
                    int linha = tabelaItens.rowAtPoint(e.getPoint());
                    if (linha >= 0) {
                        tabelaItens.setRowSelectionInterval(linha, linha);
                        menuContexto.show(tabelaItens, e.getX(), e.getY());
                    }
                }
            }
        });

        JScrollPane scroll = new JScrollPane(tabelaItens);
        scroll.setPreferredSize(new Dimension(700, 240));
        painel.add(scroll, BorderLayout.CENTER);

        return painel;
    }

    // ---------------------------------------------------------------
    // Painel "Cupom de desconto" + Totais
    // ---------------------------------------------------------------
    private JPanel criarPainelTotais() {
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBorder(new EmptyBorder(10, 0, 0, 0));

        // ----- Linha do cupom de desconto -----
        JPanel linhaCupom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
        linhaCupom.add(new JLabel("Cupom de desconto"));

        // Valor de exemplo (mock), igual à Figura 11.
        txtCupomDesconto = new JTextField("EDUCAR10", 18);
        linhaCupom.add(txtCupomDesconto);

        btnAplicarCupom = new JButton("Aplicar");
        linhaCupom.add(btnAplicarCupom);

        // ---------------------------------------------------------------
        // LÓGICA DE APLICAÇÃO DO CUPOM (apenas comentada / não implementada)
        // ---------------------------------------------------------------
        btnAplicarCupom.addActionListener(e -> {
            /*
             * Exemplo de fluxo que seria implementado aqui:
             *
             * 1. Obter o código do cupom informado:
             *
             *    String codigoCupom = txtCupomDesconto.getText();
             *
             * 2. Validar o cupom junto ao serviço/DAO de cupons:
             *
             *    CupomDAO cupomDao = new CupomDAO();
             *    Cupom cupom = cupomDao.buscarPorCodigo(codigoCupom);
             *
             *    if (cupom == null || !cupom.isValido()) {
             *        JOptionPane.showMessageDialog(this,
             *            "Cupom inválido ou expirado.",
             *            "Cupom de desconto", JOptionPane.WARNING_MESSAGE);
             *        return;
             *    }
             *
             * 3. Aplicar o desconto ao pedido (sobre os itens e/ou sobre a
             *    taxa de entrega, conforme as regras do cupom) e recalcular
             *    os totais:
             *
             *    pedido.aplicarCupom(cupom);
             *    recalcularTotais();
             */
        });

        // ----- Linhas de totais (alinhadas à direita) -----
        JPanel linhasTotais = new JPanel();
        linhasTotais.setLayout(new BoxLayout(linhasTotais, BoxLayout.Y_AXIS));

        // Cada rótulo de valor é criado primeiro (mantendo a referência
        // para futura atualização dinâmica) e só então embutido em sua
        // respectiva linha, evitando depender da estrutura interna do
        // painel retornado por criarLinhaTotal(...).
        lblTotalDescontosValor = new JLabel("R$ 10,00", SwingConstants.RIGHT);
        lblDescontoTaxaEntregaValor = new JLabel("R$ 4,00", SwingConstants.RIGHT);
        lblTaxaEntregaFinalValor = new JLabel("R$ 8,00", SwingConstants.RIGHT);
        lblTotalPedidoValor = new JLabel("R$ 140,30", SwingConstants.RIGHT);

        painelDescontoEntregaLinha = criarLinhaTotal("Total de descontos", lblTotalDescontosValor, false);
        JPanel linhaDescontoEntrega = criarLinhaTotal("Desconto na taxa de entrega", lblDescontoTaxaEntregaValor, false);
        JPanel linhaTaxaEntregaFinal = criarLinhaTotal("Taxa de entrega final", lblTaxaEntregaFinalValor, false);
        JPanel linhaTotalPedido = criarLinhaTotal("Total do pedido", lblTotalPedidoValor, true);

        linhasTotais.add(painelDescontoEntregaLinha);
        linhasTotais.add(linhaDescontoEntrega);
        linhasTotais.add(linhaTaxaEntregaFinal);
        linhasTotais.add(Box.createVerticalStrut(4));
        linhasTotais.add(linhaTotalPedido);

        painel.add(linhaCupom);
        painel.add(linhasTotais);

        /*
         * Exemplo de método que recalcularia todos os totais do pedido
         * sempre que houvesse alteração nos itens, cupom ou endereço de
         * entrega (que pode influenciar a taxa de entrega):
         *
         * private void recalcularTotais() {
         *     BigDecimal subtotal = pedido.calcularSubtotalItens();
         *     BigDecimal totalDescontos = pedido.calcularTotalDescontos();
         *     BigDecimal descontoTaxaEntrega = pedido.calcularDescontoTaxaEntrega();
         *     BigDecimal taxaEntregaFinal = pedido.calcularTaxaEntregaFinal();
         *     BigDecimal totalPedido = subtotal
         *             .subtract(totalDescontos)
         *             .add(taxaEntregaFinal);
         *
         *     lblTotalDescontosValor.setText(formatarMoeda(totalDescontos));
         *     lblDescontoTaxaEntregaValor.setText(formatarMoeda(descontoTaxaEntrega));
         *     lblTaxaEntregaFinalValor.setText(formatarMoeda(taxaEntregaFinal));
         *     lblTotalPedidoValor.setText(formatarMoeda(totalPedido));
         * }
         */

        return painel;
    }

    /**
     * Cria uma linha de total, com rótulo à esquerda e valor à direita,
     * usada tanto para as linhas normais (Total de descontos, etc.)
     * quanto para a linha em destaque "Total do pedido".
     *
     * Recebe o JLabel de valor já instanciado (em vez de uma String),
     * para que o chamador mantenha a referência e possa atualizar o
     * texto dinamicamente depois (ver recalcularTotais() comentado).
     */
    private JPanel criarLinhaTotal(String rotulo, JLabel lblValor, boolean destaque) {
        JPanel linha = new JPanel(new BorderLayout());
        linha.setBorder(new EmptyBorder(2, 0, 2, 0));

        JLabel lblRotulo = new JLabel(rotulo);

        if (destaque) {
            Font fonteNegrito = lblRotulo.getFont().deriveFont(Font.BOLD);
            lblRotulo.setFont(fonteNegrito);
            lblValor.setFont(fonteNegrito);
        }

        // Painel auxiliar para limitar a largura e alinhar à direita,
        // como na imagem de referência.
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(lblRotulo, BorderLayout.WEST);
        wrapper.add(lblValor, BorderLayout.EAST);
        wrapper.setMaximumSize(new Dimension(320, 24));
        wrapper.setPreferredSize(new Dimension(320, 24));

        JPanel alinhador = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        alinhador.add(wrapper);

        linha.add(alinhador, BorderLayout.CENTER);
        return linha;
    }

    // ---------------------------------------------------------------
    // Painel de botões "Pagar" / "Cancelar"
    // ---------------------------------------------------------------
    private JPanel criarPainelBotoes() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        btnPagar = new JButton("Pagar");
        btnCancelar = new JButton("Cancelar");

        // ---------------------------------------------------------------
        // LÓGICA DO BOTÃO "Pagar" (apenas comentada / não implementada)
        // ---------------------------------------------------------------
        btnPagar.addActionListener(e -> {
            /*
             * Exemplo de fluxo que seria implementado aqui:
             *
             * 1. Validar se há cliente selecionado, endereço de entrega
             *    escolhido e ao menos um item no pedido:
             *
             *    if (txtCliente.getText().isBlank()) {
             *        JOptionPane.showMessageDialog(this,
             *            "Selecione um cliente para o pedido.",
             *            "Validação", JOptionPane.WARNING_MESSAGE);
             *        return;
             *    }
             *
             *    if (cmbEnderecoEntrega.getSelectedItem() == null) {
             *        JOptionPane.showMessageDialog(this,
             *            "Selecione um endereço de entrega.",
             *            "Validação", JOptionPane.WARNING_MESSAGE);
             *        return;
             *    }
             *
             *    if (pedido.getItens().isEmpty()) {
             *        JOptionPane.showMessageDialog(this,
             *            "Adicione ao menos um item ao pedido.",
             *            "Validação", JOptionPane.WARNING_MESSAGE);
             *        return;
             *    }
             *
             * 2. Montar o objeto Pedido com cliente, endereço, itens,
             *    cupom aplicado e totais calculados:
             *
             *    Pedido pedido = new Pedido();
             *    pedido.setCliente(clienteSelecionado);
             *    pedido.setEnderecoEntrega(enderecoSelecionado);
             *    pedido.setItens(itensDoPedido);
             *    pedido.setCupom(cupomAplicado);
             *    pedido.setTotalPedido(totalCalculado);
             *
             * 3. Encaminhar para a tela/processo de pagamento e, em caso
             *    de sucesso, persistir o pedido:
             *
             *    PedidoDAO dao = new PedidoDAO();
             *    dao.salvar(pedido);
             *
             *    JOptionPane.showMessageDialog(this, "Pedido realizado com sucesso!");
             *    dispose();
             */
        });

        // ---------------------------------------------------------------
        // LÓGICA DO BOTÃO "Cancelar" (apenas comentada)
        // ---------------------------------------------------------------
        btnCancelar.addActionListener(e -> {
            /*
             * Exemplo de fluxo que seria implementado aqui:
             *
             * int opcao = JOptionPane.showConfirmDialog(this,
             *     "Deseja cancelar o pedido e descartar os itens adicionados?",
             *     "Cancelar", JOptionPane.YES_NO_OPTION);
             *
             * if (opcao == JOptionPane.YES_OPTION) {
             *     dispose();
             * }
             */
            dispose();
        });

        painel.add(btnPagar);
        painel.add(btnCancelar);
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
            TelaPedidoView tela = new TelaPedidoView();
            tela.setVisible(true);
        });
    }
}
