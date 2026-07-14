/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufes.delivery.repository;

import com.ufes.delivery.model.Usuario;
import com.ufes.singleton.ConexaoSQLite;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author raphael
 */
public class UsuarioRepositorySQLite implements IUsuarioRepository {

    private final String url;

    public UsuarioRepositorySQLite() {
        this.url = ConexaoSQLite.getInstancia().getURL();

        String sql = "CREATE TABLE IF NOT EXISTS tbUsuario ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT," + "nome TEXT NOT NULL,"
                + "userName TEXT NOT NULL UNIQUE," + "senha TEXT NOT NULL," + "tipo INTEGER NOT NULL,"
                + "situacao TEXT NOT NULL," + "autorizado BOOLEAN NOT NULL"
                + ");";

        try (var conn = DriverManager.getConnection(this.url); var stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("ERRO!!! " + e.getMessage());
        }
    }

    @Override
    public List<Usuario> buscarPorNomeContendo(String nome) {
        validarTrechoNome(nome);
        // IMPORTANTE: a coluna "senha" DEVE constar no SELECT, porque o
        // mapeador mapear() a lê via rs.getString("senha"). Sem ela, a
        // SQLException lançada dentro do while faz com que a lista seja
        // devolvida vazia, o que por sua vez faz CadastrarUsuarioPresenter
        // crer que não há usuários cadastrados (isPrimeiroUsuario()==true)
        // e atribuir perfil Administrador + situação Autorizado a todos os
        // cadastros posteriores — violando o US02, Cenário 2.
        String sql = "SELECT nome, userName, senha, tipo, situacao, autorizado FROM"
                + " tbUsuario WHERE nome LIKE ?";
        List<Usuario> usuarios = new ArrayList<>();

        try (var conn = DriverManager.getConnection(this.url); var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + nome + "%");
            var rs = stmt.executeQuery();

            while (rs.next()) {
                usuarios.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.out.println("ERRO!!! " + e.getMessage());
        }

        return Collections.unmodifiableList(usuarios);
    }

    @Override
    public void adicionar(Usuario usuario) {
        validarUsuario(usuario);

        // Mesmo raciocínio de buscarPorNomeContendo: o SELECT precisa trazer
        // a coluna "senha" para que o mapear() não lance SQLException.
        String sql = "SELECT nome, userName, senha, tipo, situacao, autorizado FROM"
                + " tbUsuario WHERE userName = ?";

        try (var conn = DriverManager.getConnection(this.url);
             var stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getUserName());
            var rs = stmt.executeQuery();

            if (rs.next()) {
                throw new IllegalArgumentException("O usuário já existe no sistema");
            }

            sql = "INSERT INTO tbUsuario(nome, userName, senha, tipo, "
                    + "situacao, autorizado) VALUES (?, ?, ?, ?, ?, ?)";
            try (var istmt = conn.prepareStatement(sql)) {
                istmt.setString(1, usuario.getNome());
                istmt.setString(2, usuario.getUserName());
                istmt.setString(3, usuario.getSenha());
                istmt.setInt(4, usuario.getTipo());
                istmt.setString(5, usuario.getSituacao());
                istmt.setBoolean(6, usuario.isAutorizado());
                istmt.executeUpdate();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro de persistência ao cadastrar usuário: " + e.getMessage(), e);
        }
    }

