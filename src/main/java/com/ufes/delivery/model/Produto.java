/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufes.delivery.model;

/**
 *
 * @author raphael
 */
public class Produto {
    private String nome;
    private String codigo;
    private String categoria;
    private double precoUnitario;
    private int quantidadeInicial;
    private int id;

    public Produto(String nome, String codigo, String categoria, double precoUnitario, int quantidadeInicial) {
        this.nome = nome;
        this.codigo = codigo;
        this.categoria = categoria;
        this.precoUnitario = precoUnitario;
        this.quantidadeInicial = quantidadeInicial;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public double getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(double precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public int getQuantidadeInicial() {
        return quantidadeInicial;
    }

    public void setQuantidadeInicial(int quantidadeInicial) {
        this.quantidadeInicial = quantidadeInicial;
    }
    
    
}
