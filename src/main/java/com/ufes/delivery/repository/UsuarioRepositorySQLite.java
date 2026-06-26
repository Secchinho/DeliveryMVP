/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufes.delivery.repository;

import com.ufes.delivery.model.Usuario;
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
public class UsuarioRepositorySQLite implements IUsuarioRepository{

    private final String url;
    
    public UsuarioRepositorySQLite(){
        this.url = "jdbc:sqlite:Delivery.db";
        
        String sql = "CREATE TABLE IF NOT EXISTS Usuarios ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT," + "nome TEXT NOT NULL,"
                + "userName TEXT NOT NULL," + "tipo INTEGER NOT NULL," 
                + "situacao TEXT NOT NULL," + "autorizado BOOLEAN NOT NULL"
                + ");";
        
        try(var conn = DriverManager.getConnection(this.url);
                var stmt = conn.createStatement()){
            stmt.execute(sql);
        }catch(SQLException e){
            System.out.println("ERRO!!! " + e.getMessage());
        }
    }
    
    @Override
    public List<Usuario> buscarPorNomeContendo(String nome) {
        validarTrechoNome(nome);
        String sql = "SELECT nome, userName, tipo, situacao, autorizado FROM"
                + " Produtos WHERE nome LIKE ?";
        List<Usuario> usuarios = new ArrayList<>();
        
        try(var conn = DriverManager.getConnection(this.url);
                var stmt = conn.prepareStatement(sql)){
            stmt.setString(1, "%" + nome + "%");
            var rs = stmt.executeQuery();
            
            while(rs.next()){
                usuarios.add(mapear(rs));
            }
        }catch (SQLException e){
            System.out.println("ERRO!!! " + e.getMessage());
        }
        
        return Collections.unmodifiableList(usuarios);
    }

    @Override
    public void adicionar(Usuario usuario) {
        
    }

    @Override
    public void removerPorNomeUsuario(String nome) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Usuario> listarUsuarios() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Optional<Usuario> getPorUserName(String userName) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    private void validarTrechoNome(String trechoNome) {
        if (trechoNome == null || trechoNome.trim().isEmpty()) {
            throw new IllegalArgumentException("O trecho do nome para busca deve ser informado.");
        }
    }
    
    private Usuario mapear(ResultSet rs) throws SQLException {
    Usuario u = new Usuario(
        rs.getString("nome"),
        rs.getString("userName"),
        rs.getString("senha")
    );
    u.setTipo(rs.getInt("tipo"));
    u.setSituacao(rs.getString("situacao"));
    return u;
}
}
