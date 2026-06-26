/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufes.singleton;

/**
 *
 * @author raphael
 */
public class ConexaoSQLite {
    private static ConexaoSQLite instancia = null;
    private String url;
    
    private ConexaoSQLite(){
        url = "jdbc:sqlite:Delivery.db";
    }
    
    public static synchronized ConexaoSQLite getInstacia(){
        if(instancia == null){
            instancia = new ConexaoSQLite();
        }
        
        return instancia;
    }
    
    public String getURL(){
        return url;
    }
}
