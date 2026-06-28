package com.ufes.delivery.view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.List;

public class ProdutoView extends JFrame implements IProdutoView {

    // ----- Componentes de dados do produto -----
    private JTextField txtCodigo;
    private JTextField txtNome;
    private JComboBox<String> cmbCategoria;
    private JTextField txtPrecoUnitario;
    private JTextField txtQuantidadeInicial;

    // ----- Botões -----
    private JButton btnSalvar;
    private JButton btnCancelar;

    // ----- Atributo do Presenter (Comentado para uso futuro) -----
    // private ProdutoPresenter presenter;

    public ProdutoView() {
        super("Produto");
        initComponents();
    }

    // =========================================================================
    // IMPLEMENTAÇÃO DA INTERFACE (IProdutoView)
    // =========================================================================

    @Override
    public String getCodigo() {
        return txtCodigo.getText().trim();
    }

    @Override
    public String getNome() {
        return txtNome.getText().trim();
    }

    @Override
    public String getCategoriaSelecionada() {
        return (String) cmbCategoria.getSelectedItem();
    }

    @Override
    public String getPrecoUnitario() {
        return txtPrecoUnitario.getText().trim();
    }

    @Override
    public String getQuantidadeInicial() {
        return txtQuantidadeInicial.getText().trim();
    }

    @Override
    public void setCodigo(String codigo) {
        this.txtCodigo.setText(codigo);
    }

    @Override
    public void setNome(String nome) {
        this.txtNome.setText(nome);
    }

    @Override
    public void setPrecoUnitario(String preco) {
        this.txtPrecoUnitario.setText(preco);
    }

    @Override
    public void setQuantidadeInicial(String quantidade) {
        this.txtQuantidadeInicial.setText(quantidade);
    }

    @Override
    public void setCategorias(List<String> categorias) {
        cmbCategoria.removeAllItems();
        if (categorias != null) {
            for (String cat : categorias) {
                cmbCategoria.addItem(cat);
            }
        }
    }

    @Override
    public void selecionarCategoria(String categoria) {
        cmbCategoria.setSelectedItem(categoria);
    }

    @Override
    public JButton getSalvarButton() {
        return this.btnSalvar;
    }

    @Override
    public JButton getFecharButton() {
        return this.btnCancelar;
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
     * Descomente e utilize quando criar a classe ProdutoPresenter.
     */
    // public void setPresenter(ProdutoPresenter presenter) {
    //     this.presenter = presenter;
    // }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 560);
        setMinimumSize(new Dimension(650, 520));
        setLocationRelativeTo(null);

        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(painelPrincipal);

        painelPrincipal.add(criarPainelDadosProduto(), BorderLayout.NORTH);
        painelPrincipal.add(criarPainelBotoes(), BorderLayout.SOUTH);
    }

    private JPanel criarPainelDadosProduto() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBorder(new TitledBorder("Dados do Produto"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Linha Código
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        painel.add(new JLabel("Código"), gbc);

        txtCodigo = new JTextField();
        txtCodigo.setPreferredSize(new Dimension(150, txtCodigo.getPreferredSize().height));
        gbc.gridx = 1; gbc.weightx = 0;
        painel.add(txtCodigo, gbc);

        gbc.gridx = 2; gbc.weightx = 1;
        painel.add(Box.createHorizontalGlue(), gbc);

        // Linha Nome
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        painel.add(new JLabel("Nome"), gbc);

        txtNome = new JTextField();
        gbc.gridx = 1; gbc.gridwidth = 2; gbc.weightx = 1;
        painel.add(txtNome, gbc);
        gbc.gridwidth = 1;

        // Linha Categoria
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        painel.add(new JLabel("Categoria"), gbc);

        cmbCategoria = new JComboBox<>();

        cmbCategoria.setPreferredSize(new Dimension(200, cmbCategoria.getPreferredSize().height));
        
        gbc.gridx = 1; gbc.weightx = 0;
        painel.add(cmbCategoria, gbc);

        gbc.gridx = 2; gbc.weightx = 1;
        painel.add(Box.createHorizontalGlue(), gbc);

        // Linha Preço unitário
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        painel.add(new JLabel("Preço unitário"), gbc);

        txtPrecoUnitario = new JTextField();
        txtPrecoUnitario.setPreferredSize(new Dimension(150, txtPrecoUnitario.getPreferredSize().height));
        gbc.gridx = 1; gbc.weightx = 0;
        painel.add(txtPrecoUnitario, gbc);

        gbc.gridx = 2; gbc.weightx = 1;
        painel.add(Box.createHorizontalGlue(), gbc);

        // Linha Quantidade inicial em estoque
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0;
        painel.add(new JLabel("Quantidade inicial em estoque"), gbc);

        txtQuantidadeInicial = new JTextField();
        txtQuantidadeInicial.setPreferredSize(new Dimension(150, txtQuantidadeInicial.getPreferredSize().height));
        gbc.gridx = 1; gbc.weightx = 0;
        painel.add(txtQuantidadeInicial, gbc);

        gbc.gridx = 2; gbc.weightx = 1;
        painel.add(Box.createHorizontalGlue(), gbc);

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

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            ProdutoView view = new ProdutoView();
            view.setVisible(true);
        });
    }
}