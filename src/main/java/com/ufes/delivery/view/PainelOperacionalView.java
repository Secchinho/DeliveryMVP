package com.ufes.delivery.view;

import java.util.function.IntConsumer;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.*;
import java.awt.*;

public class PainelOperacionalView extends JFrame implements IPainelOperacionalView {

    // ----- Componentes que o Presenter precisa ler/manipular -----
    private JMenuItem miNovoPedido;
    private JMenuItem miBuscarProdutos;
    private JMenuItem miNovoProduto;
    private JMenuItem miMovimentacaoEstoque;
    private JMenuItem miNovoCliente;
    private JMenuItem miBuscarClientes;

    private JLabel lblDataOperacao;

    private JLabel lblPedidosDoDia;
    private JLabel lblNovos;
    private JLabel lblAguardandoPagamento;
    private JLabel lblEmPreparo;
    private JLabel lblAguardandoEntrega;
    private JLabel lblEmTransito;
    private JLabel lblEntreguesHoje;

    private JTable tabelaPedidos;
    private DefaultTableModel modeloTabelaPedidos;
    private IntConsumer acaoVisualizarPedidoListener;

    private JLabel lblUsuario;
    private JLabel lblLogin;
    private JLabel lblTipo;

    public PainelOperacionalView() {
        super("Início");
        setSize(760, 580);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        setJMenuBar(criarMenuBar());

        JPanel conteudo = new JPanel();
        conteudo.setLayout(new BorderLayout());
        conteudo.setBorder(new EmptyBorder(10, 10, 10, 10));

        conteudo.add(criarTopo(), BorderLayout.NORTH);

        JPanel centro = new JPanel(new BorderLayout());
        centro.add(criarPainelCards(), BorderLayout.NORTH);
        centro.add(criarPainelLista(), BorderLayout.CENTER);
        conteudo.add(centro, BorderLayout.CENTER);

        add(conteudo, BorderLayout.CENTER);
        add(criarBarraStatus(), BorderLayout.SOUTH);
    }

    private JMenuBar criarMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menuOperacao = new JMenu("Operação");

        miNovoPedido = new JMenuItem("Novo pedido");
        miBuscarProdutos = new JMenuItem("Buscar produtos");
        miNovoProduto = new JMenuItem("Novo produto");
        miMovimentacaoEstoque = new JMenuItem("Movimentação de estoque");
        miNovoCliente = new JMenuItem("Novo cliente");
        miBuscarClientes = new JMenuItem("Buscar clientes");

        menuOperacao.add(miNovoPedido);
        menuOperacao.add(miBuscarProdutos);
        menuOperacao.add(miNovoProduto);
        menuOperacao.add(miMovimentacaoEstoque);
        menuOperacao.add(miNovoCliente);
        menuOperacao.add(miBuscarClientes);

