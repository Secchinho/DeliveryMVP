package com.ufes.delivery.view;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Tela de Cadastro de Cliente
 * --------------------------------
 * Esta classe implementa apenas a parte VISUAL da tela "Cliente",
 * conforme a Figura 5 (Cadastro de cliente).
 *
 * Mantém:
 *  - Nome do cliente
 *  - CPF do cliente
 *  - Até 3 endereços de entrega (com opção de marcar um como Padrão)
 *
 * Toda a lógica de persistência (inserção/leitura de dados em banco,
 * arquivo, etc.) está comentada como sugestão de implementação,
 * mas não é executada.
 */
public class TelaClienteView extends JFrame {

    // ----- Componentes de dados do cliente -----
    private JTextField txtNome;
    private JTextField txtCpf;

    // ----- Componentes da tabela de endereços de entrega -----
    private JTable tabelaEnderecos;
    private DefaultTableModel modeloEnderecos;
    private ButtonGroup grupoPadrao; // garante que só um endereço seja "Padrão"

    // ----- Botões -----
    private JButton btnSalvar;
    private JButton btnCancelar;

    private static final int MAX_ENDERECOS = 3;

    public TelaClienteView() {
        super("Cliente");
        grupoPadrao = new ButtonGroup();
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

        painelCentro.add(criarPainelDadosCliente());
        painelCentro.add(Box.createVerticalStrut(15));
        painelCentro.add(criarPainelEnderecos());

        painelPrincipal.add(painelCentro, BorderLayout.CENTER);
        painelPrincipal.add(criarPainelBotoes(), BorderLayout.SOUTH);
    }

