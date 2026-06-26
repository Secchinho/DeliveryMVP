/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.ufes.delivery.view;

import com.ufes.delivery.model.Usuario;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
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
public class TelaUsuariosView extends javax.swing.JPanel {

    private static final String[] PERFIS = {"Atendente", "Administrador"};

    private List<Usuario> todosUsuarios;
    private UsuariosTableModel tableModel;

    /**
     * Creates new form TelaUsuariosView
     */
    public TelaUsuariosView() {
        this.todosUsuarios = criarUsuariosDeExemplo();
        initComponents();
        this.tableModel = new UsuariosTableModel(todosUsuarios);
        usuariosTable.setModel(tableModel);
        configurarColunaPerfil();
        usuariosTable.setRowHeight(24);
    }

    private List<Usuario> criarUsuariosDeExemplo() {
        List<Usuario> lista = new ArrayList<>();

        Usuario adminmaster = new Usuario("Administrador Master", "adminmaster", "123456");
        adminmaster.setTipo(1);
        adminmaster.setAutorizado(true);
        lista.add(adminmaster);

        Usuario atendente01 = new Usuario("Carlos Atendente", "atendente01", "123456");
        atendente01.setAutorizado(true);
        lista.add(atendente01);

        Usuario maria01 = new Usuario("Maria Oliveira", "maria01", "123456");
        // permanece Pendente (situação padrão do construtor)
        lista.add(maria01);

        Usuario joaosilva = new Usuario("Joao Silva", "joaosilva", "123456");
        joaosilva.setAutorizado(true);
        joaosilva.setAutorizado(false); // força transição para Não autorizado
        lista.add(joaosilva);

        Usuario fulano123 = new Usuario("Fulano de Tal", "fulano123", "123456");
        fulano123.setAutorizado(true);
        lista.add(fulano123);

        return lista;
    }

    private void configurarColunaPerfil() {
        TableColumn colunaPerfil = usuariosTable.getColumnModel().getColumn(UsuariosTableModel.COL_PERFIL);
        JComboBox<String> comboPerfil = new JComboBox<>(PERFIS);
        colunaPerfil.setCellEditor(new javax.swing.DefaultCellEditor(comboPerfil));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buscaPanel = new javax.swing.JPanel();
        nomeBuscaLbl = new javax.swing.JLabel();
        nomeBuscaTxt = new javax.swing.JTextField();
        buscarBtn = new javax.swing.JButton();
        usuariosPanel = new javax.swing.JPanel();
        usuariosScroll = new javax.swing.JScrollPane();
        usuariosTable = new javax.swing.JTable();
        botoesPanel = new javax.swing.JPanel();
        autorizarBtn = new javax.swing.JButton();
        desautorizarBtn = new javax.swing.JButton();
        excluirBtn = new javax.swing.JButton();
        novoBtn = new javax.swing.JButton();
        fecharBtn = new javax.swing.JButton();

        buscaPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Busca de Usuários"));

        nomeBuscaLbl.setText("Nome");

        buscarBtn.setText("Buscar");
        buscarBtn.addActionListener(this::buscarBtnActionPerformed);

        javax.swing.GroupLayout buscaPanelLayout = new javax.swing.GroupLayout(buscaPanel);
        buscaPanel.setLayout(buscaPanelLayout);
        buscaPanelLayout.setHorizontalGroup(
            buscaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buscaPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(nomeBuscaLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(nomeBuscaTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(buscarBtn)
                .addContainerGap())
        );
        buscaPanelLayout.setVerticalGroup(
            buscaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buscaPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(buscaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nomeBuscaLbl)
                    .addComponent(nomeBuscaTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buscarBtn))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        usuariosPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Usuários"));

        usuariosTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sel.", "Nome de usuário", "Nome", "Autorizado", "Perfil", "Situação"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, true, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        usuariosScroll.setViewportView(usuariosTable);

        javax.swing.GroupLayout usuariosPanelLayout = new javax.swing.GroupLayout(usuariosPanel);
        usuariosPanel.setLayout(usuariosPanelLayout);
        usuariosPanelLayout.setHorizontalGroup(
            usuariosPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(usuariosPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(usuariosScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 600, Short.MAX_VALUE)
                .addContainerGap())
        );
        usuariosPanelLayout.setVerticalGroup(
            usuariosPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(usuariosPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(usuariosScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 220, Short.MAX_VALUE)
                .addContainerGap())
        );

        botoesPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 8, 8));

