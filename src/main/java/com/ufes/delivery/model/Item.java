package com.ufes.delivery.model;

public class Item {
    private int quantidade;
    private double valorUnitario;
    private Produto produto;

    public Item(String nome, int quantidade, double valorUnitario, String tipo) {
        validarTextoObrigatorio(nome, "Nome do item nao pode ser vazio");
        validarTextoObrigatorio(tipo, "Tipo do item nao pode ser vazio");

        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade do item deve ser maior que zero");
        }

        if (valorUnitario < 0) {
            throw new IllegalArgumentException("Valor unitario do item nao pode ser negativo");
        }

        this.quantidade = quantidade;
        this.valorUnitario = valorUnitario;
    }

    public double valorTotal() {
        return valorUnitario * quantidade;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public double getValorUnitario() {
        return valorUnitario;
    }
    
    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }
    
    public String getNomeProuto(){
        return this.produto.getNome();
    }
    
    public String getTipo(){
        return this.produto.getCategoria();
    }
    
    private void validarTextoObrigatorio(String valor, String mensagem) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(mensagem);
        }
    }
    
    @Override
    public String toString() {
        return "Item{" + "quantidade=" + quantidade + ", valorUnitario=" + valorUnitario + ", produto=" + produto + '}';
    }

    
}
