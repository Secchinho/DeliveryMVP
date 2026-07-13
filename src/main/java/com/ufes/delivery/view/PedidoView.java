package com.ufes.delivery.view;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class PedidoView extends JFrame implements IPedidoView {
    private JTextField txtCliente;
    private JButton btnNovoCliente;
    private JComboBox<String> cmbEnderecoEntrega;
    private JTable tabelaItens;
    private ItensTableModel modeloItens;
    private JTextField txtCupomDesconto;
    private JButton btnAplicarCupom;
    private JLabel lblTotalDescontosValor;
    private JLabel lblDescontoTaxaEntregaValor;
    private JLabel lblTaxaEntregaFinalValor;
    private JLabel lblTotalPedidoValor;
    private JButton btnPagar;
    private JButton btnCancelar;

    /**
     * Menu de contexto exibido ao clicar com o botão direito sobre uma linha
     * da tabela de itens (US09 cenário 4). Exposto pela interface para que o
     * presenter anexe o ActionListener ao item "Excluir".
     */
    private JPopupMenu menuContexto;
    private JMenuItem itemExcluir;


    public PedidoView() {
        super("Pedido");
        initComponents();
    }

    @Override
    public JButton getNovoClienteButton() {
        return this.btnNovoCliente;
    }

    @Override
    public JButton getPagarButton() {
        return this.btnPagar;
    }

    @Override
    public JButton getFecharButton() {
        return this.btnCancelar;
    }

    @Override
    public JButton getAplicarCupomButton() {
        return this.btnAplicarCupom;
    }

    @Override
    public JComboBox<String> getEnderecoComboBox() {
        return this.cmbEnderecoEntrega;
    }

    @Override
    public JFrame getJanelaPrincipal() {
        return this;
    }

    @Override
    public JTextField getTxtCliente() {
        return this.txtCliente;
    }

    @Override
    public JTextField getTxtCupomDesconto() {
        return this.txtCupomDesconto;
    }

    @Override
    public JTable getTabelaItens() {
        return this.tabelaItens;
    }

    @Override
    public JLabel getLblTotalDescontosValor() {
        return this.lblTotalDescontosValor;
    }

    @Override
    public JLabel getLblDescontoTaxaEntregaValor() {
        return this.lblDescontoTaxaEntregaValor;
    }

    @Override
    public JLabel getLblTaxaEntregaFinalValor() {
        return this.lblTaxaEntregaFinalValor;
    }

    @Override
    public JLabel getLblTotalPedidoValor() {
        return this.lblTotalPedidoValor;
    }

    @Override
    public JMenuItem getMenuItemExcluirItem() {
        return this.itemExcluir;
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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

    private JPanel criarPainelDadosPedido() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBorder(new TitledBorder("Dados do Pedido"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        painel.add(new JLabel("Cliente"), gbc);

        txtCliente = new JTextField();
        gbc.gridx = 1; gbc.weightx = 1;
        painel.add(txtCliente, gbc);

        btnNovoCliente = new JButton("Novo Cliente");
        gbc.gridx = 2; gbc.weightx = 0;
        painel.add(btnNovoCliente, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        painel.add(new JLabel("Endereço de entrega"), gbc);

        cmbEnderecoEntrega = new JComboBox<>();
        gbc.gridx = 1; gbc.gridwidth = 2; gbc.weightx = 1;
        painel.add(cmbEnderecoEntrega, gbc);

        return painel;
    }

    private JPanel criarPainelItens() {
        JPanel painel = new JPanel(new BorderLayout());

        String[] colunas = { "Categoria", "Item", "Preço unitário", "Quantidade", "Preço total" };
        modeloItens = new ItensTableModel(colunas, 0);

        tabelaItens = new JTable(modeloItens);
        tabelaItens.setRowHeight(26);
        tabelaItens.getTableHeader().setReorderingAllowed(false);
        tabelaItens.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Menu de contexto promovido a field da classe para que o presenter
        // possa anexar o ActionListener ao item "Excluir" via interface.
        menuContexto = new JPopupMenu();
        itemExcluir = new JMenuItem("Excluir");
        menuContexto.add(itemExcluir);

        tabelaItens.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
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

    private JPanel criarPainelTotais() {
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBorder(new EmptyBorder(10, 0, 0, 0));

        JPanel linhaCupom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
        linhaCupom.add(new JLabel("Cupom de desconto"));

        txtCupomDesconto = new JTextField(18);
        linhaCupom.add(txtCupomDesconto);

        btnAplicarCupom = new JButton("Aplicar");
        linhaCupom.add(btnAplicarCupom);

        JPanel linhasTotais = new JPanel();
        linhasTotais.setLayout(new BoxLayout(linhasTotais, BoxLayout.Y_AXIS));

        lblTotalDescontosValor = new JLabel("R$ 0,00", SwingConstants.RIGHT);
        lblDescontoTaxaEntregaValor = new JLabel("R$ 0,00", SwingConstants.RIGHT);
        lblTaxaEntregaFinalValor = new JLabel("R$ 0,00", SwingConstants.RIGHT);
        lblTotalPedidoValor = new JLabel("R$ 0,00", SwingConstants.RIGHT);

        linhasTotais.add(criarLinhaTotal("Total de descontos", lblTotalDescontosValor, false));
        linhasTotais.add(criarLinhaTotal("Desconto na taxa de entrega", lblDescontoTaxaEntregaValor, false));
        linhasTotais.add(criarLinhaTotal("Taxa de entrega final", lblTaxaEntregaFinalValor, false));
        linhasTotais.add(Box.createVerticalStrut(4));
        linhasTotais.add(criarLinhaTotal("Total do pedido", lblTotalPedidoValor, true));

        painel.add(linhaCupom);
        painel.add(linhasTotais);

        return painel;
    }

    private JPanel criarLinhaTotal(String rotulo, JLabel lblValor, boolean destaque) {
        JPanel linha = new JPanel(new BorderLayout());
        linha.setBorder(new EmptyBorder(2, 0, 2, 0));

        JLabel lblRotulo = new JLabel(rotulo);

        if (destaque) {
            Font fonteNegrito = lblRotulo.getFont().deriveFont(Font.BOLD);
            lblRotulo.setFont(fonteNegrito);
            lblValor.setFont(fonteNegrito);
        }

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

    private JPanel criarPainelBotoes() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        btnPagar = new JButton("Pagar");
        btnCancelar = new JButton("Cancelar");

        painel.add(btnPagar);
        painel.add(btnCancelar);
        return painel;
    }

    private class ItensTableModel extends DefaultTableModel {

        public ItensTableModel(String[] colunas, int linhas) {
            super(colunas, linhas);
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }

        public List<Object[]> getDadosItens() {
            List<Object[]> dados = new ArrayList<>();
            for (int i = 0; i < getRowCount(); i++) {
                String categoria = (String) getValueAt(i, 0);
                if (categoria != null && !categoria.trim().isEmpty()) {
                    Object[] linha = new Object[5];
                    linha[0] = categoria;
                    linha[1] = getValueAt(i, 1);
                    linha[2] = getValueAt(i, 2);
                    linha[3] = getValueAt(i, 3);
                    linha[4] = getValueAt(i, 4);
                    dados.add(linha);
                }
            }
            return dados;
        }
    }
}