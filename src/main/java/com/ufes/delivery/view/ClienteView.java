package com.ufes.delivery.view;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteView extends JFrame implements IClienteView {

    // ----- Componentes de dados do cliente -----
    private JTextField txtNome;
    private JTextField txtCpf;

    // ----- Componentes da tabela de endereços de entrega -----
    private JTable tabelaEnderecos;
    private DefaultTableModel modeloEnderecos;
    private ButtonGroup grupoPadrao;

    // ----- Botões -----
    private JButton btnSalvar;
    private JButton btnCancelar;

    private static final int MAX_ENDERECOS = 3;
    
    //private ClientePresenter presenter;

    public ClienteView() {
        super("Cliente");
        grupoPadrao = new ButtonGroup();
        initComponents();
    }

    // =========================================================================
    // IMPLEMENTAÇÃO DA INTERFACE (IClienteView)
    // =========================================================================

    @Override
    public JTextField getCampoNome() {
        return this.txtNome;
    }

    @Override
    public JTextField getCampoCpf() {
        return this.txtCpf;
    }

    @Override
    public JTable getTabelaEndereco() {
        return this.tabelaEnderecos;
    }

    @Override
    public JButton getBotaoSalvar() {
        return this.btnSalvar;
    }

    @Override
    public JButton getBotaoCancelar() {
        return this.btnCancelar;
    }

    @Override
    public String getNome() {
        return txtNome.getText().trim();
    }

    @Override
    public String getCpf() {
        return txtCpf.getText().trim();
    }

    @Override
    public List<Object[]> getDadosEnderecos() {
        List<Object[]> dados = new ArrayList<>();
        for (int i = 0; i < modeloEnderecos.getRowCount(); i++) {
            // Verifica se a linha tem pelo menos um endereço preenchido
            String logradouro = (String) modeloEnderecos.getValueAt(i, 1);
            if (logradouro != null && !logradouro.isBlank()) {
                Object[] linha = new Object[8];
                linha[0] = modeloEnderecos.getValueAt(i, 0); // Padrão (Boolean)
                linha[1] = logradouro;
                linha[2] = modeloEnderecos.getValueAt(i, 2); // Número
                linha[3] = modeloEnderecos.getValueAt(i, 3); // Complemento
                linha[4] = modeloEnderecos.getValueAt(i, 4); // Bairro
                linha[5] = modeloEnderecos.getValueAt(i, 5); // Cidade
                linha[6] = modeloEnderecos.getValueAt(i, 6); // UF
                linha[7] = modeloEnderecos.getValueAt(i, 7); // CEP
                dados.add(linha);
            }
        }
        return dados;
    }

    @Override
    public void setNome(String nome) {
        this.txtNome.setText(nome);
    }

    @Override
    public void setCpf(String cpf) {
        this.txtCpf.setText(cpf);
    }

    @Override
    public void setEnderecosTabela(List<Object[]> enderecos) {
        // Limpa a tabela primeiro
        for (int i = 0; i < MAX_ENDERECOS; i++) {
            for (int j = 0; j < modeloEnderecos.getColumnCount(); j++) {
                modeloEnderecos.setValueAt(null, i, j);
            }
        }
        // Preenche com os dados recebidos
        for (int i = 0; i < enderecos.size() && i < MAX_ENDERECOS; i++) {
            Object[] end = enderecos.get(i);
            for (int j = 0; j < end.length && j < modeloEnderecos.getColumnCount(); j++) {
                modeloEnderecos.setValueAt(end[j], i, j);
            }
        }
    }

    @Override
    public void exibirMensagem(String mensagem, String titulo, int tipoMensagem) {
        JOptionPane.showMessageDialog(this, mensagem, titulo, tipoMensagem);
    }

    @Override
    public void fecharTela() {
        this.dispose();
    }

    @Override
    public JFrame getJanelaPrincipal(){
        return this;
    }

    // =========================================================================
    // FIM DA IMPLEMENTAÇÃO DA INTERFACE
    // =========================================================================

    /*  PRESENTER
    public void setPresenter(ClientePresenter presenter) {
        this.presenter = presenter;
    }
    */

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

    private JPanel criarPainelDadosCliente() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBorder(new TitledBorder("Dados do Cliente"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        painel.add(new JLabel("Nome"), gbc);

        txtNome = new JTextField();
        gbc.gridx = 1; gbc.weightx = 1;
        painel.add(txtNome, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        painel.add(new JLabel("CPF"), gbc);

        txtCpf = new JTextField();
        gbc.gridx = 1; gbc.weightx = 1;
        painel.add(txtCpf, gbc);

        return painel;
    }

    private JPanel criarPainelEnderecos() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBorder(new TitledBorder("Endereços de Entrega"));

        String[] colunas = {
            "Padrão", "Logradouro", "Número", "Complemento", "Bairro", "Cidade", "UF", "CEP"
        };

        modeloEnderecos = new DefaultTableModel(colunas, MAX_ENDERECOS) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0;
            }
        };

        tabelaEnderecos = new JTable(modeloEnderecos);
        tabelaEnderecos.setRowHeight(28);
        tabelaEnderecos.getTableHeader().setReorderingAllowed(false);

        tabelaEnderecos.getColumnModel().getColumn(0).setMaxWidth(70);
        tabelaEnderecos.getColumnModel().getColumn(0).setCellRenderer(new RadioButtonRenderer());
        tabelaEnderecos.getColumnModel().getColumn(0).setCellEditor(new RadioButtonEditor());

        JScrollPane scroll = new JScrollPane(tabelaEnderecos);
        scroll.setPreferredSize(new Dimension(700, 130));
        painel.add(scroll, BorderLayout.CENTER);

        return painel;
    }

    private JPanel criarPainelBotoes() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        btnSalvar = new JButton("Salvar");
        btnCancelar = new JButton("Cancelar");

        // IMPORTANTE: No MVP, a View NÃO pode ter ActionListeners.
        // A lógica será implementada no ClientePresenter.
        // O Presenter vai acessar getBotaoSalvar() e fazer:
        // view.getBotaoSalvar().addActionListener(e -> presenter.salvarCliente());
        
        painel.add(btnSalvar);
        painel.add(btnCancelar);
        return painel;
    }

    // ---------------------------------------------------------------
    // Renderer/Editor (Mantido intacto, pois é puramente visual)
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
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            ClienteView view = new ClienteView();
            view.setVisible(true);
        });
    }
}