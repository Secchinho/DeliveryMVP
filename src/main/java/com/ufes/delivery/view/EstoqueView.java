package com.ufes.delivery.view;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Tela de Movimentação de Estoque
 * --------------------------------
 * Esta classe implementa apenas a parte VISUAL da tela "Movimentação de Estoque".
 *
 * Mantém:
 *  - Busca de produtos (campo de busca + tabela de resultados + botão Selecionar)
 *  - Produto selecionado (somente leitura: Produto / Quantidade atual em estoque)
 *  - Dados da movimentação (Data, Tipo de movimentação, Quantidade a movimentar,
 *    Motivo do ajuste, Estoque após movimentação (prévia), Nota fiscal de entrada)
 *  - Botões: Confirmar movimentação / Cancelar
 *
 * Toda a lógica de busca/cálculo/persistência está comentada como sugestão
 * de implementação, mas não é executada. Os dados exibidos na tabela e nos
 * campos são apenas exemplos ilustrativos (mock), iguais aos da imagem de
 * referência, para fins de demonstração visual.
 */
public class EstoqueView extends JFrame {

    // ----- Componentes de busca de produtos -----
    private JTextField txtBuscarProduto;
    private JButton btnBuscar;
    private JTable tabelaProdutos;
    private DefaultTableModel modeloProdutos;
    private JButton btnSelecionar;

    // ----- Componentes "Produto Selecionado" -----
    private JTextField txtProdutoSelecionado;
    private JTextField txtQuantidadeAtual;

    // ----- Componentes "Movimentação" -----
    private JComboBox<String> cmbDataMovimentacao;
    private JComboBox<String> cmbTipoMovimentacao;
    private JTextField txtQuantidadeMovimentar;
    private JTextField txtMotivoAjuste;
    private JTextField txtEstoqueAposMovimentacao;
    private JTextField txtNotaFiscal;
    private JLabel lblAvisoPrevisualizacao;
    private JLabel lblAvisoRegras;

    // ----- Botões finais -----
    private JButton btnConfirmarMovimentacao;
    private JButton btnCancelar;

    public EstoqueView() {
        super("Movimentação de Estoque");
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(770, 640);
        setMinimumSize(new Dimension(740, 600));
        setLocationRelativeTo(null);

        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(painelPrincipal);

        JPanel painelCentro = new JPanel();
        painelCentro.setLayout(new BoxLayout(painelCentro, BoxLayout.Y_AXIS));

        painelCentro.add(criarPainelBuscaProdutos());
        painelCentro.add(Box.createVerticalStrut(12));
        painelCentro.add(criarPainelProdutoSelecionado());
        painelCentro.add(Box.createVerticalStrut(12));
        painelCentro.add(criarPainelMovimentacao());

        painelPrincipal.add(painelCentro, BorderLayout.CENTER);
        painelPrincipal.add(criarPainelBotoes(), BorderLayout.SOUTH);
    }

