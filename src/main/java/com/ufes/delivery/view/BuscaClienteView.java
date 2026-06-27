package com.ufes.delivery.view;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class BuscaClienteView extends JFrame implements IBuscarClienteView {

    // Atributos disponíveis para busca
    private static final String[] ATRIBUTOS_BUSCA = {"Nome", "CPF"};

    private JComboBox<String> comboBuscarPor;
    private JTextField txtValor;
    private JButton btnBuscar;

    private JTable tabelaResultados;
    private DefaultTableModel tableModel;

    private JButton btnNovo;
    private JButton btnVisualizar;
    private JButton btnFechar;
    
    // Referência para o Presenter
    //private BuscarClientePresenter presenter;

    // =========================================================================
    // IMPLEMENTAÇÃO DA INTERFACE (IBuscarClienteView)
    // =========================================================================

    @Override
    public String getValorBusca() {
        return txtValor.getText().trim();
    }

    @Override
    public String getAtributoBusca() {
        return (String) comboBuscarPor.getSelectedItem();
    }

    @Override
    public String getCpfClienteSelecionado() {
        int linha = tabelaResultados.getSelectedRow();
        if (linha == -1) {
            return null;
        }
        // Supondo que o CPF seja a coluna 1 (índice 1)
        return (String) tableModel.getValueAt(linha, 1);
    }

    @Override
    public void exibirClientes(List<Object[]> dadosClientes) {
        // Limpa a tabela atual
        tableModel.setRowCount(0);
        
        // Adiciona os novos dados
        for (Object[] linha : dadosClientes) {
            tableModel.addRow(linha);
        }
    }

    @Override
    public void exibirMensagem(String mensagem, String titulo, int tipo) {
        JOptionPane.showMessageDialog(this, mensagem, titulo, tipo);
    }

    @Override
    public JFrame getJanelaPrincipal() {
        return this;
    }

    @Override
    public JButton getBuscarClienteButton() {
        return btnBuscar;
    }

    @Override
    public JButton getNovoClienteButton() {
        return btnNovo;
    }

    @Override
    public JButton getVisualizarClienteButton() {
        return btnVisualizar;
    }

    @Override
    public JButton getFecharButton() {
        return btnFechar;
    }
    
    // =========================================================================
    // FIM DA IMPLEMENTAÇÃO DA INTERFACE
    // =========================================================================

    public BuscaClienteView() {
        super("Clientes");
        initComponents();
        
        // Nota: NÃO carregamos dados de exemplo aqui. O Presenter iniciará a busca.
        // Ou, se quiser, pode chamar presenter.iniciar() aqui após o setPresenter.
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(760, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setContentPane(painelPrincipal);

        painelPrincipal.add(criarPainelBusca(), BorderLayout.NORTH);
        painelPrincipal.add(criarPainelResultados(), BorderLayout.CENTER);
        painelPrincipal.add(criarPainelBotoes(), BorderLayout.SOUTH);
    }

    private JPanel criarPainelBusca() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createTitledBorder("Busca de Clientes"));

        JPanel linha = new JPanel(new GridBagLayout());
        linha.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 0, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // "Buscar por" + combo
        gbc.gridx = 0;
        gbc.gridy = 0;
        linha.add(new JLabel("Buscar por"), gbc);

        comboBuscarPor = new JComboBox<>(ATRIBUTOS_BUSCA);
        gbc.gridx = 1;
        gbc.weightx = 0;
        linha.add(comboBuscarPor, gbc);

        // "Valor" + campo de texto (expande)
        gbc.gridx = 2;
        gbc.weightx = 0;
        linha.add(new JLabel("Valor"), gbc);

        txtValor = new JTextField();
        gbc.gridx = 3;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        linha.add(txtValor, gbc);

        // Botão Buscar
        btnBuscar = new JButton("Buscar");
        gbc.gridx = 4;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 0, 0, 0);
        linha.add(btnBuscar, gbc);

        painel.add(linha, BorderLayout.CENTER);

        // REMOVIDO: A lógica de ação foi removida daqui. 
        // O Presenter vai adicionar o listener externamente.

        return painel;
    }

    private JPanel criarPainelResultados() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBorder(BorderFactory.createTitledBorder("Resultados"));

        String[] colunas = {"Nome", "CPF"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaResultados = new JTable(tableModel);
        tabelaResultados.setRowHeight(26);
        tabelaResultados.setFillsViewportHeight(true);
        tabelaResultados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        DefaultTableCellRenderer rendererCentralizado = new DefaultTableCellRenderer();
        rendererCentralizado.setHorizontalAlignment(SwingConstants.CENTER);
        tabelaResultados.getColumnModel().getColumn(1).setCellRenderer(rendererCentralizado);

        JScrollPane scroll = new JScrollPane(tabelaResultados);
        painel.add(scroll, BorderLayout.CENTER);

        return painel;
    }

    private JPanel criarPainelBotoes() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        btnNovo = new JButton("Novo");
        btnVisualizar = new JButton("Visualizar");
        btnFechar = new JButton("Fechar");

        painel.add(btnNovo);
        painel.add(btnVisualizar);
        painel.add(btnFechar);

        // REMOVIDO: A lógica de ação foi removida daqui.

        return painel;
    }

    /*Método para ligar a View ao Presenter
    public void setPresenter(BuscarClientePresenter presenter) {
        this.presenter = presenter;
    }
    */

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }
            
            // FLUXO DE CRIAÇÃO NO MVP:
            BuscaClienteView view = new BuscaClienteView();
            //BuscarClientePresenter presenter = new BuscarClientePresenter(view);
            //view.setPresenter(presenter);
            
            // O Presenter configura os eventos e exibe os dados iniciais
            //presenter.iniciar();
            
            view.setVisible(true);
        });
    }
}