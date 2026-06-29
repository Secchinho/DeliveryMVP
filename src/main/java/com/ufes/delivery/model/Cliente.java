package com.ufes.delivery.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Cliente {
    private String cpf;
    private String nome;
    private String tipo;
    private double fidelidade;
    //private String logradouro;
    //private String bairro;
    //private String cidade;
    private List<Endereco> enderecos;

    public Cliente(String cpf, String nome, String tipo, double fidelidade) {
        validarTextoObrigatorio(nome, "Nome do cliente nao pode ser vazio");
        validarTextoObrigatorio(tipo, "Tipo do cliente nao pode ser vazio");
        
        if (fidelidade < 0) {
            throw new IllegalArgumentException("Fidelidade do cliente nao pode ser negativa");
        }
        
        this.enderecos = new ArrayList<>();
        this.cpf = cpf;
        this.nome = nome;
        this.tipo = tipo;
        this.fidelidade = fidelidade;
    }

    public String getNome() {
        return nome;
    }

    public String getCPF() {
        return cpf;
    }

    public String getTipo() {
        return tipo;
    }

    public double getFidelidade() {
        return fidelidade;
    }

    public void setFidelidade(double fidelidade) {
        if (fidelidade < 0) {
            throw new IllegalArgumentException("Fidelidade do cliente nao pode ser negativa");
        }

        this.fidelidade = fidelidade;
    }

    private void validarTextoObrigatorio(String valor, String mensagem) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(mensagem);
        }
    }
    
    public void addEndereco(Endereco endereco){
        if(endereco == null){
            throw new IllegalArgumentException("Insira um endereço.");
        }
        
        if(this.enderecos.size() >= 3){
            throw new RuntimeException("Não é possível adicionar mais que 3 endereços.");
        }
        
        this.enderecos.add(endereco);
    }
    
    public List<Endereco> getEnderecos(){
        return Collections.unmodifiableList(this.enderecos);
    }

    private Endereco getEnderecoPrincipal(){
        Endereco enderecoPrincipal = null;
        
        for(Endereco e : this.enderecos){
            if(e.isPadrao()){
                enderecoPrincipal = e;
            }
        }
        
        return Objects.requireNonNull(enderecoPrincipal,"Não foi encontrado um Endereço principal.");
    }
    
    public String getBairro(){
        return this.getEnderecoPrincipal().getBairro();
    }
    
    public void removerEndereco(Endereco endereco){
        if(endereco == null) throw new IllegalArgumentException("Insira um endereço.");
        
        for(Endereco e : this.enderecos){
            if(e.equals(endereco)){
                this.enderecos.remove(e);
                return;
            }
        }
        
        throw new RuntimeException("Não foi encontrado o Endereço informado");
    }
    @Override
    public String toString() {
        return "Cliente{"
                + "nome='" + nome + '\''
                + ", tipo='" + tipo + '\''
                + ", fidelidade=" + fidelidade
                + "}";
    }
}