    // ---------------------------------------------------------------
    // Painel "Dados do Cliente" (Nome / CPF)
    // ---------------------------------------------------------------
    private JPanel criarPainelDadosCliente() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBorder(new TitledBorder("Dados do Cliente"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Linha Nome
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        painel.add(new JLabel("Nome"), gbc);

        txtNome = new JTextField();
        gbc.gridx = 1; gbc.weightx = 1;
        painel.add(txtNome, gbc);

        // Linha CPF
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        painel.add(new JLabel("CPF"), gbc);

        txtCpf = new JTextField();
        gbc.gridx = 1; gbc.weightx = 1;
        painel.add(txtCpf, gbc);

        return painel;
    }

    // ---------------------------------------------------------------
    // Painel "Endereços de Entrega" (tabela com até 3 linhas)
    // ---------------------------------------------------------------
    private JPanel criarPainelEnderecos() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBorder(new TitledBorder("Endereços de Entrega"));

        String[] colunas = {
            "Padrão", "Logradouro", "Número", "Complemento", "Bairro", "Cidade", "UF", "CEP"
        };

        // Modelo de tabela apenas visual - sem dados reais carregados
        modeloEnderecos = new DefaultTableModel(colunas, MAX_ENDERECOS) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Coluna "Padrão" (0) não é editável via célula de texto,
                // pois é representada por um JRadioButton customizado.
                return column != 0;
            }
        };

        tabelaEnderecos = new JTable(modeloEnderecos);
        tabelaEnderecos.setRowHeight(28);
        tabelaEnderecos.getTableHeader().setReorderingAllowed(false);

        // Configura a coluna "Padrão" para exibir radio buttons
        tabelaEnderecos.getColumnModel().getColumn(0).setMaxWidth(70);
        tabelaEnderecos.getColumnModel().getColumn(0).setCellRenderer(new RadioButtonRenderer());
        tabelaEnderecos.getColumnModel().getColumn(0).setCellEditor(new RadioButtonEditor());

        JScrollPane scroll = new JScrollPane(tabelaEnderecos);
        scroll.setPreferredSize(new Dimension(700, 130));
        painel.add(scroll, BorderLayout.CENTER);

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
             * 1. Validar campos obrigatórios (Nome, CPF).
             *
             *    String nome = txtNome.getText();
             *    String cpf  = txtCpf.getText();
             *
             *    if (nome.isBlank() || cpf.isBlank()) {
             *        JOptionPane.showMessageDialog(this,
             *            "Nome e CPF são obrigatórios.",
             *            "Validação", JOptionPane.WARNING_MESSAGE);
             *        return;
             *    }
             *
             * 2. Criar objeto Cliente:
             *
             *    Cliente cliente = new Cliente();
             *    cliente.setNome(nome);
             *    cliente.setCpf(cpf);
             *
             * 3. Percorrer as linhas da tabela "modeloEnderecos" e montar
             *    a lista de até 3 endereços de entrega:
             *
             *    List<Endereco> enderecos = new ArrayList<>();
             *    for (int i = 0; i < modeloEnderecos.getRowCount(); i++) {
             *        String logradouro  = (String) modeloEnderecos.getValueAt(i, 1);
             *        String numero      = (String) modeloEnderecos.getValueAt(i, 2);
             *        String complemento = (String) modeloEnderecos.getValueAt(i, 3);
             *        String bairro      = (String) modeloEnderecos.getValueAt(i, 4);
             *        String cidade      = (String) modeloEnderecos.getValueAt(i, 5);
             *        String uf          = (String) modeloEnderecos.getValueAt(i, 6);
             *        String cep         = (String) modeloEnderecos.getValueAt(i, 7);
             *
             *        if (logradouro != null && !logradouro.isBlank()) {
             *            Endereco endereco = new Endereco(logradouro, numero,
             *                complemento, bairro, cidade, uf, cep);
             *            endereco.setPadrao(isLinhaPadrao(i)); // verifica radio selecionado
             *            enderecos.add(endereco);
             *        }
             *    }
             *    cliente.setEnderecos(enderecos);
             *
             * 4. Persistir o cliente (banco de dados, arquivo, API, etc.):
             *
             *    ClienteDAO dao = new ClienteDAO();
             *    dao.salvar(cliente);
             *
             * 5. Exibir mensagem de sucesso e/ou fechar a tela:
             *
             *    JOptionPane.showMessageDialog(this, "Cliente salvo com sucesso!");
             *    dispose();
             */
        });

        // ---------------------------------------------------------------
        // LÓGICA DE LEITURA / CANCELAMENTO (apenas comentada)
        // ---------------------------------------------------------------
        btnCancelar.addActionListener(e -> {
            /*
             * Exemplo de fluxo que seria implementado aqui:
             *
             * 1. Perguntar se o usuário deseja realmente cancelar,
             *    descartando alterações não salvas:
             *
             *    int opcao = JOptionPane.showConfirmDialog(this,
             *        "Deseja cancelar e descartar as alterações?",
             *        "Cancelar", JOptionPane.YES_NO_OPTION);
             *
             * 2. Se confirmado, fechar a tela sem salvar:
             *
             *    if (opcao == JOptionPane.YES_OPTION) {
             *        dispose();
             *    }
             */
            dispose();
        });

        painel.add(btnSalvar);
        painel.add(btnCancelar);
        return painel;
    }

    /*
     * Exemplo de como seria a leitura/preenchimento inicial da tela
     * (ex.: ao abrir a tela para EDITAR um cliente já existente):
     *
     * public void carregarCliente(Cliente cliente) {
     *     txtNome.setText(cliente.getNome());
     *     txtCpf.setText(cliente.getCpf());
     *
     *     List<Endereco> enderecos = cliente.getEnderecos();
     *     for (int i = 0; i < enderecos.size() && i < MAX_ENDERECOS; i++) {
     *         Endereco end = enderecos.get(i);
     *         modeloEnderecos.setValueAt(end.getLogradouro(), i, 1);
     *         modeloEnderecos.setValueAt(end.getNumero(), i, 2);
     *         modeloEnderecos.setValueAt(end.getComplemento(), i, 3);
     *         modeloEnderecos.setValueAt(end.getBairro(), i, 4);
     *         modeloEnderecos.setValueAt(end.getCidade(), i, 5);
     *         modeloEnderecos.setValueAt(end.getUf(), i, 6);
     *         modeloEnderecos.setValueAt(end.getCep(), i, 7);
     *         if (end.isPadrao()) {
     *             // selecionar o radio button correspondente à linha i
     *         }
     *     }
     * }
     */

    // ---------------------------------------------------------------
    // Renderer/Editor para exibir um JRadioButton dentro da célula
    // da coluna "Padrão" (apenas visual - sem lógica de persistência)
    // ---------------------------------------------------------------
    private class RadioButtonRenderer extends JPanel implements javax.swing.table.TableCellRenderer {
        private final JRadioButton radio = new JRadioButton();

        RadioButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
            add(radio);
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            radio.setSelected(Boolean.TRUE.equals(value));
            setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            return this;
        }
    }

    private class RadioButtonEditor extends javax.swing.AbstractCellEditor
            implements javax.swing.table.TableCellEditor {
        private final JPanel painel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        private final JRadioButton radio = new JRadioButton();
        private int linhaAtual;

        RadioButtonEditor() {
            painel.add(radio);
            grupoPadrao.add(radio);
            radio.addActionListener(e -> {
                // Apenas visual: marca esta linha como "Padrão" e
                // automaticamente desmarca as demais (ButtonGroup já garante isso).
                modeloEnderecos.setValueAt(Boolean.TRUE, linhaAtual, 0);
                fireEditingStopped();
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            linhaAtual = row;
            radio.setSelected(Boolean.TRUE.equals(value));
            return painel;
        }

        @Override
        public Object getCellEditorValue() {
            return radio.isSelected();
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
            TelaClienteView tela = new TelaClienteView();
            tela.setVisible(true);
        });
    }
}