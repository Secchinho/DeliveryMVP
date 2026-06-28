package com.ufes.delivery.view;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MovimentacaoEstoqueView extends JFrame implements IMovimentacaoEstoqueView {

    // ----- Componentes de busca de produtos -----
    private JTextField txtBuscarProduto;
    private JButton btnBuscar;
    private JTable tabelaProdutos;
    private DefaultTableModel modeloProdutos;
    private JButton btnSelecionar;

    // ----- Componentes "Produto Selecionado" -----
    private JTextField txtProdutoSelecionado;
    private JTextField txtQuantidadeAtual;

    // ----- Componentes "Movimentação" -----
    private JComboBox<String> cmbDataMovimentacao;
    private JComboBox<String> cmbTipoMovimentacao;
    private JTextField txtQuantidadeMovimentar;
    private JTextField txtMotivoAjuste;
    private JTextField txtEstoqueAposMovimentacao;
    private JTextField txtNotaFiscal;
    private JLabel lblAvisoPrevisualizacao;
    private JLabel lblAvisoRegras;

    // ----- Botões finais -----
    private JButton btnConfirmarMovimentacao;
    private JButton btnCancelar;

    // ----- Atributo do Presenter (Comentado para uso futuro) -----
    // private MovimentacaoEstoquePresenter presenter;

    public MovimentacaoEstoqueView() {
        super("Movimentação de Estoque");
        initComponents();
    }

    // =========================================================================
    // IMPLEMENTAÇÃO DA INTERFACE (IMovimentacaoEstoqueView)
    // =========================================================================

    @Override
    public String getTermoBuscaProduto() {
        return txtBuscarProduto.getText().trim();
    }

    @Override
    public String getProdutoSelecionadoCodigo() {
        int linha = tabelaProdutos.getSelectedRow();
        if (linha == -1) return null;
        return (String) modeloProdutos.getValueAt(linha, 0); // Coluna 0 é o Código
    }

    @Override
    public String getQuantidadeMovimentar() {
        return txtQuantidadeMovimentar.getText().trim();
    }

    @Override
    public String getMotivoAjuste() {
        return txtMotivoAjuste.getText().trim();
    }

    @Override
    public String getNotaFiscal() {
        return txtNotaFiscal.getText().trim();
    }

    @Override
    public String getTipoMovimentacaoSelecionado() {
        return (String) cmbTipoMovimentacao.getSelectedItem();
    }

    @Override
    public void setProdutoSelecionado(String nomeProduto) {
        this.txtProdutoSelecionado.setText(nomeProduto);
    }

    @Override
    public void setQuantidadeAtual(String quantidade) {
        this.txtQuantidadeAtual.setText(quantidade);
    }

    @Override
    public void setEstoqueAposMovimentacao(String quantidade) {
        this.txtEstoqueAposMovimentacao.setText(quantidade);
    }

    @Override
    public void adicionarLinhaTabela(Object[] linha) {
        modeloProdutos.addRow(linha);
    }

    @Override
    public void limparTabelaProdutos() {
        modeloProdutos.setRowCount(0);
    }

    @Override
    public JButton getBuscarButton() {
        return this.btnBuscar;
    }

    @Override
    public JButton getSelecionarButton() {
        return this.btnSelecionar;
    }

    @Override
    public JButton getConfirmarMovimentacaoButton() {
        return this.btnConfirmarMovimentacao;
    }

    @Override
    public JButton getCancelarButton() {
        return this.btnCancelar;
    }

    @Override
    public JComboBox<String> getTipoMovimentacaoComboBox() {
        return this.cmbTipoMovimentacao;
    }

    @Override
    public JTextField getNotaFiscalTextField() {
        return this.txtNotaFiscal;
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
    public JFrame getJanelaPrincipal() {
        return this;
    }

    // =========================================================================
    // FIM DA IMPLEMENTAÇÃO DA INTERFACE
    // =========================================================================

    /**
     * Método para ligar a View ao Presenter.
     * Descomente e utilize quando criar a classe MovimentacaoEstoquePresenter.
     */
    // public void setPresenter(MovimentacaoEstoquePresenter presenter) {
    //     this.presenter = presenter;
    // }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(770, 640);
        setMinimumSize(new Dimension(740, 600));
        setLocationRelativeTo(null);

        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(painelPrincipal);

        JPanel painelCentro = new JPanel();
        painelCentro.setLayout(new BoxLayout(painelCentro, BoxLayout.Y_AXIS));

        painelCentro.add(criarPainelBuscaProdutos());
        painelCentro.add(Box.createVerticalStrut(12));
        painelCentro.add(criarPainelProdutoSelecionado());
        painelCentro.add(Box.createVerticalStrut(12));
        painelCentro.add(criarPainelMovimentacao());

        painelPrincipal.add(painelCentro, BorderLayout.CENTER);
        painelPrincipal.add(criarPainelBotoes(), BorderLayout.SOUTH);
    }

    // ---------------------------------------------------------------
    // Painel "Busca de Produtos"
    // ---------------------------------------------------------------
    private JPanel criarPainelBuscaProdutos() {
        JPanel painel = new JPanel(new BorderLayout(8, 8));
        painel.setBorder(new TitledBorder("Busca de Produtos"));

        JPanel linhaBusca = new JPanel(new BorderLayout(8, 8));
        linhaBusca.add(new JLabel("Buscar produto"), BorderLayout.WEST);

        txtBuscarProduto = new JTextField();
        linhaBusca.add(txtBuscarProduto, BorderLayout.CENTER);

        btnBuscar = new JButton("Buscar");
        linhaBusca.add(btnBuscar, BorderLayout.EAST);

        String[] colunas = { "Código", "Produto", "Categoria", "Estoque atual" };
        modeloProdutos = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // --- REMOVIDO: Dados Mock ---
        // A tabela inicia vazia, o Presenter irá preencher.

        tabelaProdutos = new JTable(modeloProdutos);
        tabelaProdutos.setRowHeight(26);
        tabelaProdutos.getTableHeader().setReorderingAllowed(false);
        tabelaProdutos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(tabelaProdutos);
        scroll.setPreferredSize(new Dimension(700, 150));

        JPanel painelBotaoSelecionar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnSelecionar = new JButton("Selecionar");
        painelBotaoSelecionar.add(btnSelecionar);

        JPanel painelTabela = new JPanel(new BorderLayout());
        painelTabela.add(scroll, BorderLayout.CENTER);
        painelTabela.add(painelBotaoSelecionar, BorderLayout.SOUTH);

        painel.add(linhaBusca, BorderLayout.NORTH);
        painel.add(painelTabela, BorderLayout.CENTER);

        return painel;
    }

    // ---------------------------------------------------------------
    // Painel "Produto Selecionado" (somente leitura)
    // ---------------------------------------------------------------
    private JPanel criarPainelProdutoSelecionado() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBorder(new TitledBorder("Produto Selecionado"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        painel.add(new JLabel("Produto"), gbc);

        txtProdutoSelecionado = new JTextField();
        txtProdutoSelecionado.setEditable(false);
        gbc.gridx = 1; gbc.weightx = 1;
        painel.add(txtProdutoSelecionado, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        painel.add(new JLabel("Quantidade atual em estoque"), gbc);

        txtQuantidadeAtual = new JTextField();
        txtQuantidadeAtual.setEditable(false);
        gbc.gridx = 1; gbc.weightx = 1;
        painel.add(txtQuantidadeAtual, gbc);

        return painel;
    }

    // ---------------------------------------------------------------
    // Painel "Movimentação"
    // ---------------------------------------------------------------
    private JPanel criarPainelMovimentacao() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBorder(new TitledBorder("Movimentação"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Linha 1: Data (Desabilitada - será preenchida pelo sistema)
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        painel.add(new JLabel("Data da movimentação"), gbc);

        cmbDataMovimentacao = new JComboBox<>();
        cmbDataMovimentacao.setEditable(false);
        cmbDataMovimentacao.setEnabled(false); // O Presenter define a data atual
        cmbDataMovimentacao.setPreferredSize(new Dimension(140, cmbDataMovimentacao.getPreferredSize().height));
        gbc.gridx = 1; gbc.weightx = 0;
        painel.add(cmbDataMovimentacao, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        painel.add(new JLabel("Tipo de movimentação"), gbc);

        cmbTipoMovimentacao = new JComboBox<>(new String[] { "Entrada", "Saída", "Ajuste de estoque" });
        cmbTipoMovimentacao.setPreferredSize(new Dimension(180, cmbTipoMovimentacao.getPreferredSize().height));
        gbc.gridx = 3; gbc.weightx = 1;
        painel.add(cmbTipoMovimentacao, gbc);

        // Linha 2
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        painel.add(new JLabel("Quantidade a movimentar"), gbc);

        txtQuantidadeMovimentar = new JTextField();
        gbc.gridx = 1; gbc.weightx = 0;
        painel.add(txtQuantidadeMovimentar, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        painel.add(new JLabel("Motivo do ajuste"), gbc);

        txtMotivoAjuste = new JTextField();
        gbc.gridx = 3; gbc.weightx = 1;
        painel.add(txtMotivoAjuste, gbc);

        // Linha 3
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        painel.add(new JLabel("Estoque após movimentação (prévia)"), gbc);

        txtEstoqueAposMovimentacao = new JTextField();
        txtEstoqueAposMovimentacao.setEditable(false);
        gbc.gridx = 1; gbc.weightx = 0;
        painel.add(txtEstoqueAposMovimentacao, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        painel.add(new JLabel("Nota fiscal de entrada"), gbc);

        txtNotaFiscal = new JTextField("(Opcional)");
        txtNotaFiscal.setEnabled(false);
        gbc.gridx = 3; gbc.weightx = 1;
        painel.add(txtNotaFiscal, gbc);

        // Linha 4: Avisos
        lblAvisoPrevisualizacao = new JLabel(
            "Pré-visualização, a atualização definitiva ocorrerá após a confirmação da movimentação.");
        lblAvisoPrevisualizacao.setForeground(new Color(0, 70, 160));
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 4; gbc.weightx = 1;
        painel.add(lblAvisoPrevisualizacao, gbc);

        lblAvisoRegras = new JLabel(
            "Ajustes de estoque requerem motivo. Entradas requerem número da nota fiscal.");
        lblAvisoRegras.setForeground(new Color(0, 70, 160));
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 4; gbc.weightx = 1;
        painel.add(lblAvisoRegras, gbc);

        return painel;
    }

    // ---------------------------------------------------------------
    // Painel de botões "Confirmar movimentação" / "Cancelar"
    // ---------------------------------------------------------------
    private JPanel criarPainelBotoes() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        btnConfirmarMovimentacao = new JButton("Confirmar movimentação");
        btnCancelar = new JButton("Cancelar");

        // --- REMOVIDO: Listeners de botões ---
        // A lógica será tratada no Presenter.

        painel.add(btnConfirmarMovimentacao);
        painel.add(btnCancelar);
        return painel;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            MovimentacaoEstoqueView view = new MovimentacaoEstoqueView();
            view.setVisible(true);
        });
    }
}