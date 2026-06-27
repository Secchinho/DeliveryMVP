package com.ufes.delivery.view;

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

import com.ufes.delivery.model.Usuario;

public class GerenciarUsuariosView extends JFrame implements IGerenciarUsuariosView {

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
    
    // Referência para o Presenter
    //private IGerenciarUsuariosPresenter presenter; 

    // =========================================================================
    // IMPLEMENTAÇÃO DA INTERFACE (IGerenciarUsuariosView)
    // =========================================================================
    
    @Override
    public JTextField getNomeBuscaTxt() {
        return nomeBuscaTxt;
    }

    @Override
    public JTable getUsuariosTable() {
        return usuariosTable;
    }

    @Override
    public JButton getAutorizarBtn() {
        return autorizarBtn;
    }

    @Override
    public JButton getDesautorizarBtn() {
        return desautorizarBtn;
    }

    @Override
    public JButton getExcluirBtn() {
        return excluirBtn;
    }

    @Override
    public JButton getNovoBtn() {
        return novoBtn;
    }

    @Override
    public JButton getFecharBtn() {
        return fecharBtn;
    }

    @Override
    public List<Usuario> getUsuariosSelecionados() {
        return tableModel.getUsuariosSelecionados();
    }

    @Override
    public void atualizarTabela(List<Usuario> usuarios) {
        this.todosUsuarios = usuarios;
        this.tableModel = new UsuariosTableModel(todosUsuarios);
        usuariosTable.setModel(tableModel);
        configurarColunaPerfil();
        tableModel.fireTableDataChanged();
    }

    @Override
    public void exibirMensagem(String mensagem, String titulo, int tipoMensagem) {
        JOptionPane.showMessageDialog(this, mensagem, titulo, tipoMensagem);
    }

    @Override
    public void abrirTelaCadastro() {
        CadastrarUsuarioView telaNovo = new CadastrarUsuarioView();
        telaNovo.setVisible(true);
    }

    @Override
    public void fecharTela() {
        this.dispose();
    }
    
    // =========================================================================
    // FIM DA IMPLEMENTAÇÃO DA INTERFACE
    // =========================================================================

    public GerenciarUsuariosView() {
        this.todosUsuarios = new ArrayList<>(); 
        initComponents();
        this.tableModel = new UsuariosTableModel(todosUsuarios);
        usuariosTable.setModel(tableModel);
        configurarColunaPerfil();
        usuariosTable.setRowHeight(24);

        setTitle("Gerenciar Usuários");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }

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

        // -----------------------------------------------------------
        // EVENTOS: DISPARAM O PRESENTER
        // -----------------------------------------------------------
        buscarBtn.addActionListener(evt -> {
            //if(presenter != null) presenter.buscarUsuario();
        });
        
        autorizarBtn.addActionListener(evt -> {
            //if(presenter != null) presenter.autorizarUsuario();
        });
        
        desautorizarBtn.addActionListener(evt -> {
            //if(presenter != null) presenter.desautorizarUsuario();
        });
        
        excluirBtn.addActionListener(evt -> {
            //if(presenter != null) presenter.excluirUsuario();
        });
        
        novoBtn.addActionListener(evt -> {
            //if(presenter != null) presenter.novoUsuario();
        });
        
        fecharBtn.addActionListener(evt -> {
            //if(presenter != null) presenter.iniciar(); // Ou um método de fechar
        });
    }

    // ======================================================================
    // MODELO DA TABELA (Mantido igual)
    // ======================================================================
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
        public int getRowCount() { return exibidos.size(); }
        @Override
        public int getColumnCount() { return colunas.length; }
        @Override
        public String getColumnName(int column) { return colunas[column]; }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return (columnIndex == COL_SEL || columnIndex == COL_AUTORIZADO) ? Boolean.class : String.class;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex == COL_SEL || columnIndex == COL_AUTORIZADO || columnIndex == COL_PERFIL;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Usuario usuario = exibidos.get(rowIndex);
            switch (columnIndex) {
                case COL_SEL: return selecionados.contains(usuario);
                case COL_USERNAME: return usuario.getUserName();
                case COL_NOME: return usuario.getNome();
                case COL_AUTORIZADO: return usuario.isAutorizado();
                case COL_PERFIL: return (usuario.getTipo() == 1) ? "Administrador" : "Atendente";
                case COL_SITUACAO: return usuario.getSituacao();
                default: return null;
            }
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            Usuario usuario = exibidos.get(rowIndex);
            switch (columnIndex) {
                case COL_SEL:
                    if (Boolean.TRUE.equals(aValue)) {
                        if (!selecionados.contains(usuario)) selecionados.add(usuario);
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
                    usuario.setTipo(String.valueOf(aValue).equals("Administrador") ? 1 : 0);
                    break;
                default: break;
            }
            fireTableRowsUpdated(rowIndex, rowIndex);
        }
    }

    // ======================================================================
    // MAIN (Para testes visuais)
    // ======================================================================
    public static void main(String[] args) {
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(GerenciarUsuariosView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        SwingUtilities.invokeLater(() -> {
            GerenciarUsuariosView tela = new GerenciarUsuariosView();
            tela.setVisible(true);
        });
    }
}