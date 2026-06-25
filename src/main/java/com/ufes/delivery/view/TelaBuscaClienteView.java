package com.ufes.delivery.view;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Tela de Busca de Clientes.
 *
 * Java 21 / Swing.
 *
 * Esta classe contém SOMENTE a parte visual (layout dos componentes).
 * Toda a lógica de negócio/persistência (busca por Nome/CPF, abrir Novo,
 * abrir Visualizar) está comentada como indicação de onde deve ser
 * implementada.
 */
public class TelaBuscaClienteView extends JFrame {

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

    public TelaBuscaClienteView() {
        super("Clientes");
        initComponents();
        carregarDadosExemplo(); // apenas para a tabela não ficar vazia visualmente
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

        // ------------------------------------------------------------------
        // TODO: implementar ação de busca, ex:
        //
        // btnBuscar.addActionListener(e -> buscarClientes());
        // ------------------------------------------------------------------

        return painel;
    }

    private JPanel criarPainelResultados() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBorder(BorderFactory.createTitledBorder("Resultados"));

        String[] colunas = {"Nome", "CPF"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Resultados são apenas para visualização/seleção
                return false;
            }
        };

        tabelaResultados = new JTable(tableModel);
        tabelaResultados.setRowHeight(26);
        tabelaResultados.setFillsViewportHeight(true);
        tabelaResultados.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

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

        // ------------------------------------------------------------------
        // TODO: implementar as ações dos botões, ex:
        //
        // btnNovo.addActionListener(e -> novoCliente());
        // btnVisualizar.addActionListener(e -> visualizarClienteSelecionado());
        // btnFechar.addActionListener(e -> dispose());
        // ------------------------------------------------------------------

        return painel;
    }

    // =====================================================================
    // Ações da tela (lógica a implementar)
    // =====================================================================

    // private void buscarClientes() {
    //     String atributo = (String) comboBuscarPor.getSelectedItem(); // "Nome" ou "CPF"
    //     String valor = txtValor.getText().trim();
    //     // TODO: buscar clientes (ex.: via DAO/Service) conforme o atributo escolhido
    //     // List<Cliente> clientes = atributo.equals("Nome")
    //     //         ? clienteService.buscarPorNome(valor)
    //     //         : clienteService.buscarPorCpf(valor);
    //     // preencherTabela(clientes);
    // }

    // private void preencherTabela(List<Cliente> clientes) {
    //     // TODO: limpar e popular tableModel com os clientes encontrados
    //     // tableModel.setRowCount(0);
    //     // for (Cliente c : clientes) {
    //     //     tableModel.addRow(new Object[]{c.getNome(), c.getCpfFormatado()});
    //     // }
    // }

    // private void novoCliente() {
    //     // TODO: abrir tela/diálogo de cadastro de novo cliente e persistir
    //     // TelaCadastroCliente tela = new TelaCadastroCliente(this);
    //     // tela.setVisible(true);
    //     // if (tela.isConfirmado()) {
    //     //     clienteService.salvar(tela.getClienteCriado());
    //     //     buscarClientes(); // ou adicionar a linha diretamente na tabela
    //     // }
    // }

    // private void visualizarClienteSelecionado() {
    //     // TODO: obter o cliente selecionado na tabela e abrir tela de detalhes
    //     // int linha = tabelaResultados.getSelectedRow();
    //     // if (linha == -1) {
    //     //     JOptionPane.showMessageDialog(this, "Selecione um cliente.", "Atenção", JOptionPane.WARNING_MESSAGE);
    //     //     return;
    //     // }
    //     // String cpf = (String) tableModel.getValueAt(linha, 1);
    //     // Cliente cliente = clienteService.buscarPorCpf(cpf);
    //     // TelaDetalheCliente tela = new TelaDetalheCliente(this, cliente);
    //     // tela.setVisible(true);
    // }

    // =====================================================================
    // Dados de exemplo (apenas para exibir a tela; substituir pela busca
    // real ao implementar a lógica)
    // =====================================================================
    private void carregarDadosExemplo() {
        tableModel.addRow(new Object[]{"Fulano de Tal", "000.000.000-00"});
        tableModel.addRow(new Object[]{"Fulano da Silva", "111.111.111-11"});
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }
            new TelaBuscaClienteView().setVisible(true);
        });
    }
}
