/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufes.delivery.model;
/**
 *
 * @author raphael
 */
public class Usuario {
    private String nome;
    private String userName;
    private int tipo; //Adotamos inteiros para o tipo. 0 = Atendente e 1 = Admin
    private String senha;
    private String situacao;
    private boolean autorizado;
    
    public Usuario(String nome, String userName, String senha){
        this.nome = nome;
        this.userName = userName;
        this.senha = senha;
        this.tipo = 0; 
        this.situacao = "Pendente";
    }
    
    public String getSenha() {
        return senha;
    }

    public String getNome() {
        return nome;
    }
    
    public String getSituacao() {
        return situacao;
    }
    
    public int getTipo() {
        return tipo;
    }

    public String getUserName() {
        return userName;
    }

    public void setSituacao(String situacao){
        this.situacao = situacao;
    }
    
    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public boolean isAutorizado() {
        return autorizado;
    }

    public void setAutorizado(boolean autorizado) {
        this.autorizado = autorizado;
    }
}
