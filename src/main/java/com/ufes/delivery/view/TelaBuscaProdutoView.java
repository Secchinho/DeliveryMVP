package com.ufes.delivery.view;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Tela de Busca de Produtos
 * --------------------------------
 * Esta classe implementa apenas a parte VISUAL da tela "Produtos" (busca).
 *
 * Mantém:
 *  - Filtro de busca (Buscar por / Valor / Buscar)
 *  - Tabela de resultados (Código, Nome, Categoria, Preço unitário,
 *    Estoque atual, Ação)
 *  - Botões inferiores: Novo, Visualizar, Fechar
 *
 * Toda a lógica de busca/leitura de dados está comentada como sugestão
 * de implementação, mas não é executada. Os dados exibidos na tabela
 * são apenas exemplos ilustrativos (mock), iguais aos da imagem de
 * referência, para fins de demonstração visual.
 */
public class TelaBuscaProdutoView extends JFrame {

    // ----- Componentes do painel de busca -----
    private JComboBox<String> cmbBuscarPor;
    private JTextField txtValor;
    private JButton btnBuscar;

    // ----- Componentes da tabela de resultados -----
    private JTable tabelaResultados;
    private DefaultTableModel modeloResultados;

    // ----- Botões inferiores -----
    private JButton btnNovo;
    private JButton btnVisualizar;
    private JButton btnFechar;

    public TelaBuscaProdutoView() {
        super("Produtos");
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(760, 600);
        setMinimumSize(new Dimension(700, 560));
        setLocationRelativeTo(null);

        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(painelPrincipal);

        JPanel painelCentro = new JPanel();
        painelCentro.setLayout(new BoxLayout(painelCentro, BoxLayout.Y_AXIS));

        painelCentro.add(criarPainelBusca());
        painelCentro.add(Box.createVerticalStrut(15));
        painelCentro.add(criarPainelResultados());

        painelPrincipal.add(painelCentro, BorderLayout.CENTER);
        painelPrincipal.add(criarPainelBotoes(), BorderLayout.SOUTH);
    }

