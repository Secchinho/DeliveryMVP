/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufes.delivery.model;

/**
 *
 * @author lucas
 */
public class Endereco {
    private boolean padrao;
    private String logradouro;
    private int numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String uf;
    private String cep;
    private int id;

    public Endereco(boolean padrao, String logradouro, int numero, String complemento, String bairro, String cidade, String uf, String cep) {
        this.padrao = padrao;
        this.logradouro = logradouro;
        this.numero = numero;
        this.complemento = complemento;
        this.bairro = bairro;
        this.cidade = cidade;
        this.uf = uf;
        this.cep = cep;
    }

    public boolean isPadrao() {
        return padrao;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public int getNumero() {
        return numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public String getUf() {
        return uf;
    }

    public String getCep() {
        return cep;
    }
    
    public boolean equals(Endereco outroEndereco){
        if(outroEndereco == null) throw new IllegalArgumentException("Insira um endereço.");
        
        if(this.bairro.equals(outroEndereco.getBairro()) &&
                this.cep.equals(outroEndereco.getCep()) &&
                this.cidade.equals(outroEndereco.getCidade()) &&
                this.complemento.equals(outroEndereco.getComplemento()) &&
                this.logradouro.equals(outroEndereco.getLogradouro()) &&
                this.uf.equals(outroEndereco.getUf()) &&
                this.numero == outroEndereco.getNumero()
                ) return true;
        
        return false;
    }
    
}
