/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.ufes.delivery.view;

/**
 *
 * @author fabricio
 */
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class TelaPainelOperacionalView extends JFrame {

    public TelaPainelOperacionalView() {
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

        String[] itens = {
                "Novo pedido", "Buscar produtos", "Novo produto",
                "Movimentação de estoque", "Novo cliente", "Buscar clientes"
        };
        for (String item : itens) {
            JMenuItem mi = new JMenuItem(item);
            menuOperacao.add(mi);
        }
        menuBar.add(menuOperacao);
        return menuBar;
    }

    private JPanel criarTopo() {
        JPanel painel = new JPanel();
        painel.setLayout(new FlowLayout(FlowLayout.CENTER));
        painel.setBorder(new EmptyBorder(0, 0, 10, 0));

        // TODO: Consultar no sistema a data de operação atual
        // Exemplo: String dataOperacao = servicoOperacao.obterDataOperacao();
        String dataOperacao = ""; // valor deve vir do sistema

        JLabel label = new JLabel("Data de operação: " + dataOperacao);
        label.setFont(new Font("SansSerif", Font.BOLD, 16));
        label.setForeground(new Color(20, 40, 90));
        label.setBorder(new CompoundBorder(
                new LineBorder(new Color(120, 140, 180), 1),
                new EmptyBorder(8, 20, 8, 20)
        ));
        label.setOpaque(true);
        label.setBackground(new Color(235, 240, 250));

        painel.add(label);
        return painel;
    }

    private JPanel criarPainelCards() {
        // TODO: Consultar no sistema os indicadores/contadores de pedidos
        // Exemplo: DashboardDTO dash = servicoPedido.obterIndicadores(dataOperacao);
        // Os valores abaixo (rótulo + quantidade) devem ser substituídos pelos dados retornados

        String[][] dadosLinha1 = {
                {"Pedidos do dia", ""},
                {"Novos", ""},
                {"Aguardando pagamento", ""},
                {"Em preparo", ""}
        };
        String[][] dadosLinha2 = {
                {"Aguardando entrega", ""},
                {"Em trânsito", ""},
                {"Entregues hoje", ""}
        };

        JPanel painelCards = new JPanel();
        painelCards.setLayout(new BoxLayout(painelCards, BoxLayout.Y_AXIS));
        painelCards.setBorder(new EmptyBorder(0, 0, 10, 0));

        JPanel linha1 = new JPanel(new GridLayout(1, 4, 10, 10));
        for (String[] d : dadosLinha1) linha1.add(criarCard(d[0], d[1]));

        JPanel linha2 = new JPanel(new GridLayout(1, 4, 10, 10));
        for (String[] d : dadosLinha2) linha2.add(criarCard(d[0], d[1]));
        linha2.add(new JPanel() {{ setOpaque(false); }}); // espaço vazio p/ alinhar 3 cards

        painelCards.add(linha1);
        painelCards.add(Box.createVerticalStrut(10));
        painelCards.add(linha2);

        return painelCards;
    }

    private JPanel criarCard(String titulo, String valor) {
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

        JLabel lblValor = new JLabel(valor);
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

        // TODO: Consultar no sistema a lista de pedidos do dia
        // Exemplo: List<PedidoDTO> pedidos = servicoPedido.listarPedidos(dataOperacao);
        //
        // Para cada pedido retornado, montar uma linha assim:
        //   Object[] linha = {
        //       pedido.getNumero(),
        //       pedido.getNomeCliente(),
        //       pedido.getDataPedidoFormatada(),
        //       pedido.getDataConclusaoFormatada(), // ou "-" se ainda não concluído
        //       pedido.getEstadoDescricao(),
        //       pedido.getValorTotalFormatado(),
        //       "Visualizar"
        //   };
        //   model.addRow(linha);

        Object[][] dados = {
                // linhas devem ser preenchidas dinamicamente a partir do sistema
        };

        DefaultTableModel model = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 6;
            }
        };
        for (Object[] linha : dados) {
            Object[] linhaCompleta = new Object[7];
            System.arraycopy(linha, 0, linhaCompleta, 0, 6);
            linhaCompleta[6] = "Visualizar";
            model.addRow(linhaCompleta);
        }

        JTable tabela = new JTable(model);
        tabela.setRowHeight(28);
        tabela.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        tabela.setFont(new Font("SansSerif", Font.PLAIN, 12));

        tabela.getColumn("Ação").setCellRenderer((table, value, isSelected, hasFocus, row, column) -> {
            JButton btn = new JButton("Visualizar");
            btn.setFont(new Font("SansSerif", Font.PLAIN, 11));
            return btn;
        });
        tabela.getColumn("Ação").setCellEditor(new javax.swing.DefaultCellEditor(new JCheckBox()) {
            private final JButton botao = new JButton("Visualizar");
            {
                botao.setFont(new Font("SansSerif", Font.PLAIN, 11));
                botao.addActionListener(e -> fireEditingStopped());
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

        JScrollPane scroll = new JScrollPane(tabela);
        painel.add(scroll, BorderLayout.CENTER);

        return painel;
    }

    private JPanel criarBarraStatus() {
        JPanel barra = new JPanel(new BorderLayout());
        barra.setBorder(new CompoundBorder(new MatteBorder(1, 0, 0, 0, Color.GRAY), new EmptyBorder(4, 10, 4, 10)));

        // TODO: Consultar no sistema os dados da sessão do usuário logado
        // Exemplo: SessaoUsuarioDTO sessao = servicoAutenticacao.obterSessaoAtual();
        String nomeUsuario = ""; // sessao.getNomeUsuario()
        String dataHoraLogin = ""; // sessao.getDataHoraLoginFormatada()
        String tipoUsuario = ""; // sessao.getTipoUsuario()

        JLabel usuario = new JLabel("Usuário logado: " + nomeUsuario);
        JLabel login = new JLabel("Login: " + dataHoraLogin);
        JLabel tipo = new JLabel("Tipo: " + tipoUsuario);

        JPanel painelCentro = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelCentro.add(login);

        barra.add(usuario, BorderLayout.WEST);
        barra.add(painelCentro, BorderLayout.CENTER);
        barra.add(tipo, BorderLayout.EAST);

        return barra;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new TelaPainelOperacionalView().setVisible(true);
        });
    }
}