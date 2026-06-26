/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.ufes.delivery.view;

import com.ufes.delivery.model.Usuario;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

/**
 * Tela de gestão de usuários e autorizações.
 * Aberta a partir do botão "Cadastrar Usuário" da TelaLoginView.
 *
 * @author raphael
 */
public class TelaUsuariosView extends JFrame {

    private static final String[] PERFIS = {"Atendente", "Administrador"};

    private List<Usuario> todosUsuarios;
    private UsuariosTableModel tableModel;

    // Componentes visuais
    private JTextField nomeBuscaTxt;
    private JTable usuariosTable;
    private JButton autorizarBtn;
    private JButton desautorizarBtn;
    private JButton excluirBtn;
    private JButton novoBtn;
    private JButton fecharBtn;

    /**
     * Creates new form TelaUsuariosView
     */
    public TelaUsuariosView() {
        // TODO: Aqui você deve carregar a lista de usuários do seu banco de dados ou repositório
        // Exemplo: this.todosUsuarios = usuarioService.buscarTodos();
        this.todosUsuarios = new ArrayList<>(); // Inicializa vazio (sem dados de exemplo)

        initComponents();
        this.tableModel = new UsuariosTableModel(todosUsuarios);
        usuariosTable.setModel(tableModel);
        configurarColunaPerfil();
        usuariosTable.setRowHeight(24);

        setTitle("Usuários");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }

    // =========================================================================
    // MÉTODO DE CARREGAMENTO DE DADOS (Teste opcional)
    // =========================================================================
    /**
     * Método para carregar dados de exemplo. Descomente no 'main' se quiser
     * ver a tela preenchida para testes, ou utilize sua lógica real de banco aqui.
     */
    // public void carregarDadosExemplo() {
    //     // TODO: Substitua este bloco pela sua lógica real de carregamento de dados
    //     this.todosUsuarios.clear();
    //     
    //     // Exemplo de usuários (apenas para visualização, remova ao conectar com o banco)
    //     Usuario adminmaster = new Usuario("Administrador Master", "adminmaster", "123456");
    //     adminmaster.setTipo(1);
    //     adminmaster.setAutorizado(true);
    //     adminmaster.setSituacao("Autorizado");
    //     todosUsuarios.add(adminmaster);
    //
    //     Usuario atendente01 = new Usuario("Carlos Atendente", "atendente01", "123456");
    //     atendente01.setTipo(0);
    //     atendente01.setAutorizado(true);
    //     atendente01.setSituacao("Autorizado");
    //     todosUsuarios.add(atendente01);
    //     
    //     tableModel.filtrarPorNome(""); // Atualiza a tabela
    // }

