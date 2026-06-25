/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufes.util;

import com.ufes.delivery.model.Usuario;
import com.ufes.delivery.repository.IUsuarioRepository;
import java.util.List;

/**
 *
 * @author lucas
 */
public class AutenticacaoUsuarioService {
    IUsuarioRepository usuarioRepository;
    
    public AutenticacaoUsuarioService(IUsuarioRepository usuarioRepository){
        this.usuarioRepository = usuarioRepository;
    }
    
    public boolean autenticarUsuario(String nome, String senha){
        boolean encontrado = false;
        List<Usuario> usuarios = this.usuarioRepository.buscarPorNome(nome);
        
        for(Usuario u : usuarios){
            if(u.getSenha().equals(senha)){
                encontrado = true;
                break;
            }
        }
        
        return encontrado;
    }
}