        autorizarBtn.setText("Autorizar");
        autorizarBtn.addActionListener(this::autorizarBtnActionPerformed);
        botoesPanel.add(autorizarBtn);

        desautorizarBtn.setText("Desautorizar");
        desautorizarBtn.addActionListener(this::desautorizarBtnActionPerformed);
        botoesPanel.add(desautorizarBtn);

        excluirBtn.setText("Excluir");
        excluirBtn.addActionListener(this::excluirBtnActionPerformed);
        botoesPanel.add(excluirBtn);

        novoBtn.setText("Novo");
        novoBtn.addActionListener(this::novoBtnActionPerformed);
        botoesPanel.add(novoBtn);

        fecharBtn.setText("Fechar");
        fecharBtn.addActionListener(this::fecharBtnActionPerformed);
        botoesPanel.add(fecharBtn);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(buscaPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(usuariosPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(botoesPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(10, 10, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(buscaPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(usuariosPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(botoesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10))
        );
    }// </editor-fold>//GEN-END:initComponents


    private void buscarBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buscarBtnActionPerformed
        String filtro = nomeBuscaTxt.getText() == null ? "" : nomeBuscaTxt.getText().trim();
        tableModel.filtrarPorNome(filtro);
    }//GEN-LAST:event_buscarBtnActionPerformed

    private void autorizarBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autorizarBtnActionPerformed
        List<Usuario> selecionados = tableModel.getUsuariosSelecionados();
        if (selecionados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione ao menos um usuário na coluna \"Sel.\".",
                    "Autorizar", JOptionPane.WARNING_MESSAGE);
            return;
        }
        for (Usuario usuario : selecionados) {
            usuario.setAutorizado(true);
        }
        tableModel.fireTableDataChanged();
    }//GEN-LAST:event_autorizarBtnActionPerformed

    private void desautorizarBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_desautorizarBtnActionPerformed
        List<Usuario> selecionados = tableModel.getUsuariosSelecionados();
        if (selecionados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione ao menos um usuário na coluna \"Sel.\".",
                    "Desautorizar", JOptionPane.WARNING_MESSAGE);
            return;
        }
        for (Usuario usuario : selecionados) {
            usuario.setAutorizado(true);
            usuario.setAutorizado(false);
        }
        tableModel.fireTableDataChanged();
    }//GEN-LAST:event_desautorizarBtnActionPerformed

    private void excluirBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_excluirBtnActionPerformed
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
            tableModel.filtrarPorNome(nomeBuscaTxt.getText());
        }
    }//GEN-LAST:event_excluirBtnActionPerformed

    private void novoBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_novoBtnActionPerformed
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
            todosUsuarios.add(novo);
            nomeBuscaTxt.setText("");
            tableModel.filtrarPorNome("");
        }
    }//GEN-LAST:event_novoBtnActionPerformed

    private void fecharBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fecharBtnActionPerformed
        java.awt.Window janela = SwingUtilities.getWindowAncestor(this);
        if (janela != null) {
            janela.dispose();
        }
    }//GEN-LAST:event_fecharBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton autorizarBtn;
    private javax.swing.JPanel botoesPanel;
    private javax.swing.JPanel buscaPanel;
    private javax.swing.JButton buscarBtn;
    private javax.swing.JButton desautorizarBtn;
    private javax.swing.JButton excluirBtn;
    private javax.swing.JButton fecharBtn;
    private javax.swing.JLabel nomeBuscaLbl;
    private javax.swing.JTextField nomeBuscaTxt;
    private javax.swing.JButton novoBtn;
    private javax.swing.JPanel usuariosPanel;
    private javax.swing.JScrollPane usuariosScroll;
    private javax.swing.JTable usuariosTable;
    // End of variables declaration//GEN-END:variables

    /**
     * Modelo de tabela que liga a lista de {@link Usuario} à JTable,
     * controlando a seleção (coluna "Sel."), a autorização e o perfil.
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
                    if(usuario.getTipo() == 0){
                        return "Atendente";
                    }else{
                        return "Admin";
                    }
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
                    usuario.setAutorizado(Boolean.TRUE.equals(aValue));
                    break;
                case COL_PERFIL:
                    if(String.valueOf(aValue).equals("Administrador")){
                        usuario.setTipo(1);
                    }else if(String.valueOf(aValue).equals("Atendente")){
                        usuario.setTipo(0);
                    }else{
                        usuario.setTipo(0);
                    }
                    break;
                default:
                    break;
            }
            fireTableRowsUpdated(rowIndex, rowIndex);
        }
    }
}
