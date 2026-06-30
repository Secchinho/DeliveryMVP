package com.ufes.delivery.view;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class BuscarProdutoView extends JFrame implements IBuscarProdutoView {

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

    // ----- Atributo do Presenter (Comentado para uso futuro) -----
    // private BuscarProdutoPresenter presenter;

    public BuscarProdutoView() {
        super("Produtos");
        initComponents();
    }

    // =========================================================================
    // IMPLEMENTAÇÃO DA INTERFACE (IBuscarProdutoView)
    // =========================================================================

    @Override
    public JButton getBuscarButton() {
        return this.btnBuscar;
    }

    @Override
    public JButton getNovoButton() {
        return this.btnNovo;
    }

    @Override
    public JButton getVisualizarButton() {
        return this.btnVisualizar;
    }

    @Override
    public JButton getFecharButton() {
        return this.btnFechar;
    }

    @Override
    public JComboBox<String> getComboBuscarPor() {
        return this.cmbBuscarPor;
    }

    @Override
    public JFrame getJanelaPrincipal() {
        return this;
    }

    @Override
    public JTextField getTxtValor() {
        return this.txtValor;
    }

    @Override
    public JTable getTabelaResultados() {
        return this.tabelaResultados;
    }

    @Override
    public DefaultTableModel getModeloResultados() {
        return this.modeloResultados;
    }
    // =========================================================================
    // FIM DA IMPLEMENTAÇÃO DA INTERFACE
    // =========================================================================

    /**
     * Método para ligar a View ao Presenter.
     * Descomente e utilize quando criar a classe BuscarProdutoPresenter.
     */
    // public void setPresenter(BuscarProdutoPresenter presenter) {
    //     this.presenter = presenter;
    // }

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

    private JPanel criarPainelBusca() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBorder(new TitledBorder("Busca de Produtos"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        painel.add(new JLabel("Buscar por"), gbc);

        cmbBuscarPor = new JComboBox<>(new String[] { "Nome", "Código", "Categoria" });
        cmbBuscarPor.setPreferredSize(new Dimension(150, cmbBuscarPor.getPreferredSize().height));
        gbc.gridx = 1; gbc.weightx = 0;
        painel.add(cmbBuscarPor, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        painel.add(new JLabel("Valor"), gbc);

        txtValor = new JTextField();
        gbc.gridx = 3; gbc.weightx = 1;
        painel.add(txtValor, gbc);

        btnBuscar = new JButton("Buscar");
        gbc.gridx = 4; gbc.weightx = 0;
        painel.add(btnBuscar, gbc);

        // --- REMOVIDO: Listener do btnBuscar ---
        // A lógica será tratada no Presenter

        return painel;
    }

    private JPanel criarPainelResultados() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBorder(new TitledBorder("Resultados"));

        String[] colunas = { "Código", "Nome", "Categoria", "Preço unitário", "Estoque atual", "Ação" };

        modeloResultados = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };

        // --- REMOVIDO: Dados Mock (linhas de exemplo) ---
        // A tabela agora inicia vazia, pronta para ser populada pelo Presenter.

        tabelaResultados = new JTable(modeloResultados);
        tabelaResultados.setRowHeight(28);
        tabelaResultados.getTableHeader().setReorderingAllowed(false);

        tabelaResultados.getColumnModel().getColumn(5).setCellRenderer(new BotaoAcaoRenderer());
        tabelaResultados.getColumnModel().getColumn(5).setCellEditor(new BotaoAcaoEditor());
        tabelaResultados.getColumnModel().getColumn(5).setMaxWidth(110);

        JScrollPane scroll = new JScrollPane(tabelaResultados);
        scroll.setPreferredSize(new Dimension(700, 280));
        painel.add(scroll, BorderLayout.CENTER);

        return painel;
    }

    private JPanel criarPainelBotoes() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        btnNovo = new JButton("Novo");
        btnVisualizar = new JButton("Visualizar");
        btnFechar = new JButton("Fechar");

        // --- REMOVIDO: Listeners dos botões inferiores ---
        // As lógicas serão tratadas no Presenter

        painel.add(btnNovo);
        painel.add(btnVisualizar);
        painel.add(btnFechar);
        return painel;
    }

    // ---------------------------------------------------------------
    // Renderer/Editor (Mantidos, pois são puramente visuais)
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
                // --- REMOVIDO: Lógica de negócio dentro do botão da tabela ---
                // O Presenter deve recuperar o código do produto e decidir o que fazer.
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
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            BuscarProdutoView view = new BuscarProdutoView();
            view.setVisible(true);
        });
    }
}