    @Override
    public void atualizar(Usuario usuario) {
        validarUsuario(usuario);

        // SELECT traz "senha" para compatibilidade com mapear().
        String sql = "SELECT nome, userName, senha, tipo, situacao, autorizado FROM"
                + " tbUsuario WHERE userName = ?";

        try (var conn = DriverManager.getConnection(this.url); var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario.getUserName());
            var rs = stmt.executeQuery();
            if (rs.next()) {
                // Corrigido: o WHERE agora usa parâmetro (?) em vez de
                // concatenação de string. Antes, "WHERE userName = " + userName
                // produzia SQL inválido (userName tratado como nome de coluna
                // inexistente) e o UPDATE silenciosamente falhava, de modo que
                // autorizações/desautorizações dadas via Gerenciar Usuários
                // nunca eram persistidas. Adicionalmente, a concatenação abria
                // brecha de SQL injection.
                sql = "UPDATE tbUsuario SET nome = ?, senha = ?, tipo = ?,"
                        + " situacao = ?, autorizado = ? "
                        + "WHERE userName = ?";
                var ustmt = conn.prepareStatement(sql);
                ustmt.setString(1, usuario.getNome());
                ustmt.setString(2, usuario.getSenha());
                ustmt.setInt(3, usuario.getTipo());
                ustmt.setString(4, usuario.getSituacao());
                ustmt.setBoolean(5, usuario.isAutorizado());
                ustmt.setString(6, usuario.getUserName());
                ustmt.executeUpdate();
            } else {
                sql = "INSERT INTO tbUsuario(nome, userName, senha, tipo, "
                        + "situacao, autorizado) VALUES (?, ?, ?, ?, ?, ?)";
                var istmt = conn.prepareStatement(sql);
                istmt.setString(1, usuario.getNome());
                istmt.setString(2, usuario.getUserName());
                istmt.setString(3, usuario.getSenha());
                istmt.setInt(4, usuario.getTipo());
                istmt.setString(5, usuario.getSituacao());
                istmt.setBoolean(6, usuario.isAutorizado());
                istmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("ERRO!!! " + e.getMessage());
        }
    }

    @Override
    public void removerPorNomeUsuario(String userName) {
        validarUserName(userName);

        String sql = "DELETE FROM tbUsuario WHERE userName = ?";

        try (var conn = DriverManager.getConnection(this.url); var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userName);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("ERRO!!! " + e.getMessage());
        }
    }

    @Override
    public List<Usuario> listarUsuarios() {
        // IMPORTANTE: a coluna "senha" DEVE constar no SELECT porque o
        // mapeador mapear() a lê via rs.getString("senha"). Sem ela, a
        // SQLException lançada no primeiro rs.next() faz com que a lista
        // seja devolvida vazia, levando CadastrarUsuarioPresenter a crer
        // que não há usuários cadastrados (isPrimeiroUsuario()==true) e a
        // registrar TODO novo usuário como Administrador+Autorizado — bug
        // que viola o US02 (Cenário 2) e, em cascata, faz o painel
        // operacional exibir "Administrador" para qualquer login (US04).
        String sql = "SELECT nome, userName, senha, tipo, situacao, autorizado FROM "
                + "tbUsuario";
        List<Usuario> usuarios = new ArrayList<>();

        try (var conn = DriverManager.getConnection(this.url); var stmt = conn.createStatement(); var rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                usuarios.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.out.println("ERRO!!! " + e.getMessage());
        }

        return Collections.unmodifiableList(usuarios);
    }

    @Override
    public Optional<Usuario> getPorUserName(String userName) {
        validarUserName(userName);

        String sql = "SELECT nome, userName, senha, tipo, situacao, autorizado "
                + "FROM tbUsuario WHERE userName = ?";

        try (var conn = DriverManager.getConnection(this.url); var stmt = conn.prepareStatement(sql);) {
            stmt.setString(1, userName);
            var rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapear(rs));
            }
        } catch (SQLException e) {
            System.out.println("ERRO!!! " + e.getMessage());
        }

        return Optional.empty();
    }

    private void validarTrechoNome(String trechoNome) {
        if (trechoNome == null || trechoNome.trim().isEmpty()) {
            throw new IllegalArgumentException("O trecho do nome para busca deve ser informado.");
        }
    }

    private void validarUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("O usuário deve ser informado.");
        }
    }

    private void validarUserName(String userName) {
        if (userName == null) {
            throw new IllegalArgumentException("O nome de usuário deve ser informado.");
        }
    }

    private Usuario mapear(ResultSet rs) throws SQLException {
        Usuario u = new Usuario(rs.getString("nome"), rs.getString("userName"), rs.getString("senha"));
        u.setTipo(rs.getInt("tipo"));
        u.setSituacao(rs.getString("situacao"));
        // Antes faltava: o campo "autorizado" nunca era reposto a partir do
        // ResultSet, permanecendo com o default false mesmo para usuários
        // previamente autorizados. Agora é lido da coluna, presente em todos
        // os SELECTs deste repositório.
        u.setAutorizado(rs.getBoolean("autorizado"));
        return u;
    }
}
