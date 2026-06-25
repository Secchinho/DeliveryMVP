package com.ufes.delivery.view;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * Tela de Cadastro de Produto
 * --------------------------------
 * Esta classe implementa apenas a parte VISUAL da tela "Produto".
 *
 * Mantém:
 *  - Código do produto
 *  - Nome do produto
 *  - Categoria (lista de seleção)
 *  - Preço unitário
 *  - Quantidade inicial em estoque
 *
 * Toda a lógica de persistência (inserção/leitura de dados em banco,
 * arquivo, etc.) está comentada como sugestão de implementação,
 * mas não é executada.
 */
public class TelaCadastroProdutoView extends JFrame {

    // ----- Componentes de dados do produto -----
    private JTextField txtCodigo;
    private JTextField txtNome;
    private JComboBox<String> cmbCategoria;
    private JTextField txtPrecoUnitario;
    private JTextField txtQuantidadeInicial;

    // ----- Botões -----
    private JButton btnSalvar;
    private JButton btnCancelar;

    public TelaCadastroProdutoView() {
        super("Produto");
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 560);
        setMinimumSize(new Dimension(650, 520));
        setLocationRelativeTo(null);

        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(painelPrincipal);

        painelPrincipal.add(criarPainelDadosProduto(), BorderLayout.NORTH);
        painelPrincipal.add(criarPainelBotoes(), BorderLayout.SOUTH);
    }

    // ---------------------------------------------------------------
    // Painel "Dados do Produto"
    // ---------------------------------------------------------------
    private JPanel criarPainelDadosProduto() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBorder(new TitledBorder("Dados do Produto"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Linha Código
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        painel.add(new JLabel("Código"), gbc);

        txtCodigo = new JTextField();
        txtCodigo.setPreferredSize(new Dimension(150, txtCodigo.getPreferredSize().height));
        gbc.gridx = 1; gbc.weightx = 0;
        painel.add(txtCodigo, gbc);

        // Espaçador para alinhar a largura igual à imagem (campo curto)
        gbc.gridx = 2; gbc.weightx = 1;
        painel.add(Box.createHorizontalGlue(), gbc);

        // Linha Nome
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        painel.add(new JLabel("Nome"), gbc);

        txtNome = new JTextField();
        gbc.gridx = 1; gbc.gridwidth = 2; gbc.weightx = 1;
        painel.add(txtNome, gbc);
        gbc.gridwidth = 1;

        // Linha Categoria
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        painel.add(new JLabel("Categoria"), gbc);

        cmbCategoria = new JComboBox<>();
        cmbCategoria.setPreferredSize(new Dimension(200, cmbCategoria.getPreferredSize().height));
        /*
         * Carga das categorias (apenas exemplo de como seria feito):
         *
         * List<Categoria> categorias = categoriaDAO.listarTodas();
         * for (Categoria c : categorias) {
         *     cmbCategoria.addItem(c.getNome());
         * }
         */
        gbc.gridx = 1; gbc.weightx = 0;
        painel.add(cmbCategoria, gbc);

        gbc.gridx = 2; gbc.weightx = 1;
        painel.add(Box.createHorizontalGlue(), gbc);

        // Linha Preço unitário
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        painel.add(new JLabel("Preço unitário"), gbc);

        txtPrecoUnitario = new JTextField();
        txtPrecoUnitario.setPreferredSize(new Dimension(150, txtPrecoUnitario.getPreferredSize().height));
        gbc.gridx = 1; gbc.weightx = 0;
        painel.add(txtPrecoUnitario, gbc);

        gbc.gridx = 2; gbc.weightx = 1;
        painel.add(Box.createHorizontalGlue(), gbc);

        // Linha Quantidade inicial em estoque
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0;
        painel.add(new JLabel("Quantidade inicial em estoque"), gbc);

        txtQuantidadeInicial = new JTextField();
        txtQuantidadeInicial.setPreferredSize(new Dimension(150, txtQuantidadeInicial.getPreferredSize().height));
        gbc.gridx = 1; gbc.weightx = 0;
        painel.add(txtQuantidadeInicial, gbc);

        gbc.gridx = 2; gbc.weightx = 1;
        painel.add(Box.createHorizontalGlue(), gbc);

        return painel;
    }

    // ---------------------------------------------------------------
    // Painel de botões "Salvar" / "Cancelar"
    // ---------------------------------------------------------------
    private JPanel criarPainelBotoes() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        btnSalvar = new JButton("Salvar");
        btnCancelar = new JButton("Cancelar");

        // ---------------------------------------------------------------
        // LÓGICA DE INSERÇÃO DE DADOS (apenas comentada / não implementada)
        // ---------------------------------------------------------------
        btnSalvar.addActionListener(e -> {
            /*
             * Exemplo de fluxo que seria implementado aqui:
             *
             * 1. Validar campos obrigatórios:
             *
             *    String codigo = txtCodigo.getText();
             *    String nome   = txtNome.getText();
             *    String categoria = (String) cmbCategoria.getSelectedItem();
             *    String precoTexto = txtPrecoUnitario.getText();
             *    String qtdTexto   = txtQuantidadeInicial.getText();
             *
             *    if (codigo.isBlank() || nome.isBlank() || categoria == null) {
             *        JOptionPane.showMessageDialog(this,
             *            "Código, Nome e Categoria são obrigatórios.",
             *            "Validação", JOptionPane.WARNING_MESSAGE);
             *        return;
             *    }
             *
             * 2. Converter valores numéricos com tratamento de erro:
             *
             *    BigDecimal precoUnitario;
             *    int quantidadeInicial;
             *    try {
             *        precoUnitario = new BigDecimal(precoTexto.replace(",", "."));
             *        quantidadeInicial = Integer.parseInt(qtdTexto);
             *    } catch (NumberFormatException ex) {
             *        JOptionPane.showMessageDialog(this,
             *            "Preço unitário e Quantidade devem ser numéricos.",
             *            "Validação", JOptionPane.WARNING_MESSAGE);
             *        return;
             *    }
             *
             * 3. Criar objeto Produto:
             *
             *    Produto produto = new Produto();
             *    produto.setCodigo(codigo);
             *    produto.setNome(nome);
             *    produto.setCategoria(categoria);
             *    produto.setPrecoUnitario(precoUnitario);
             *    produto.setQuantidadeEstoque(quantidadeInicial);
             *
             * 4. Persistir o produto (banco de dados, arquivo, API, etc.):
             *
             *    ProdutoDAO dao = new ProdutoDAO();
             *    dao.salvar(produto);
             *
             * 5. Exibir mensagem de sucesso e/ou fechar a tela:
             *
             *    JOptionPane.showMessageDialog(this, "Produto salvo com sucesso!");
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
             *     "Deseja cancelar e descartar as alterações?",
             *     "Cancelar", JOptionPane.YES_NO_OPTION);
             *
             * if (opcao == JOptionPane.YES_OPTION) {
             *     dispose();
             * }
             */
            dispose();
        });

        painel.add(btnSalvar);
        painel.add(btnCancelar);
        return painel;
    }

    /*
     * Exemplo de como seria a leitura/preenchimento inicial da tela
     * (ex.: ao abrir a tela para EDITAR um produto já existente):
     *
     * public void carregarProduto(Produto produto) {
     *     txtCodigo.setText(produto.getCodigo());
     *     txtNome.setText(produto.getNome());
     *     cmbCategoria.setSelectedItem(produto.getCategoria());
     *     txtPrecoUnitario.setText(produto.getPrecoUnitario().toString());
     *     txtQuantidadeInicial.setText(String.valueOf(produto.getQuantidadeEstoque()));
     * }
     */

    public static void main(String[] args) {
        // Look and feel padrão do sistema (apenas estético)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // Mantém o look and feel padrão do Swing em caso de falha
        }

        SwingUtilities.invokeLater(() -> {
            TelaCadastroProdutoView tela = new TelaCadastroProdutoView();
            tela.setVisible(true);
        });
    }
}
