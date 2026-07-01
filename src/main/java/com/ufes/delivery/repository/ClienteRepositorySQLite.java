/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufes.delivery.repository;

import com.ufes.delivery.model.Cliente;
import com.ufes.delivery.model.Endereco;
import com.ufes.singleton.ConexaoSQLite;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author raphael
 */
public class ClienteRepositorySQLite implements IClienteRepository {

    private final String url;

    public ClienteRepositorySQLite() {
        this.url = ConexaoSQLite.getInstancia().getURL();
        String sqlCliente = "CREATE TABLE IF NOT EXISTS tbCliente ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "cpf TEXT NOT NULL UNIQUE, "
                + "nome TEXT NOT NULL, "
                + "tipo TEXT NOT NULL, "
                + "fidelidade DOUBLE NOT NULL);";

        // cliente_id (FK para tbCliente.id) substitui a antiga cliente_cpf.
        // O id do cliente é uma chave substituta e nunca é alterado pela
        // aplicação, o que evita o risco de endereços "descolarem" do
        // cliente em caso de edição do CPF.
        String sqlEndereco = "CREATE TABLE IF NOT EXISTS tbEndereco ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "cliente_id INTEGER NOT NULL, "
                + "padrao INTEGER NOT NULL, "
                + "logradouro TEXT NOT NULL, "
                + "numero INTEGER NOT NULL, "
                + "complemento TEXT, "
                + "bairro TEXT NOT NULL, "
                + "cidade TEXT NOT NULL, "
                + "uf TEXT NOT NULL, "
                + "cep TEXT NOT NULL, "
                + "FOREIGN KEY (cliente_id) REFERENCES tbCliente(id) "
                + "ON DELETE CASCADE);";

        try (var conn = DriverManager.getConnection(this.url); var stmt = conn.createStatement()) {
            stmt.execute(sqlCliente);
            stmt.execute(sqlEndereco);
        } catch (SQLException e) {
            System.out.println("ERRO!!! " + e.getMessage());
        }
    }

