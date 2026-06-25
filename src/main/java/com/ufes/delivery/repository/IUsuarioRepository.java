/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ufes.delivery.repository;

import com.ufes.delivery.model.Usuario;
import java.util.List;

/**
 *
 * @author lucas
 */
public interface IUsuarioRepository {
    public List<Usuario> buscarPorNome(String nome);
    public void adicionar(Usuario usuario);
    public void removerPorNome(String nome);
    public List<Usuario> listarUsuarios();
}
