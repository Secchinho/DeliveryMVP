/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufes.util;

import com.ufes.delivery.model.Usuario;
import com.ufes.delivery.repository.IUsuarioRepository;
import java.util.Optional;

/**
 * Serviço de domínio responsável exclusivamente por autenticar um usuário
 * e validar sua situação de autorização (US01 - Autenticar usuário e
 * iniciar sessão).
 *
 * Responsabilidade única: este serviço NÃO cuida de cadastro, autorização
 * administrativa ou apresentação de mensagens — essas responsabilidades
 * pertencem a CadastrarUsuarioService, GerenciarUsuariosPresenter e às
 * Views/Presenters, respectivamente (conforme o diagrama de classes e o
 * padrão MVP Passive View adotado no projeto).
 *
 * @author lucas
 */
public class AutenticacaoUsuarioService {

    private static final String SITUACAO_AUTORIZADO = "Autorizado";

    private final IUsuarioRepository usuarioRepository;

    public AutenticacaoUsuarioService(IUsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario autenticarUsuario(String nome, String senha) {
        if(nome == null || nome.isBlank() || senha == null || senha.isBlank()){
            throw new IllegalArgumentException("Insira valores válidos.");
        }
        
        Optional<Usuario> usuario = usuarioRepository.getPorUserName(nome);

        if (usuario.isEmpty() || !senhaCorresponde(usuario.get(), senha)) {
            throw new RuntimeException("Usuário inválido!");
        }

        if (!usuarioAutorizado(usuario.get())) {
            throw new RuntimeException("Usuário não autorizado!");
        }

        return usuario.get();
    }

    private boolean senhaCorresponde(Usuario usuario, String senha) {
        return usuario.getSenha() != null && usuario.getSenha().equals(senha);
    }

    private boolean usuarioAutorizado(Usuario usuario) {
        return SITUACAO_AUTORIZADO.equals(usuario.getSituacao());
    }
}