    @Override
    public List<Cliente> buscarPorNomeContendo(String nome) {
        validarTrechoNome(nome);

        String sql = "SELECT id, cpf, nome, tipo, fidelidade FROM tbCliente WHERE nome LIKE ?";
        List<Cliente> clientes = new ArrayList<>();

        try (var conn = DriverManager.getConnection(this.url); var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + nome + "%");
            var rs = stmt.executeQuery();

            while (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getString("cpf"),
                        rs.getString("nome"),
                        rs.getString("tipo"),
                        rs.getDouble("fidelidade"));
                // Necessário para que carregarEnderecos() encontre os
                // endereços corretos (agora filtrados por cliente_id).
                cliente.setId(rs.getInt("id"));
                carregarEnderecos(conn, cliente);
                clientes.add(cliente);
            }
        } catch (SQLException e) {
            System.out.println("ERRO!!! " + e.getMessage());
        }

        return Collections.unmodifiableList(clientes);
    }

    @Override
    public void adicionar(Cliente cliente) {
        validarCliente(cliente);

        String sqlBusca = "SELECT cpf FROM tbCliente WHERE cpf = ?";
        String sqlInsereCliente = "INSERT INTO tbCliente(cpf, nome, tipo, fidelidade) "
                + "VALUES (?, ?, ?, ?)";
        try (var conn = DriverManager.getConnection(this.url)) {
            conn.setAutoCommit(false);
            try {
                try (var stmtBusca = conn.prepareStatement(sqlBusca)) {
                    stmtBusca.setString(1, cliente.getCPF());
                    var rs = stmtBusca.executeQuery();
                    if (rs.next()) {
                        throw new IllegalArgumentException("O cliente já existe no sistema");
                    }
                }

                try (var stmtInsere = conn.prepareStatement(sqlInsereCliente, Statement.RETURN_GENERATED_KEYS)) {
                    stmtInsere.setString(1, cliente.getCPF());
                    stmtInsere.setString(2, cliente.getNome());
                    stmtInsere.setString(3, cliente.getTipo());
                    stmtInsere.setDouble(4, cliente.getFidelidade());
                    stmtInsere.executeUpdate();

                    // Captura o id gerado para o cliente, necessário para
                    // gravar os endereços já vinculados por cliente_id.
                    try (var rsChaves = stmtInsere.getGeneratedKeys()) {
                        if (rsChaves.next()) {
                            cliente.setId(rsChaves.getInt(1));
                        }
                    }
                }

                inserirEnderecos(conn, cliente);

                conn.commit();
            } catch (RuntimeException | SQLException ex) {
                conn.rollback();
                throw ex;
            }
        } catch (SQLException e) {
            System.out.println("ERRO!!! " + e.getMessage());
        }
    }

    @Override
    public void atualizar(Cliente cliente) {
        validarCliente(cliente);

        String sqlBusca = "SELECT id FROM tbCliente WHERE id = ?";
        String sqlAtualizaCliente = "UPDATE tbCliente SET nome = ?, tipo = ?, fidelidade = ?, cpf = ?"
                + "WHERE id = ?";
        // Corrigido: antes filtrava por "id" (a PK da própria tbEndereco)
        // comparando com cliente.getId(), o que nunca apagava os endereços
        // antigos do cliente correto. Agora filtra pela FK cliente_id.
        String sqlRemoveEnderecos = "DELETE FROM tbEndereco WHERE cliente_id = ?";

        try (var conn = DriverManager.getConnection(this.url)) {
            conn.setAutoCommit(false);
            try {
                try (var stmtBusca = conn.prepareStatement(sqlBusca)) {
                    stmtBusca.setInt(1, cliente.getId());
                    var rs = stmtBusca.executeQuery();
                    if (!rs.next()) {
                        throw new IllegalArgumentException("O cliente ainda não existe");
                    }
                }

                try (var stmtAtualiza = conn.prepareStatement(sqlAtualizaCliente)) {
                    stmtAtualiza.setString(1, cliente.getNome());
                    stmtAtualiza.setString(2, cliente.getTipo());
                    stmtAtualiza.setDouble(3, cliente.getFidelidade());
                    stmtAtualiza.setString(4, cliente.getCPF());
                    stmtAtualiza.setInt(5, cliente.getId());
                    stmtAtualiza.executeUpdate();
                }

                try (var stmtDel = conn.prepareStatement(sqlRemoveEnderecos)) {
                    stmtDel.setInt(1, cliente.getId());
                    stmtDel.executeUpdate();
                }

                inserirEnderecos(conn, cliente);

                conn.commit();
            } catch (RuntimeException | SQLException ex) {
                conn.rollback();
                throw ex;
            }
        } catch (SQLException e) {
            System.out.println("ERRO!!! " + e.getMessage());
        }
    }

    @Override
    public void removerPorCPF(String cpf) {
        validarCPF(cpf);

        String sqlBuscaId = "SELECT id FROM tbCliente WHERE cpf = ?";
        String sqlDelEnd = "DELETE FROM tbEndereco WHERE cliente_id = ?";
        String sqlDelCliente = "DELETE FROM tbCliente WHERE cpf = ?";

        try (var conn = DriverManager.getConnection(this.url)) {
            conn.setAutoCommit(false);
            try {
                int clienteId = -1;
                try (var stmtBusca = conn.prepareStatement(sqlBuscaId)) {
                    stmtBusca.setString(1, cpf);
                    var rs = stmtBusca.executeQuery();
                    if (rs.next()) {
                        clienteId = rs.getInt("id");
                    }
                }

                if (clienteId != -1) {
                    try (var stmtDel = conn.prepareStatement(sqlDelEnd)) {
                        stmtDel.setInt(1, clienteId);
                        stmtDel.executeUpdate();
                    }
                }

                try (var stmtDel = conn.prepareStatement(sqlDelCliente)) {
                    stmtDel.setString(1, cpf);
                    stmtDel.executeUpdate();
                }
                conn.commit();
            } catch (RuntimeException | SQLException ex) {
                conn.rollback();
                throw ex;
            }
        } catch (SQLException e) {
            System.out.println("ERRO!!! " + e.getMessage());
        }
    }

    @Override
    public void removerPorId(int id) {

        // Corrigido: o filtro antigo "WHERE id = ?" comparava a PK da
        // própria tbEndereco com o id do cliente. O correto é filtrar
        // pela FK cliente_id.
        String sqlDelEnd = "DELETE FROM tbEndereco WHERE cliente_id = ?";
        String sqlDelCliente = "DELETE FROM tbCliente WHERE id = ?";

        try (var conn = DriverManager.getConnection(this.url)) {
            conn.setAutoCommit(false);
            try {
                try (var stmtDel = conn.prepareStatement(sqlDelEnd)) {
                    stmtDel.setInt(1, id);
                    stmtDel.executeUpdate();
                }
                try (var stmtDel = conn.prepareStatement(sqlDelCliente)) {
                    stmtDel.setInt(1, id);
                    stmtDel.executeUpdate();
                }
                conn.commit();
            } catch (RuntimeException | SQLException ex) {
                conn.rollback();
                throw ex;
            }
        } catch (SQLException e) {
            System.out.println("ERRO!!! " + e.getMessage());
        }
    }

    @Override
    public List<Cliente> listarClientes() {
        String sql = "SELECT id, cpf, nome, tipo, fidelidade FROM tbCliente";
        List<Cliente> clientes = new ArrayList<>();

        try (var conn = DriverManager.getConnection(this.url); var stmt = conn.createStatement(); var rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getString("cpf"),
                        rs.getString("nome"),
                        rs.getString("tipo"),
                        rs.getDouble("fidelidade"));
                cliente.setId(rs.getInt("id"));
                carregarEnderecos(conn, cliente);
                clientes.add(cliente);
            }
        } catch (SQLException e) {
            System.out.println("ERRO!!! " + e.getMessage());
        }

        return Collections.unmodifiableList(clientes);
    }

    @Override
    public Optional<Cliente> getPorCPF(String cpf) {
        validarCPF(cpf);

        String sql = "SELECT id, cpf, nome, tipo, fidelidade FROM tbCliente WHERE cpf = ?";

        try (var conn = DriverManager.getConnection(this.url); var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            var rs = stmt.executeQuery();

            if (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getString("cpf"),
                        rs.getString("nome"),
                        rs.getString("tipo"),
                        rs.getDouble("fidelidade"));
                cliente.setId(rs.getInt("id"));
                carregarEnderecos(conn, cliente);
                return Optional.of(cliente);
            }
        } catch (SQLException e) {
            System.out.println("ERRO!!! " + e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public Optional<Cliente> getPorIdCliente(int id) {

        String sql = "SELECT id, cpf, nome, tipo, fidelidade FROM tbCliente WHERE id = ?";

        try (var conn = DriverManager.getConnection(this.url); var stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            var rs = stmt.executeQuery();

            if (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getString("cpf"),
                        rs.getString("nome"),
                        rs.getString("tipo"),
                        rs.getDouble("fidelidade"));

                cliente.setId(rs.getInt("id"));
                carregarEnderecos(conn, cliente);
                return Optional.of(cliente);
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

    private void validarCPF(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new IllegalArgumentException("O cpf busca deve ser informado.");
        }
    }

    private void validarCliente(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("O cliente deve ser informado.");
        }
    }

    private void carregarEnderecos(Connection conn, Cliente cliente) throws SQLException {
        String sql = "SELECT padrao, logradouro, numero, complemento, bairro, cidade, uf, cep "
                + "FROM tbEndereco WHERE cliente_id = ?";

        try (var stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cliente.getId());
            var rs = stmt.executeQuery();
            while (rs.next()) {
                Endereco end = new Endereco(
                        rs.getInt("padrao") == 1,
                        rs.getString("logradouro"),
                        rs.getInt("numero"),
                        rs.getString("complemento"),
                        rs.getString("bairro"),
                        rs.getString("cidade"),
                        rs.getString("uf"),
                        rs.getString("cep"));
                cliente.addEndereco(end);
            }
        }
    }

    private void inserirEnderecos(Connection conn, Cliente cliente) throws SQLException {
        String sqlInsereEndereco = "INSERT INTO tbEndereco(cliente_id, padrao, logradouro, "
                + "numero, complemento, bairro, cidade, uf, cep) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (var stmt = conn.prepareStatement(sqlInsereEndereco)) {
            for (Endereco end : cliente.getEnderecos()) {
                stmt.setInt(1, cliente.getId());
                stmt.setInt(2, end.isPadrao() ? 1 : 0);
                stmt.setString(3, end.getLogradouro());
                stmt.setInt(4, end.getNumero());
                stmt.setString(5, end.getComplemento());
                stmt.setString(6, end.getBairro());
                stmt.setString(7, end.getCidade());
                stmt.setString(8, end.getUf());
                stmt.setString(9, end.getCep());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }
}