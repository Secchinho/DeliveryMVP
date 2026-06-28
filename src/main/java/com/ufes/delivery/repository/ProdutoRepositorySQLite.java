/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufes.delivery.repository;

import com.ufes.delivery.model.Produto;
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
public class ProdutoRepositorySQLite implements IProdutoRepository {

    private final String url;

    public ProdutoRepositorySQLite() {
        this.url = ConexaoSQLite.getInstancia().getURL();

        String sql = "CREATE TABLE IF NOT EXISTS tbProduto ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT," + "nome TEXT NOT NULL,"
                + "codigo TEXT NOT NULL," + "categoria TEXT NOT NULL, "
                + "precoUnitario DOUBLE NOT NULL," + "quantidadeInicial INTEGER NOT NULL "
                + ");";

        try (var conn = DriverManager.getConnection(this.url); var stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("ERRO!!! " + e.getMessage());
        }
    }

    @Override
    public List<Produto> buscarPorNome(String nome) {
        validarNome(nome);

        String sql = "SELECT nome, codigo, categoria, precoUnitario, "
                + " quantidadeInicial FROM"
                + " tbProduto WHERE nome LIKE ?";

        List<Produto> produtos = new ArrayList<>();

        try (var conn = DriverManager.getConnection(this.url); var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + nome + "%");
            var rs = stmt.executeQuery();

            while (rs.next()) {
                produtos.add(new Produto(rs.getString("nome"),
                        rs.getString("codigo"), rs.getString("categoria"),
                        rs.getDouble("precoUnitario"), rs.getInt("quantidadeInicial")));

            }
        } catch (SQLException e) {
            System.out.println("ERRO!!! " + e.getMessage());
        }

        return Collections.unmodifiableList(produtos);
    }

    @Override
    public List<Produto> buscarPorCategoria(String categoria) {
        validarCategoria(categoria);

        String sql = "SELECT nome, codigo, categoria, precoUnitario, "
                + " quantidadeInicial FROM"
                + " tbProduto WHERE categoria = ?";

        List<Produto> produtos = new ArrayList<>();

        try (var conn = DriverManager.getConnection(this.url); var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, categoria);
            var rs = stmt.executeQuery();

            while (rs.next()) {
                produtos.add(new Produto(rs.getString("nome"),
                        rs.getString("codigo"), rs.getString("categoria"),
                        rs.getDouble("precoUnitario"), rs.getInt("quantidadeInicial")));

            }
        } catch (SQLException e) {
            System.out.println("ERRO!!! " + e.getMessage());
        }

        return Collections.unmodifiableList(produtos);
    }

    @Override
    public void adicionar(Produto produto) {
        validarProduto(produto);

        String sql = "SELECT nome, codigo, categoria, precoUnitario, "
                + " quantidadeInicial FROM"
                + " tbProduto WHERE codigo = ?";

        try (var conn = DriverManager.getConnection(this.url); var stmt = conn.prepareStatement(sql)){
            stmt.setString(1, produto.getCodigo());
            var rs = stmt.executeQuery();
            if (rs.next()) {
                throw new SQLException("O produto ainda não existe");
            } else {
                sql = "INSERT INTO tbProduto(nome, codigo, categoria, precoUnitario, "
                        + "quantidadeInicial) VALUES (?, ?, ?, ?, ?)";
                var istmt = conn.prepareStatement(sql);
                istmt.setString(1, produto.getNome());
                istmt.setString(2, produto.getCodigo());
                istmt.setString(3, produto.getCategoria());
                istmt.setDouble(4, produto.getPrecoUnitario());
                istmt.setInt(5, produto.getQuantidadeInicial());
                istmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("ERRO!!! " + e.getMessage());
        }
    }

    @Override
    public void atualizar(Produto produto) {
        validarProduto(produto);

        String sql = "SELECT nome, codigo, categoria, precoUnitario, "
                + " quantidadeInicial FROM"
                + " tbProduto WHERE codigo = ?";

        try (var conn = DriverManager.getConnection(this.url); var stmt = conn.prepareStatement(sql)){
            stmt.setString(1, produto.getCodigo());
            var rs = stmt.executeQuery(sql);
            if (rs.next()) {
                sql = "UPDATE tbProduto SET nome = ?, categoria = ?,"
                        + " precoUnitario = ?, quantidadeInicial = ? "
                        + "WHERE codigo = " + produto.getCodigo();

                var ustmt = conn.prepareStatement(sql);
                ustmt.setString(1, produto.getNome());
                ustmt.setString(2, produto.getCategoria());
                ustmt.setDouble(3, produto.getPrecoUnitario());
                ustmt.setInt(4, produto.getQuantidadeInicial());
                ustmt.executeUpdate();
            } else {
                throw new SQLException("O produto ainda não existe");
            }
        } catch (SQLException e) {
            System.out.println("ERRO!!! " + e.getMessage());
        }
    }

    @Override
    public void removerPorCodigo(String codigo) {
        validarCodigo(codigo);

        String sql = "DELETE FROM tbProduto WHERE codigo = ?";

        try (var conn = DriverManager.getConnection(this.url); var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, codigo);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("ERRO!!! " + e.getMessage());
        }
    }

    @Override
    public List<Produto> listarProdutos() {

        String sql = "SELECT nome, codigo, categoria, precoUnitario, "
                + " quantidadeInicial FROM"
                + " tbProduto";
        List<Produto> produtos = new ArrayList<>();

        try (var conn = DriverManager.getConnection(this.url); var stmt = conn.createStatement(); var rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                produtos.add(new Produto(rs.getString("nome"),
                        rs.getString("codigo"), rs.getString("categoria"),
                        rs.getDouble("precoUnitario"), rs.getInt("quantidadeInicial")));
            }

        } catch (SQLException e) {
            System.out.println("ERRO!!! " + e.getMessage());
        }

        return Collections.unmodifiableList(produtos);
    }

    @Override
    public Optional<Produto> getPorCodigo(String codigo) {
        validarCodigo(codigo);

        String sql = "SELECT nome, codigo, categoria, precoUnitario, "
                + " quantidadeInicial FROM"
                + " tbProduto WHERE codigo = " + codigo;

        try (var conn = DriverManager.getConnection(this.url); var stmt = conn.prepareStatement(sql);) {
            stmt.setString(1, codigo);
            var rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(new Produto(rs.getString("nome"),
                        rs.getString("codigo"), rs.getString("categoria"),
                        rs.getDouble("precoUnitario"), rs.getInt("quantidadeInicial")));
            }
        } catch (SQLException e) {
            System.out.println("ERRO!!! " + e.getMessage());
        }

        return Optional.empty();
    }

    private void validarNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome para busca deve ser informado.");
        }
    }

    private void validarCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("O código para busca deve ser informado.");
        }
    }

    private void validarCategoria(String categoria) {
        if (categoria == null || categoria.trim().isEmpty()) {
            throw new IllegalArgumentException("A categoria para busca deve ser informado.");
        }
    }

    private void validarProduto(Produto produto) {
        if (produto == null) {
            throw new IllegalArgumentException("O produto deve ser informado.");
        }
    }
}
