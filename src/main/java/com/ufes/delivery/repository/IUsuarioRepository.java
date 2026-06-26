/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ufes.delivery.repository;

import com.ufes.delivery.model.Usuario;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author lucas
 */
public interface IUsuarioRepository {
    public List<Usuario> buscarPorNomeContendo(String nome);
    public void salvar(Usuario usuario);
    public void removerPorNomeUsuario(String userName);
    public List<Usuario> listarUsuarios();
    public Optional<Usuario> getPorUserName(String userName);
}
