package com.ufes.delivery.view;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ClienteView extends JFrame implements IClienteView {

    // ----- Componentes de dados do cliente -----
    private JTextField txtNome;
    private JTextField txtCpf;

    // ----- Componentes da tabela de endereços de entrega -----
    private JTable tabelaEnderecos;
    private DefaultTableModel modeloEnderecos;


    // ----- Botões -----
    private JButton btnSalvar;
    private JButton btnCancelar;

    private static final int MAX_ENDERECOS = 3;

    public ClienteView() {
        super("Cliente");
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
    public JFrame getJanelaPrincipal(){
        return this;
    }
    
    @Override
    public DefaultTableModel getModeloEnderecos() {
        return this.modeloEnderecos;
    }

    @Override
    public void exibirMensagem(String mensagem, String titulo, int tipo) {
        JOptionPane.showMessageDialog(this, mensagem, titulo, tipo);
    }

    // =========================================================================
    // FIM DA IMPLEMENTAÇÃO DA INTERFACE
    // =========================================================================

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
                return true; // Coluna 0 (Padrão) também precisa ser editável para o editor funcionar
            }
        };

        tabelaEnderecos = new JTable(modeloEnderecos);
        tabelaEnderecos.setRowHeight(28);
        tabelaEnderecos.getTableHeader().setReorderingAllowed(false);

        tabelaEnderecos.getColumnModel().getColumn(0).setMaxWidth(70);
        tabelaEnderecos.getColumnModel().getColumn(0).setCellRenderer(new RadioButtonRenderer());

        // MouseListener garante: (1) ativação com 1 clique, (2) desmarca outras linhas no modelo
        tabelaEnderecos.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = tabelaEnderecos.rowAtPoint(e.getPoint());
                int col = tabelaEnderecos.columnAtPoint(e.getPoint());
                if (col == 0 && row >= 0) {
                    for (int i = 0; i < modeloEnderecos.getRowCount(); i++) {
                        modeloEnderecos.setValueAt(i == row, i, 0);
                    }
                }
            }
        });

        JScrollPane scroll = new JScrollPane(tabelaEnderecos);
        scroll.setPreferredSize(new Dimension(700, 130));
        painel.add(scroll, BorderLayout.CENTER);

        return painel;
    }

    private JPanel criarPainelBotoes() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        btnSalvar = new JButton("Salvar");
        btnCancelar = new JButton("Cancelar");
        
        painel.add(btnSalvar);
        painel.add(btnCancelar);
        return painel;
    }

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
}