    // ---------------------------------------------------------------
    // Painel "Busca de Produtos"
    // ---------------------------------------------------------------
    private JPanel criarPainelBusca() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBorder(new TitledBorder("Busca de Produtos"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // "Buscar por"
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        painel.add(new JLabel("Buscar por"), gbc);

        cmbBuscarPor = new JComboBox<>(new String[] { "Nome", "Código", "Categoria" });
        cmbBuscarPor.setPreferredSize(new Dimension(150, cmbBuscarPor.getPreferredSize().height));
        gbc.gridx = 1; gbc.weightx = 0;
        painel.add(cmbBuscarPor, gbc);

        // "Valor"
        gbc.gridx = 2; gbc.weightx = 0;
        painel.add(new JLabel("Valor"), gbc);

        txtValor = new JTextField();
        gbc.gridx = 3; gbc.weightx = 1;
        painel.add(txtValor, gbc);

        // Botão "Buscar"
        btnBuscar = new JButton("Buscar");
        gbc.gridx = 4; gbc.weightx = 0;
        painel.add(btnBuscar, gbc);

        // ---------------------------------------------------------------
        // LÓGICA DE BUSCA (apenas comentada / não implementada)
        // ---------------------------------------------------------------
        btnBuscar.addActionListener(e -> {
            /*
             * Exemplo de fluxo que seria implementado aqui:
             *
             * 1. Obter os critérios de busca informados pelo usuário:
             *
             *    String campoBusca = (String) cmbBuscarPor.getSelectedItem();
             *    String valorBusca = txtValor.getText();
             *
             * 2. Validar se o valor de busca foi informado:
             *
             *    if (valorBusca.isBlank()) {
             *        JOptionPane.showMessageDialog(this,
             *            "Informe um valor para buscar.",
             *            "Validação", JOptionPane.WARNING_MESSAGE);
             *        return;
             *    }
             *
             * 3. Consultar os produtos de acordo com o critério escolhido:
             *
             *    ProdutoDAO dao = new ProdutoDAO();
             *    List<Produto> produtos = dao.buscarPor(campoBusca, valorBusca);
             *
             * 4. Limpar a tabela e preencher com os resultados encontrados:
             *
             *    modeloResultados.setRowCount(0);
             *    for (Produto p : produtos) {
             *        modeloResultados.addRow(new Object[] {
             *            p.getCodigo(),
             *            p.getNome(),
             *            p.getCategoria(),
             *            formatarMoeda(p.getPrecoUnitario()),
             *            p.getEstoqueAtual(),
             *            "Visualizar" // a coluna "Ação" é renderizada com um botão
             *        });
             *    }
             *
             * 5. Caso nenhum resultado seja encontrado, exibir mensagem:
             *
             *    if (produtos.isEmpty()) {
             *        JOptionPane.showMessageDialog(this,
             *            "Nenhum produto encontrado.",
             *            "Busca", JOptionPane.INFORMATION_MESSAGE);
             *    }
             */
        });

        return painel;
    }

    // ---------------------------------------------------------------
    // Painel "Resultados" (tabela)
    // ---------------------------------------------------------------
    private JPanel criarPainelResultados() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBorder(new TitledBorder("Resultados"));

        String[] colunas = { "Código", "Nome", "Categoria", "Preço unitário", "Estoque atual", "Ação" };

        modeloResultados = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Apenas a coluna "Ação" possui um componente interativo (botão).
                return column == 5;
            }
        };

        // Dados de exemplo (mock) apenas para representar visualmente a tela,
        // iguais aos exibidos na imagem de referência.
        modeloResultados.addRow(new Object[] { "2001", "Caderno Universitário", "Papelaria", "R$ 18,50", "120", "Visualizar" });
        modeloResultados.addRow(new Object[] { "2002", "Livro de Matemática Básica", "Educação", "R$ 45,00", "35", "Visualizar" });
        modeloResultados.addRow(new Object[] { "2003", "Jogo de Xadrez", "Lazer", "R$ 32,90", "18", "Visualizar" });
        modeloResultados.addRow(new Object[] { "2004", "Quebra-cabeça 500 peças", "Entretenimento", "R$ 27,40", "22", "Visualizar" });

        /*
         * Em uma implementação real, as linhas acima não seriam fixas.
         * A tabela seria populada dinamicamente a partir do resultado
         * da busca (ver lógica comentada em btnBuscar) ou, ao abrir a
         * tela pela primeira vez, com a listagem completa de produtos:
         *
         * ProdutoDAO dao = new ProdutoDAO();
         * List<Produto> produtos = dao.listarTodos();
         * for (Produto p : produtos) {
         *     modeloResultados.addRow(new Object[] { ... });
         * }
         */

        tabelaResultados = new JTable(modeloResultados);
        tabelaResultados.setRowHeight(28);
        tabelaResultados.getTableHeader().setReorderingAllowed(false);

        // Coluna "Ação" exibida com um botão "Visualizar"
        tabelaResultados.getColumnModel().getColumn(5).setCellRenderer(new BotaoAcaoRenderer());
        tabelaResultados.getColumnModel().getColumn(5).setCellEditor(new BotaoAcaoEditor());
        tabelaResultados.getColumnModel().getColumn(5).setMaxWidth(110);

        JScrollPane scroll = new JScrollPane(tabelaResultados);
        scroll.setPreferredSize(new Dimension(700, 280));
        painel.add(scroll, BorderLayout.CENTER);

        return painel;
    }

    // ---------------------------------------------------------------
    // Painel de botões inferiores "Novo" / "Visualizar" / "Fechar"
    // ---------------------------------------------------------------
    private JPanel criarPainelBotoes() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        btnNovo = new JButton("Novo");
        btnVisualizar = new JButton("Visualizar");
        btnFechar = new JButton("Fechar");

        // ---------------------------------------------------------------
        // LÓGICA DO BOTÃO "Novo" (apenas comentada)
        // ---------------------------------------------------------------
        btnNovo.addActionListener(e -> {
            /*
             * Exemplo de fluxo que seria implementado aqui:
             *
             * Abrir a tela de cadastro de produto (TelaProduto) em modo
             * de inclusão, sem nenhum dado pré-carregado:
             *
             * TelaProduto telaProduto = new TelaProduto();
             * telaProduto.setVisible(true);
             */
        });

        // ---------------------------------------------------------------
        // LÓGICA DO BOTÃO "Visualizar" (apenas comentada)
        // ---------------------------------------------------------------
        btnVisualizar.addActionListener(e -> {
            /*
             * Exemplo de fluxo que seria implementado aqui:
             *
             * 1. Verificar se alguma linha da tabela está selecionada:
             *
             *    int linhaSelecionada = tabelaResultados.getSelectedRow();
             *    if (linhaSelecionada == -1) {
             *        JOptionPane.showMessageDialog(this,
             *            "Selecione um produto na tabela.",
             *            "Visualizar", JOptionPane.WARNING_MESSAGE);
             *        return;
             *    }
             *
             * 2. Obter o código do produto selecionado e buscar os dados
             *    completos:
             *
             *    String codigo = (String) modeloResultados.getValueAt(linhaSelecionada, 0);
             *    ProdutoDAO dao = new ProdutoDAO();
             *    Produto produto = dao.buscarPorCodigo(codigo);
             *
             * 3. Abrir a tela de cadastro de produto (TelaProduto) em modo
             *    de visualização/edição, já preenchida:
             *
             *    TelaProduto telaProduto = new TelaProduto();
             *    telaProduto.carregarProduto(produto);
             *    telaProduto.setVisible(true);
             */
        });

        // ---------------------------------------------------------------
        // LÓGICA DO BOTÃO "Fechar" (apenas comentada)
        // ---------------------------------------------------------------
        btnFechar.addActionListener(e -> {
            /*
             * Exemplo de fluxo que seria implementado aqui:
             *
             * Simplesmente fechar a tela atual, sem necessidade de
             * confirmação, pois não há dados a serem salvos nesta tela.
             */
            dispose();
        });

        painel.add(btnNovo);
        painel.add(btnVisualizar);
        painel.add(btnFechar);
        return painel;
    }

    // ---------------------------------------------------------------
    // Renderer/Editor para exibir um botão "Visualizar" dentro da
    // célula da coluna "Ação" (apenas visual - sem lógica de negócio)
    // ---------------------------------------------------------------
    private class BotaoAcaoRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        BotaoAcaoRenderer() {
            setText("Visualizar");
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    private class BotaoAcaoEditor extends javax.swing.AbstractCellEditor
            implements javax.swing.table.TableCellEditor {
        private final JButton botao = new JButton("Visualizar");
        private int linhaAtual;

        BotaoAcaoEditor() {
            botao.addActionListener(e -> {
                /*
                 * Exemplo de fluxo que seria implementado aqui:
                 *
                 * Ao clicar no botão "Visualizar" de uma linha específica
                 * da tabela, abrir a tela de cadastro de produto já
                 * preenchida com os dados daquela linha:
                 *
                 * String codigo = (String) modeloResultados.getValueAt(linhaAtual, 0);
                 * ProdutoDAO dao = new ProdutoDAO();
                 * Produto produto = dao.buscarPorCodigo(codigo);
                 *
                 * TelaProduto telaProduto = new TelaProduto();
                 * telaProduto.carregarProduto(produto);
                 * telaProduto.setVisible(true);
                 */
                fireEditingStopped();
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            linhaAtual = row;
            return botao;
        }

        @Override
        public Object getCellEditorValue() {
            return "Visualizar";
        }
    }

    public static void main(String[] args) {
        // Look and feel padrão do sistema (apenas estético)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // Mantém o look and feel padrão do Swing em caso de falha
        }

        SwingUtilities.invokeLater(() -> {
            TelaBuscaProdutoView tela = new TelaBuscaProdutoView();
            tela.setVisible(true);
        });
    }
}
