/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ufes.delivery.repository;

import com.ufes.delivery.model.Produto;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author raphael
 */
public interface IProdutoRepository {

    public List<Produto> buscarPorNome(String nome);

    public List<Produto> buscarPorCategoria(String categoria);

    public void adicionar(Produto produto);

    public void atualizar(Produto produto);

    public void removerPorCodigo(String codigo);

    public List<Produto> listarProdutos();

    public Optional<Produto> getPorCodigo(String codigo);
}