    // ---------------------------------------------------------------
    // Painel "Busca de Produtos"
    // ---------------------------------------------------------------
    private JPanel criarPainelBuscaProdutos() {
        JPanel painel = new JPanel(new BorderLayout(8, 8));
        painel.setBorder(new TitledBorder("Busca de Produtos"));

        // Linha de busca: rótulo + campo + botão
        JPanel linhaBusca = new JPanel(new BorderLayout(8, 8));
        linhaBusca.add(new JLabel("Buscar produto"), BorderLayout.WEST);

        txtBuscarProduto = new JTextField();
        linhaBusca.add(txtBuscarProduto, BorderLayout.CENTER);

        btnBuscar = new JButton("Buscar");
        linhaBusca.add(btnBuscar, BorderLayout.EAST);

        // ---------------------------------------------------------------
        // LÓGICA DE BUSCA DE PRODUTOS (apenas comentada / não implementada)
        // ---------------------------------------------------------------
        btnBuscar.addActionListener(e -> {
            /*
             * Exemplo de fluxo que seria implementado aqui:
             *
             * 1. Obter o termo de busca informado pelo usuário:
             *
             *    String termo = txtBuscarProduto.getText();
             *
             * 2. Consultar produtos cujo nome/código contenha o termo:
             *
             *    ProdutoDAO dao = new ProdutoDAO();
             *    List<Produto> produtos = dao.buscarPorNomeOuCodigo(termo);
             *
             * 3. Limpar a tabela e preencher com os resultados encontrados:
             *
             *    modeloProdutos.setRowCount(0);
             *    for (Produto p : produtos) {
             *        modeloProdutos.addRow(new Object[] {
             *            p.getCodigo(), p.getNome(), p.getCategoria(), p.getEstoqueAtual()
             *        });
             *    }
             */
        });

        // Tabela de resultados
        String[] colunas = { "Código", "Produto", "Categoria", "Estoque atual" };
        modeloProdutos = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // tabela apenas para seleção, não editável
            }
        };

        // Dados de exemplo (mock) apenas para representar visualmente a tela,
        // iguais aos exibidos na imagem de referência.
        modeloProdutos.addRow(new Object[] { "2001", "Caderno Universitário", "Papelaria", "120" });
        modeloProdutos.addRow(new Object[] { "2002", "Livro de Matemática Básica", "Educação", "35" });

        tabelaProdutos = new JTable(modeloProdutos);
        tabelaProdutos.setRowHeight(26);
        tabelaProdutos.getTableHeader().setReorderingAllowed(false);
        tabelaProdutos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(tabelaProdutos);
        scroll.setPreferredSize(new Dimension(700, 150));

        // Botão "Selecionar", alinhado à direita, abaixo da tabela
        JPanel painelBotaoSelecionar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnSelecionar = new JButton("Selecionar");

        // ---------------------------------------------------------------
        // LÓGICA DE SELEÇÃO DE PRODUTO (apenas comentada / não implementada)
        // ---------------------------------------------------------------
        btnSelecionar.addActionListener(e -> {
            /*
             * Exemplo de fluxo que seria implementado aqui:
             *
             * 1. Verificar se uma linha está selecionada na tabela:
             *
             *    int linha = tabelaProdutos.getSelectedRow();
             *    if (linha == -1) {
             *        JOptionPane.showMessageDialog(this,
             *            "Selecione um produto na tabela.",
             *            "Seleção", JOptionPane.WARNING_MESSAGE);
             *        return;
             *    }
             *
             * 2. Obter os dados do produto selecionado e preencher os campos
             *    do painel "Produto Selecionado":
             *
             *    String codigo = (String) modeloProdutos.getValueAt(linha, 0);
             *    String nome   = (String) modeloProdutos.getValueAt(linha, 1);
             *    String estoqueAtual = String.valueOf(modeloProdutos.getValueAt(linha, 3));
             *
             *    txtProdutoSelecionado.setText(nome);
             *    txtQuantidadeAtual.setText(estoqueAtual);
             *
             * 3. Recalcular a prévia do estoque após movimentação, caso já
             *    haja uma quantidade informada (ver método recalcularPrevia()).
             */
        });
        painelBotaoSelecionar.add(btnSelecionar);

        JPanel painelTabela = new JPanel(new BorderLayout());
        painelTabela.add(scroll, BorderLayout.CENTER);
        painelTabela.add(painelBotaoSelecionar, BorderLayout.SOUTH);

        painel.add(linhaBusca, BorderLayout.NORTH);
        painel.add(painelTabela, BorderLayout.CENTER);

        return painel;
    }

    // ---------------------------------------------------------------
    // Painel "Produto Selecionado" (somente leitura)
    // ---------------------------------------------------------------
    private JPanel criarPainelProdutoSelecionado() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBorder(new TitledBorder("Produto Selecionado"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Produto
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        painel.add(new JLabel("Produto"), gbc);

        // Valor de exemplo (mock) representando o produto já selecionado
        // na tabela acima, igual à imagem de referência.
        txtProdutoSelecionado = new JTextField("Caderno Universitário");
        txtProdutoSelecionado.setEditable(false);
        gbc.gridx = 1; gbc.weightx = 1;
        painel.add(txtProdutoSelecionado, gbc);

        // Quantidade atual em estoque
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        painel.add(new JLabel("Quantidade atual em estoque"), gbc);

        txtQuantidadeAtual = new JTextField("120");
        txtQuantidadeAtual.setEditable(false);
        gbc.gridx = 1; gbc.weightx = 1;
        painel.add(txtQuantidadeAtual, gbc);

        return painel;
    }

    // ---------------------------------------------------------------
    // Painel "Movimentação"
    // ---------------------------------------------------------------
    private JPanel criarPainelMovimentacao() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBorder(new TitledBorder("Movimentação"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // ----- Linha 1: Data da movimentação / Tipo de movimentação -----
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        painel.add(new JLabel("Data da movimentação"), gbc);

        cmbDataMovimentacao = new JComboBox<>(new String[] { "20/06/2026" });
        cmbDataMovimentacao.setEditable(true);
        cmbDataMovimentacao.setPreferredSize(new Dimension(140, cmbDataMovimentacao.getPreferredSize().height));
        /*
         * Em uma implementação real, este campo provavelmente usaria um
         * componente de seleção de data (ex.: JDateChooser de alguma lib,
         * ou um JSpinner com SpinnerDateModel), em vez de um combo editável.
         */
        gbc.gridx = 1; gbc.weightx = 0;
        painel.add(cmbDataMovimentacao, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        painel.add(new JLabel("Tipo de movimentação"), gbc);

        cmbTipoMovimentacao = new JComboBox<>(new String[] { "Entrada", "Saída", "Ajuste de estoque" });
        cmbTipoMovimentacao.setSelectedItem("Ajuste de estoque");
        cmbTipoMovimentacao.setPreferredSize(new Dimension(180, cmbTipoMovimentacao.getPreferredSize().height));
        gbc.gridx = 3; gbc.weightx = 1;
        painel.add(cmbTipoMovimentacao, gbc);

        // ---------------------------------------------------------------
        // LÓGICA DE TROCA DE TIPO DE MOVIMENTAÇÃO (apenas comentada)
        // ---------------------------------------------------------------
        cmbTipoMovimentacao.addActionListener(e -> {
            /*
             * Exemplo de fluxo que seria implementado aqui:
             *
             * String tipo = (String) cmbTipoMovimentacao.getSelectedItem();
             *
             * - Se tipo == "Entrada": habilitar o campo "Nota fiscal de
             *   entrada" e tornar seu preenchimento obrigatório.
             * - Se tipo == "Ajuste de estoque": habilitar o campo "Motivo
             *   do ajuste" e tornar seu preenchimento obrigatório.
             * - Se tipo == "Saída": desabilitar ambos os campos extras,
             *   se não forem aplicáveis.
             *
             * txtNotaFiscal.setEnabled(tipo.equals("Entrada"));
             * txtMotivoAjuste.setEnabled(tipo.equals("Ajuste de estoque"));
             *
             * recalcularPrevia();
             */
        });

        // ----- Linha 2: Quantidade a movimentar / Motivo do ajuste -----
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        painel.add(new JLabel("Quantidade a movimentar"), gbc);

        txtQuantidadeMovimentar = new JTextField("15");
        gbc.gridx = 1; gbc.weightx = 0;
        painel.add(txtQuantidadeMovimentar, gbc);

        // ---------------------------------------------------------------
        // LÓGICA DE RECÁLCULO DA PRÉVIA DE ESTOQUE (apenas comentada)
        // ---------------------------------------------------------------
        /*
         * Exemplo de listener que seria adicionado ao campo de quantidade,
         * para recalcular a prévia do estoque a cada alteração:
         *
         * txtQuantidadeMovimentar.getDocument().addDocumentListener(
         *     new javax.swing.event.DocumentListener() {
         *         public void insertUpdate(javax.swing.event.DocumentEvent e) { recalcularPrevia(); }
         *         public void removeUpdate(javax.swing.event.DocumentEvent e) { recalcularPrevia(); }
         *         public void changedUpdate(javax.swing.event.DocumentEvent e) { recalcularPrevia(); }
         *     });
         *
         * private void recalcularPrevia() {
         *     try {
         *         int estoqueAtual = Integer.parseInt(txtQuantidadeAtual.getText());
         *         int quantidade   = Integer.parseInt(txtQuantidadeMovimentar.getText());
         *         String tipo = (String) cmbTipoMovimentacao.getSelectedItem();
         *
         *         int previa;
         *         if (tipo.equals("Entrada")) {
         *             previa = estoqueAtual + quantidade;
         *         } else if (tipo.equals("Saída")) {
         *             previa = estoqueAtual - quantidade;
         *         } else { // Ajuste de estoque: quantidade pode ser o novo valor ou a diferença
         *             previa = estoqueAtual + quantidade;
         *         }
         *         txtEstoqueAposMovimentacao.setText(String.valueOf(previa));
         *     } catch (NumberFormatException ex) {
         *         txtEstoqueAposMovimentacao.setText("");
         *     }
         * }
         */

        gbc.gridx = 2; gbc.weightx = 0;
        painel.add(new JLabel("Motivo do ajuste"), gbc);

        txtMotivoAjuste = new JTextField("Correção de contagem física");
        gbc.gridx = 3; gbc.weightx = 1;
        painel.add(txtMotivoAjuste, gbc);

        // ----- Linha 3: Estoque após movimentação (prévia) / Nota fiscal -----
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        painel.add(new JLabel("Estoque após movimentação (prévia)"), gbc);

        // Valor de exemplo (mock): 120 (atual) + 15 (a movimentar) - 30 = 105,
        // apenas para representar visualmente a prévia mostrada na imagem.
        txtEstoqueAposMovimentacao = new JTextField("105");
        txtEstoqueAposMovimentacao.setEditable(false);
        gbc.gridx = 1; gbc.weightx = 0;
        painel.add(txtEstoqueAposMovimentacao, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        painel.add(new JLabel("Nota fiscal de entrada"), gbc);

        txtNotaFiscal = new JTextField("(Opcional)");
        // Desabilitado pois o tipo selecionado por padrão é "Ajuste de estoque",
        // que não exige nota fiscal (apenas aplicável para "Entrada").
        txtNotaFiscal.setEnabled(false);
        gbc.gridx = 3; gbc.weightx = 1;
        painel.add(txtNotaFiscal, gbc);

        // ----- Linha 4: Avisos -----
        lblAvisoPrevisualizacao = new JLabel(
            "Pré-visualização, a atualização definitiva ocorrerá após a confirmação da movimentação.");
        lblAvisoPrevisualizacao.setForeground(new Color(0, 70, 160));
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 4; gbc.weightx = 1;
        painel.add(lblAvisoPrevisualizacao, gbc);

        lblAvisoRegras = new JLabel(
            "Ajustes de estoque requerem motivo. Entradas requerem número da nota fiscal.");
        lblAvisoRegras.setForeground(new Color(0, 70, 160));
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 4; gbc.weightx = 1;
        painel.add(lblAvisoRegras, gbc);

        return painel;
    }

    // ---------------------------------------------------------------
    // Painel de botões "Confirmar movimentação" / "Cancelar"
    // ---------------------------------------------------------------
    private JPanel criarPainelBotoes() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        btnConfirmarMovimentacao = new JButton("Confirmar movimentação");
        btnCancelar = new JButton("Cancelar");

        // ---------------------------------------------------------------
        // LÓGICA DE CONFIRMAÇÃO DA MOVIMENTAÇÃO (apenas comentada)
        // ---------------------------------------------------------------
        btnConfirmarMovimentacao.addActionListener(e -> {
            /*
             * Exemplo de fluxo que seria implementado aqui:
             *
             * 1. Validar se um produto foi selecionado:
             *
             *    if (txtProdutoSelecionado.getText().isBlank()) {
             *        JOptionPane.showMessageDialog(this,
             *            "Selecione um produto antes de confirmar a movimentação.",
             *            "Validação", JOptionPane.WARNING_MESSAGE);
             *        return;
             *    }
             *
             * 2. Validar a quantidade informada:
             *
             *    int quantidade;
             *    try {
             *        quantidade = Integer.parseInt(txtQuantidadeMovimentar.getText());
             *        if (quantidade <= 0) throw new NumberFormatException();
             *    } catch (NumberFormatException ex) {
             *        JOptionPane.showMessageDialog(this,
             *            "Informe uma quantidade válida maior que zero.",
             *            "Validação", JOptionPane.WARNING_MESSAGE);
             *        return;
             *    }
             *
             * 3. Validar regras específicas de cada tipo de movimentação:
             *
             *    String tipo = (String) cmbTipoMovimentacao.getSelectedItem();
             *
             *    if (tipo.equals("Ajuste de estoque") && txtMotivoAjuste.getText().isBlank()) {
             *        JOptionPane.showMessageDialog(this,
             *            "Informe o motivo do ajuste de estoque.",
             *            "Validação", JOptionPane.WARNING_MESSAGE);
             *        return;
             *    }
             *
             *    if (tipo.equals("Entrada") && txtNotaFiscal.getText().isBlank()) {
             *        JOptionPane.showMessageDialog(this,
             *            "Informe o número da nota fiscal de entrada.",
             *            "Validação", JOptionPane.WARNING_MESSAGE);
             *        return;
             *    }
             *
             * 4. Montar o objeto de movimentação e persistir:
             *
             *    MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();
             *    movimentacao.setProduto(produtoSelecionado);
             *    movimentacao.setData(LocalDate.parse((String) cmbDataMovimentacao.getSelectedItem(), formato));
             *    movimentacao.setTipo(tipo);
             *    movimentacao.setQuantidade(quantidade);
             *    movimentacao.setMotivoAjuste(txtMotivoAjuste.getText());
             *    movimentacao.setNotaFiscal(txtNotaFiscal.getText());
             *
             *    MovimentacaoEstoqueDAO dao = new MovimentacaoEstoqueDAO();
             *    dao.confirmar(movimentacao); // atualiza definitivamente o estoque
             *
             * 5. Exibir mensagem de sucesso e/ou fechar a tela:
             *
             *    JOptionPane.showMessageDialog(this, "Movimentação confirmada com sucesso!");
             *    dispose();
             */
        });

        // ---------------------------------------------------------------
        // LÓGICA DE CANCELAMENTO (apenas comentada)
        // ---------------------------------------------------------------
        btnCancelar.addActionListener(e -> {
            /*
             * Exemplo de fluxo que seria implementado aqui:
             *
             * int opcao = JOptionPane.showConfirmDialog(this,
             *     "Deseja cancelar e descartar a movimentação?",
             *     "Cancelar", JOptionPane.YES_NO_OPTION);
             *
             * if (opcao == JOptionPane.YES_OPTION) {
             *     dispose();
             * }
             */
            dispose();
        });

        painel.add(btnConfirmarMovimentacao);
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
            EstoqueView tela = new EstoqueView();
            tela.setVisible(true);
        });
    }
}