    private void configurarColunaPerfil() {
        TableColumn colunaPerfil = usuariosTable.getColumnModel().getColumn(UsuariosTableModel.COL_PERFIL);
        JComboBox<String> comboPerfil = new JComboBox<>(PERFIS);
        colunaPerfil.setCellEditor(new javax.swing.DefaultCellEditor(comboPerfil));
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        // Painel de Busca
        JPanel buscaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buscaPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Busca de Usuários"));

        JLabel nomeBuscaLbl = new JLabel("Nome");
        nomeBuscaTxt = new JTextField(25);
        JButton buscarBtn = new JButton("Buscar");

        buscaPanel.add(nomeBuscaLbl);
        buscaPanel.add(nomeBuscaTxt);
        buscaPanel.add(buscarBtn);

        // Painel de Usuários (Tabela)
        JPanel usuariosPanel = new JPanel(new BorderLayout());
        usuariosPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Usuários"));

        usuariosTable = new JTable();
        JScrollPane usuariosScroll = new JScrollPane(usuariosTable);
        usuariosPanel.add(usuariosScroll, BorderLayout.CENTER);

        // Painel de Botões
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));

        autorizarBtn = new JButton("Autorizar");
        desautorizarBtn = new JButton("Desautorizar");
        excluirBtn = new JButton("Excluir");
        novoBtn = new JButton("Novo");
        fecharBtn = new JButton("Fechar");

        botoesPanel.add(autorizarBtn);
        botoesPanel.add(desautorizarBtn);
        botoesPanel.add(excluirBtn);
        botoesPanel.add(novoBtn);
        botoesPanel.add(fecharBtn);

        // Adicionando ao Frame
        add(buscaPanel, BorderLayout.NORTH);
        add(usuariosPanel, BorderLayout.CENTER);
        add(botoesPanel, BorderLayout.SOUTH);

        // Ações dos Botões
        buscarBtn.addActionListener(evt -> buscarBtnActionPerformed());
        autorizarBtn.addActionListener(evt -> autorizarBtnActionPerformed());
        desautorizarBtn.addActionListener(evt -> desautorizarBtnActionPerformed());
        excluirBtn.addActionListener(evt -> excluirBtnActionPerformed());
        novoBtn.addActionListener(evt -> novoBtnActionPerformed());
        fecharBtn.addActionListener(evt -> fecharBtnActionPerformed());
    }

    private void buscarBtnActionPerformed() {
        String filtro = nomeBuscaTxt.getText() == null ? "" : nomeBuscaTxt.getText().trim();
        tableModel.filtrarPorNome(filtro);
    }

    private void autorizarBtnActionPerformed() {
        List<Usuario> selecionados = tableModel.getUsuariosSelecionados();
        if (selecionados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione ao menos um usuário na coluna \"Sel.\".",
                    "Autorizar", JOptionPane.WARNING_MESSAGE);
            return;
        }
        for (Usuario usuario : selecionados) {
            usuario.setAutorizado(true);
            usuario.setSituacao("Autorizado");
            // TODO: Persistir a alteração no banco de dados
            // Exemplo: usuarioService.atualizar(usuario);
        }
        tableModel.fireTableDataChanged();
    }

    private void desautorizarBtnActionPerformed() {
        List<Usuario> selecionados = tableModel.getUsuariosSelecionados();
        if (selecionados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione ao menos um usuário na coluna \"Sel.\".",
                    "Desautorizar", JOptionPane.WARNING_MESSAGE);
            return;
        }
        for (Usuario usuario : selecionados) {
            usuario.setAutorizado(false);
            usuario.setSituacao("Não autorizado");
            // TODO: Persistir a alteração no banco de dados
            // Exemplo: usuarioService.atualizar(usuario);
        }
        tableModel.fireTableDataChanged();
    }

    private void excluirBtnActionPerformed() {
        List<Usuario> selecionados = tableModel.getUsuariosSelecionados();
        if (selecionados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione ao menos um usuário na coluna \"Sel.\".",
                    "Excluir", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirma = JOptionPane.showConfirmDialog(this,
                "Deseja realmente excluir " + selecionados.size() + " usuário(s)?",
                "Confirmar exclusão", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            todosUsuarios.removeAll(selecionados);
            // TODO: Persistir a exclusão no banco de dados
            // Exemplo: usuarioService.deletar(selecionados);
            tableModel.filtrarPorNome(nomeBuscaTxt.getText());
        }
    }

    private void novoBtnActionPerformed() {
        JTextField nomeTxt = new JTextField();
        JTextField userTxt = new JTextField();
        JTextField senhaTxt = new JTextField();
        Object[] campos = {
            "Nome:", nomeTxt,
            "Nome de usuário:", userTxt,
            "Senha:", senhaTxt
        };
        int opcao = JOptionPane.showConfirmDialog(this, campos, "Novo Usuário", JOptionPane.OK_CANCEL_OPTION);
        if (opcao == JOptionPane.OK_OPTION) {
            String nome = nomeTxt.getText().trim();
            String userName = userTxt.getText().trim();
            String senha = senhaTxt.getText().trim();
            if (nome.isEmpty() || userName.isEmpty() || senha.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos.", "Novo Usuário",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            Usuario novo = new Usuario(nome, userName, senha);
            novo.setSituacao("Pendente");
            todosUsuarios.add(novo);
            // TODO: Persistir o novo usuário no banco de dados
            // Exemplo: usuarioService.salvar(novo);

            nomeBuscaTxt.setText("");
            tableModel.filtrarPorNome("");
        }
    }

    private void fecharBtnActionPerformed() {
        this.dispose();
    }

    /**
     * Modelo de tabela que liga a lista de {@link Usuario} à JTable.
     * Adaptado para os getters e setters da sua classe Usuario.
     */
    private static class UsuariosTableModel extends AbstractTableModel {

        static final int COL_SEL = 0;
        static final int COL_USERNAME = 1;
        static final int COL_NOME = 2;
        static final int COL_AUTORIZADO = 3;
        static final int COL_PERFIL = 4;
        static final int COL_SITUACAO = 5;

        private final String[] colunas = {"Sel.", "Nome de usuário", "Nome", "Autorizado", "Perfil", "Situação"};
        private final List<Usuario> todos;
        private List<Usuario> exibidos;
        private final List<Usuario> selecionados = new ArrayList<>();

        UsuariosTableModel(List<Usuario> todos) {
            this.todos = todos;
            this.exibidos = new ArrayList<>(todos);
        }

        void filtrarPorNome(String filtro) {
            selecionados.clear();
            String alvo = filtro == null ? "" : filtro.trim().toLowerCase();
            exibidos = new ArrayList<>();
            for (Usuario usuario : todos) {
                if (alvo.isEmpty()
                        || usuario.getNome().toLowerCase().contains(alvo)
                        || usuario.getUserName().toLowerCase().contains(alvo)) {
                    exibidos.add(usuario);
                }
            }
            fireTableDataChanged();
        }

        List<Usuario> getUsuariosSelecionados() {
            return new ArrayList<>(selecionados);
        }

        @Override
        public int getRowCount() {
            return exibidos.size();
        }

        @Override
        public int getColumnCount() {
            return colunas.length;
        }

        @Override
        public String getColumnName(int column) {
            return colunas[column];
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            switch (columnIndex) {
                case COL_SEL:
                case COL_AUTORIZADO:
                    return Boolean.class;
                default:
                    return String.class;
            }
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex == COL_SEL || columnIndex == COL_AUTORIZADO || columnIndex == COL_PERFIL;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Usuario usuario = exibidos.get(rowIndex);
            switch (columnIndex) {
                case COL_SEL:
                    return selecionados.contains(usuario);
                case COL_USERNAME:
                    return usuario.getUserName();
                case COL_NOME:
                    return usuario.getNome();
                case COL_AUTORIZADO:
                    return usuario.isAutorizado();
                case COL_PERFIL:
                    return (usuario.getTipo() == 1) ? "Administrador" : "Atendente";
                case COL_SITUACAO:
                    return usuario.getSituacao();
                default:
                    return null;
            }
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            Usuario usuario = exibidos.get(rowIndex);
            switch (columnIndex) {
                case COL_SEL:
                    boolean marcado = Boolean.TRUE.equals(aValue);
                    if (marcado) {
                        if (!selecionados.contains(usuario)) {
                            selecionados.add(usuario);
                        }
                    } else {
                        selecionados.remove(usuario);
                    }
                    break;
                case COL_AUTORIZADO:
                    boolean autorizado = Boolean.TRUE.equals(aValue);
                    usuario.setAutorizado(autorizado);
                    usuario.setSituacao(autorizado ? "Autorizado" : "Não autorizado");
                    break;
                case COL_PERFIL:
                    String perfilSelecionado = String.valueOf(aValue);
                    usuario.setTipo(perfilSelecionado.equals("Administrador") ? 1 : 0);
                    break;
                default:
                    break;
            }
            fireTableRowsUpdated(rowIndex, rowIndex);
        }
    }

    // ======================================================================
    // MAIN PARA TESTAR A TELA
    // ======================================================================
    public static void main(String[] args) {
        // Tenta usar o visual padrão do sistema operacional
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaUsuariosView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        SwingUtilities.invokeLater(() -> {
            TelaUsuariosView tela = new TelaUsuariosView();
            // Descomente a linha abaixo se quiser ver a tela com dados de exemplo para testes visuais:
            // tela.carregarDadosExemplo(); 
            tela.setVisible(true);
        });
    }
}