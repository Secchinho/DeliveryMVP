/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufes.delivery.repository;

import com.ufes.delivery.model.Cliente;
import com.ufes.singleton.ConexaoSQLite;
import java.sql.DriverManager;
import java.sql.SQLException;
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
        this.url = ConexaoSQLite.getInstacia().getURL();
        String sql = "CREATE TABLE IF NOT EXISTS tbCliente ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, cpf TEXT NOT NULL"
                + "nome TEXT NOT NULL, tipo TEXT NOT NULL,"
                + "fidelidade DOUBLE NOT NULL, logradouro TEXT NOT NULL,"
                + "bairro TEXT NOT NULL, cidade TEXT NOT NULL);";

        try (var conn = DriverManager.getConnection(this.url); var stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("ERRO!!! " + e.getMessage());
        }
    }

    @Override
    public List<Cliente> buscarPorNomeContendo(String nome) {
        validarTrechoNome(nome);

        String sql = "SELECT cpf, nome, tipo, fidelidade, logradouro, bairro, cidade FROM"
                + " tbCliente WHERE nome LIKE ?";
        List<Cliente> clientes = new ArrayList<>();

        try (var conn = DriverManager.getConnection(this.url); var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + nome + "%");
            var rs = stmt.executeQuery();

            while (rs.next()) {
                clientes.add(new Cliente(rs.getString("cpf"), rs.getString("nome"),
                        rs.getString("tipo"), rs.getDouble("fidelidade"),
                        rs.getString("logradouro"), rs.getString("bairro"),
                        rs.getString("cidade")));
            }
        } catch (SQLException e) {
            System.out.println("ERRO!!! " + e.getMessage());
        }

        return Collections.unmodifiableList(clientes);
    }

    @Override
    public void salvar(Cliente cliente) {
        validarCliente(cliente);

        String sql = "SELECT cpf, nome, tipo, fidelidade, logradouro, bairro, cidade FROM"
                + " tbCliente WHERE cpf = " + cliente.getCPF();

        try (var conn = DriverManager.getConnection(this.url); var stmt = conn.createStatement(); var rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                sql = "UPDATE tbCliente SET cpf = ?, nome = ?, tipo = ?,"
                        + " fidelidade = ?, logradouro = ?, bairro = ?, cidade = ? "
                        + "WHERE cpf = " + cliente.getCPF();
                var ustmt = conn.prepareStatement(sql);
                ustmt.setString(1, cliente.getCPF());
                ustmt.setString(2, cliente.getNome());
                ustmt.setString(3, cliente.getTipo());
                ustmt.setDouble(4, cliente.getFidelidade());
                ustmt.setString(5, cliente.getLogradouro());
                ustmt.setString(6, cliente.getBairro());
                ustmt.setString(7, cliente.getCidade());
                ustmt.executeUpdate();
            } else {
                sql = "INSERT INTO tbCliente(cpf, nome, tipo, fidelidade, logradouro, "
                        + "bairro, cidade) VALUES (?, ?, ?, ?, ?, ?, ?)";
                var istmt = conn.prepareStatement(sql);
                istmt.setString(1, cliente.getCPF());
                istmt.setString(2, cliente.getNome());
                istmt.setString(3, cliente.getTipo());
                istmt.setDouble(4, cliente.getFidelidade());
                istmt.setString(5, cliente.getLogradouro());
                istmt.setString(6, cliente.getBairro());
                istmt.setString(7, cliente.getCidade());
                istmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("ERRO!!! " + e.getMessage());
        }
    }

    @Override
    public void removerPorCPF(String cpf) {
        validarCPF(cpf);

        String sql = "DELETE FROM tbCliente WHERE cpf = ?";

        try (var conn = DriverManager.getConnection(this.url); var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("ERRO!!! " + e.getMessage());
        }
    }

    @Override
    public List<Cliente> listarClientes() {
        String sql = "SELECT cpf, nome, tipo, fidelidade, logradouro, bairro, cidade FROM "
                + "tbCliente";
        List<Cliente> clientes = new ArrayList<>();

        try (var conn = DriverManager.getConnection(this.url); var stmt = conn.createStatement(); var rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                clientes.add(new Cliente(rs.getString("cpf"), rs.getString("nome"),
                         rs.getString("tipo"), rs.getDouble("fidelidade"),
                         rs.getString("logradouro"), rs.getString("bairro"),
                         rs.getString("cidade")));
            }
        } catch (SQLException e) {
            System.out.println("ERRO!!! " + e.getMessage());
        }

        return Collections.unmodifiableList(clientes);
    }

    @Override
    public Optional<Cliente> getPorCPF(String cpf) {
        validarCPF(cpf);

       String sql = "SELECT cpf, nome, tipo, fidelidade, logradouro, bairro, cidade FROM "
                + "tbCliente WHERE cpf = ?";

        try (var conn = DriverManager.getConnection(this.url); var stmt = conn.prepareStatement(sql);) {
            stmt.setString(1, cpf);
            var rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(new Cliente(rs.getString("cpf"), rs.getString("nome"),
                         rs.getString("tipo"), rs.getDouble("fidelidade"),
                         rs.getString("logradouro"), rs.getString("bairro"),
                         rs.getString("cidade")));
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

}
