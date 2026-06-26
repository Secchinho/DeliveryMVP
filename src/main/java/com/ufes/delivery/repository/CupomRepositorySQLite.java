/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufes.delivery.repository;

import com.ufes.delivery.model.CupomDescontoPedido;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

/**
 *
 * @author raphael
 */
public class CupomRepositorySQLite implements ICupomRepository{

    private final String url;
    //Só escrevendo abaixo para eu não esquecer os campos
//    private String codigo;
//    private double percentual;
//    private LocalDateTime dataHoraInicio;
//    private LocalDateTime dataHoraFim;
//    String valor = localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
// Pelo q vi o sql aq no java não tem dateTime, achei essa solução acima na internet
    public CupomRepositorySQLite() {
        this.url = "jdbc:sqlite:Delivery.db";

        String sql = "CREATE TABLE IF NOT EXISTS tbCupom ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT," + "codigo TEXT NOT NULL,"
                + "percentual DOUBLE NOT NULL," + "dataHoraInicio TEXT NOT NULL" 
                + "dataHoraFim INTEGER NOT NULL," + ");";

        try (var conn = DriverManager.getConnection(this.url); var stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("ERRO!!! " + e.getMessage());
        }
    }
    
    @Override
    public void adicionarCupom(CupomDescontoPedido cupom) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void atualizarCupom(CupomDescontoPedido cupom) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void removerCupom(String codigo) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void removerCuponsExpirados() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Optional<CupomDescontoPedido> buscarCupom(String codigo) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Map<String, CupomDescontoPedido> getCuponsDisponiveis() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
