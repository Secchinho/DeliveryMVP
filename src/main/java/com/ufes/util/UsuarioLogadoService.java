package com.ufes.util; // Adapte para o pacote correto do seu projeto

import com.ufes.delivery.model.Usuario;

/**
 * Serviço responsável por manter em memória o usuário logado na sessão atual.
 * Utiliza o padrão Singleton para garantir uma única instância em toda a aplicação.
 */
public class UsuarioLogadoService {
    private static UsuarioLogadoService instancia;
    private Usuario usuarioAtual;

    private UsuarioLogadoService() {
    }

    public static UsuarioLogadoService getInstance() {
        if (instancia == null) {
            instancia = new UsuarioLogadoService();
        }
        return instancia;
    }

    public void logar(Usuario usuario) {
        this.usuarioAtual = usuario;
    }

    public String getNome() {
        return this.usuarioAtual.getNome();
    }
    
    public String getSituacao() {
        return this.usuarioAtual.getSituacao();
    }

    public String getUserName() {
        return this.usuarioAtual.getUserName();
    }

    public int getTipo(){
        return this.usuarioAtual.getTipo();
    }

    public void deslogar() {
        this.usuarioAtual = null;
    }

    public boolean isLogado() {
        return this.usuarioAtual != null;
    }
}