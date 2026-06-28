/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ufes.delivery.repository;

import com.ufes.delivery.model.Cliente;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author raphael
 */
public interface IClienteRepository {
    public List<Cliente> buscarPorNomeContendo(String nome);
    public void adicionar(Cliente cliente);
    public void atualizar(Cliente cliente);
    public void removerPorCPF(String cpf);
    public List<Cliente> listarClientes();
    public Optional<Cliente> getPorCPF(String cpf);
}