        menuBar.add(menuOperacao);
        return menuBar;
    }

    private JPanel criarTopo() {
        JPanel painel = new JPanel();
        painel.setLayout(new FlowLayout(FlowLayout.CENTER));
        painel.setBorder(new EmptyBorder(0, 0, 10, 0));

        // O valor exibido aqui é definido pelo Presenter via exibirDataOperacao(...)
        lblDataOperacao = new JLabel("Data de operação: ");
        lblDataOperacao.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblDataOperacao.setForeground(new Color(20, 40, 90));
        lblDataOperacao.setBorder(new CompoundBorder(
                new LineBorder(new Color(120, 140, 180), 1),
                new EmptyBorder(8, 20, 8, 20)
        ));
        lblDataOperacao.setOpaque(true);
        lblDataOperacao.setBackground(new Color(235, 240, 250));

        painel.add(lblDataOperacao);
        return painel;
    }

    private JPanel criarPainelCards() {
        JPanel painelCards = new JPanel();
        painelCards.setLayout(new BoxLayout(painelCards, BoxLayout.Y_AXIS));
        painelCards.setBorder(new EmptyBorder(0, 0, 10, 0));

        lblPedidosDoDia = new JLabel("");
        lblNovos = new JLabel("");
        lblAguardandoPagamento = new JLabel("");
        lblEmPreparo = new JLabel("");
        lblAguardandoEntrega = new JLabel("");
        lblEmTransito = new JLabel("");
        lblEntreguesHoje = new JLabel("");

        JPanel linha1 = new JPanel(new GridLayout(1, 4, 10, 10));
        linha1.add(criarCard("Pedidos do dia", lblPedidosDoDia));
        linha1.add(criarCard("Novos", lblNovos));
        linha1.add(criarCard("Aguardando pagamento", lblAguardandoPagamento));
        linha1.add(criarCard("Em preparo", lblEmPreparo));

        JPanel linha2 = new JPanel(new GridLayout(1, 4, 10, 10));
        linha2.add(criarCard("Aguardando entrega", lblAguardandoEntrega));
        linha2.add(criarCard("Em trânsito", lblEmTransito));
        linha2.add(criarCard("Entregues hoje", lblEntreguesHoje));
        linha2.add(new JPanel() {{ setOpaque(false); }}); // espaço vazio p/ alinhar 3 cards

        painelCards.add(linha1);
        painelCards.add(Box.createVerticalStrut(10));
        painelCards.add(linha2);

        return painelCards;
    }

    private JPanel criarCard(String titulo, JLabel lblValor) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(190, 190, 190), 1),
                new EmptyBorder(10, 10, 10, 10)
        ));
        card.setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitulo.setFont(new Font("SansSerif", Font.PLAIN, 13));

        lblValor.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblValor.setFont(new Font("SansSerif", Font.BOLD, 26));

        card.add(lblTitulo);
        card.add(Box.createVerticalStrut(6));
        card.add(lblValor);

        return card;
    }

    private JPanel criarPainelLista() {
        JPanel painel = new JPanel(new BorderLayout());

        JLabel titulo = new JLabel("Lista de Pedidos");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 14));
        titulo.setBorder(new EmptyBorder(5, 0, 5, 0));
        painel.add(titulo, BorderLayout.NORTH);

        String[] colunas = {"Pedido", "Cliente", "Data do pedido", "Data de conclusão",
                "Estado do pedido", "Valor total", "Ação"};

        // As linhas são preenchidas dinamicamente pelo Presenter via atualizarListaPedidos(...)
        modeloTabelaPedidos = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 6;
            }
        };

        tabelaPedidos = new JTable(modeloTabelaPedidos);
        tabelaPedidos.setRowHeight(28);
        tabelaPedidos.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        tabelaPedidos.setFont(new Font("SansSerif", Font.PLAIN, 12));

        tabelaPedidos.getColumn("Ação").setCellRenderer((table, value, isSelected, hasFocus, row, column) -> {
            JButton btn = new JButton("Visualizar");
            btn.setFont(new Font("SansSerif", Font.PLAIN, 11));
            return btn;
        });
        tabelaPedidos.getColumn("Ação").setCellEditor(new javax.swing.DefaultCellEditor(new JCheckBox()) {
            private final JButton botao = new JButton("Visualizar");
            {
                botao.setFont(new Font("SansSerif", Font.PLAIN, 11));
                botao.addActionListener(e -> {
                    fireEditingStopped();
                    if (acaoVisualizarPedidoListener != null) {
                        int linha = tabelaPedidos.getEditingRow();
                        if (linha >= 0) {
                            acaoVisualizarPedidoListener.accept(linha);
                        }
                    }
                });
            }
            @Override
            public Component getTableCellEditorComponent(JTable t, Object value, boolean isSelected, int row, int col) {
                return botao;
            }
            @Override
            public Object getCellEditorValue() {
                return "Visualizar";
            }
        });

        JScrollPane scroll = new JScrollPane(tabelaPedidos);
        painel.add(scroll, BorderLayout.CENTER);

        return painel;
    }

    private JPanel criarBarraStatus() {
        JPanel barra = new JPanel(new BorderLayout());
        barra.setBorder(new CompoundBorder(new MatteBorder(1, 0, 0, 0, Color.GRAY), new EmptyBorder(4, 10, 4, 10)));

        // Os valores exibidos aqui são definidos pelo Presenter via exibirSessaoUsuario(...)
        lblUsuario = new JLabel("Usuário logado: ");
        lblLogin = new JLabel("Login: ");
        lblTipo = new JLabel("Tipo: ");

        JPanel painelCentro = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelCentro.add(lblLogin);

        barra.add(lblUsuario, BorderLayout.WEST);
        barra.add(painelCentro, BorderLayout.CENTER);
        barra.add(lblTipo, BorderLayout.EAST);

        return barra;
    }

    // =====================================================================
    // IMPLEMENTAÇÃO DA INTERFACE IPainelOperacionalView
    // =====================================================================

    @Override
    public JFrame getJanelaPrincipal() {
        return this;
    }

    @Override
    public void fecharTela() {
        this.dispose();
    }

    @Override
    public JMenuItem getMenuNovoPedido() {
        return miNovoPedido;
    }

    @Override
    public JMenuItem getMenuBuscarProdutos() {
        return miBuscarProdutos;
    }

    @Override
    public JMenuItem getMenuNovoProduto() {
        return miNovoProduto;
    }

    @Override
    public JMenuItem getMenuMovimentacaoEstoque() {
        return miMovimentacaoEstoque;
    }

    @Override
    public JMenuItem getMenuNovoCliente() {
        return miNovoCliente;
    }

    @Override
    public JMenuItem getMenuBuscarClientes() {
        return miBuscarClientes;
    }

    @Override
    public void exibirDataOperacao(String dataOperacao) {
        lblDataOperacao.setText("Data de operação: " + (dataOperacao != null ? dataOperacao : ""));
    }

    @Override
    public void exibirIndicadores(int pedidosDoDia, int novos, int aguardandoPagamento, int emPreparo,
                                   int aguardandoEntrega, int emTransito, int entreguesHoje) {
        lblPedidosDoDia.setText(String.valueOf(pedidosDoDia));
        lblNovos.setText(String.valueOf(novos));
        lblAguardandoPagamento.setText(String.valueOf(aguardandoPagamento));
        lblEmPreparo.setText(String.valueOf(emPreparo));
        lblAguardandoEntrega.setText(String.valueOf(aguardandoEntrega));
        lblEmTransito.setText(String.valueOf(emTransito));
        lblEntreguesHoje.setText(String.valueOf(entreguesHoje));
    }

    @Override
    public JTable getTabelaPedidos() {
        return tabelaPedidos;
    }

    @Override
    public void atualizarListaPedidos(Object[][] linhas) {
        modeloTabelaPedidos.setRowCount(0);
        if (linhas == null) {
            return;
        }
        for (Object[] linha : linhas) {
            Object[] linhaCompleta = new Object[7];
            System.arraycopy(linha, 0, linhaCompleta, 0, Math.min(6, linha.length));
            linhaCompleta[6] = "Visualizar";
            modeloTabelaPedidos.addRow(linhaCompleta);
        }
    }

    @Override
    public void setAcaoVisualizarPedidoListener(IntConsumer callback) {
        this.acaoVisualizarPedidoListener = callback;
    }

    @Override
    public void exibirSessaoUsuario(String nomeUsuario, String dataHoraLogin, String tipoUsuario) {
        lblUsuario.setText("Usuário logado: " + (nomeUsuario != null ? nomeUsuario : ""));
        lblLogin.setText("Login: " + (dataHoraLogin != null ? dataHoraLogin : ""));
        lblTipo.setText("Tipo: " + (tipoUsuario != null ? tipoUsuario : ""));
    }

    @Override
    public void exibirMensagem(String mensagem, String titulo, int tipoMensagem) {
        JOptionPane.showMessageDialog(this, mensagem, titulo, tipoMensagem);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new PainelOperacionalView().setVisible(true);
        });
    }
}