/*
 * POC Delivery com Auditoria - CadastrarUsuarioPresenter
 * Corrigido conforme Diagrama de Classes e Especificação de Requisitos (US02).
 */
package com.ufes.delivery.presenters;

import com.ufes.delivery.repository.IUsuarioRepository;
import com.ufes.delivery.model.Usuario;
import com.ufes.util.AutenticacaoUsuarioService;
import com.ufes.delivery.view.ICadastrarUsuarioView;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.swing.JOptionPane;

/**
 * Presenter responsável por intermediar a view de cadastro de usuário
 * (ICadastrarUsuarioView), o serviço de cadastro (CadastrarUsuarioService)
 * e o repositório de usuários (IUsuarioRepository), seguindo o padrão
 * MVP Passive View.
 *
 * Responsabilidades (conforme Diagrama de Classes):
 * - Ler dados da view e encaminhar ao serviço para validação e persistência;
 * - Validar campos obrigatórios e formatos antes de delegar ao serviço;
 * - Verificar duplicidade de nome de usuário via repositório;
 * - Determinar perfil e situação conforme regras de primeiro cadastro;
 * - Exibir mensagens de validação em português associadas ao campo violado;
 * - Fechar a view e retornar ao fluxo de login após cadastro ou cancelamento.
 *
 * As regras de validação seguem a US02 da Especificação de Requisitos.
 */
public class CadastrarUsuarioPresenter {

    private ICadastrarUsuarioView view;
    private IUsuarioRepository usuarioRepository;

    /**
     * Constrói o presenter com as dependências definidas no Diagrama de Classes.
     *
     * @param view              interface da tela de cadastro de usuário
     * @param cadastrarService  serviço de cadastro responsável por validar e persistir
     * @param usuarioRepository repositório para consultas de unicidade e listagem
     * @throws NullPointerException se qualquer dependência for nula
     */
    public CadastrarUsuarioPresenter(
            ICadastrarUsuarioView view,
            IUsuarioRepository usuarioRepository) {

        this.view = Objects.requireNonNull(view,
                "A view de cadastro não pode ser nula.");
        this.usuarioRepository = Objects.requireNonNull(usuarioRepository,
                "O repositório de usuários não pode ser nulo.");
        this.configurarEventos();
    }

    /**
     * Configura os listeners dos componentes da view conforme definido
     * na interface ICadastrarUsuarioView do Diagrama de Classes.
     *
     * - getConfirmarButton() -> cadastrarUsuario()
     * - getCancelarButton()  -> cancelar()
     */
    private void configurarEventos() {
        this.view.getConfirmarButton().addActionListener(e -> cadastrarUsuario());
        this.view.getCancelarButton().addActionListener(e -> cancelar());
    }

    /**
     * Realiza o cadastro do usuário conforme os cenários da US02:
     *
     * Cenário 1 - Primeiro usuário: perfil Administrador, situação Autorizado.
     * Cenário 2 - Usuário posterior: perfil Atendente, situação Pendente.
     * Cenário 3 - Rejeitar nome de usuário duplicado.
     * Cenário 4 - Rejeitar dados obrigatórios ausentes.
     * Cenário 5 - Retornar ao login após cadastro ou cancelamento.
     *
     * Regras de validação de dados de entrada (US02):
     * - Nome: obrigatório, 2 a 120 caracteres, letras, espaços, apóstrofos e hífens.
     * - Nome de usuário: obrigatório, 3 a 30 caracteres, apenas letras minúsculas
     *   e algarismos, sem espaços, único entre todos os cadastros.
     * - Senha: obrigatória, 8 a 64 caracteres, não armazenada em texto aberto.
     */
    private void cadastrarUsuario() {
        String nomeCivil = this.view.getCampoNomeCivil().getText().trim();
        String nomeUsuario = this.view.getCampoNomeUsuario().getText().trim();
        String senha = new String(this.view.getCampoSenha().getPassword());

        // --- Validação de campos obrigatórios e formatos (Cenário 4) ---
        StringBuilder erros = new StringBuilder();

        validarNomeCivil(nomeCivil, erros);
        validarNomeUsuario(nomeUsuario, erros);
        validarSenha(senha, erros);

        if (erros.length() > 0) {
            exibirMensagem(erros.toString(), "Campos obrigatórios", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // --- Verificar duplicidade de nome de usuário (Cenário 3) ---
        if (isNomeUsuarioDuplicado(nomeUsuario)) {
            exibirMensagem(
                    "Nome de usuário já está em uso.",
                    "Duplicidade",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // --- Determinar perfil e situação (Cenários 1 e 2) ---
        boolean primeiroUsuario = isPrimeiroUsuario();
        String tipo = primeiroUsuario ? "Administrador" : "Atendente";
        String situacao = primeiroUsuario ? "Autorizado" : "Pendente";

        // --- Delegar ao serviço de cadastro ---
        try {
            Usuario usuario = new Usuario(nomeCivil, nomeUsuario, senha);
            usuario.setSituacao(situacao);

            try{
                this.usuarioRepository.adicionar(usuario);
            }catch(IllegalArgumentException e){
                exibirMensagem(
                    "Erro ao cadastrar usuário: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            }

            exibirMensagem(
                    "Usuário cadastrado com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);

            // Cenário 5 - Retornar ao login após cadastro confirmado
            cancelar();

        } catch (Exception ex) {
            exibirMensagem(
                    "Erro ao cadastrar usuário: " + ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Cancela o cadastro, limpa os campos da view e fecha a janela,
     * retornando ao fluxo de login (Cenário 5).
     */
    private void cancelar() {
        this.view.getCampoNomeCivil().setText("");
        this.view.getCampoNomeUsuario().setText("");
        this.view.getCampoSenha().setText("");
        this.view.getJanelaPrincipal().dispose();
    }

    /**
     * Torna a janela de cadastro visível.
     */
    public void iniciar() {
        this.view.getJanelaPrincipal().setVisible(true);
    }

    // ===================== Métodos auxiliares de validação =====================

    /**
     * Valida o nome civil conforme regra da US02:
     * obrigatório, 2 a 120 caracteres, letras, espaços, apóstrofos e hífens.
     */
    private void validarNomeCivil(String nomeCivil, StringBuilder erros) {
        if (nomeCivil.isEmpty()) {
            erros.append("Nome civil é obrigatório.\n");
            return;
        }
        if (nomeCivil.length() < 2 || nomeCivil.length() > 120) {
            erros.append("Nome civil deve conter de 2 a 120 caracteres.\n");
            return;
        }
        if (!nomeCivil.matches("[\\p{L}\\s'\\-]+")) {
            erros.append("Nome civil deve conter apenas letras, espaços, apóstrofos e hífens.\n");
        }
    }

    /**
     * Valida o nome de usuário conforme regras da US02 e US01:
     * obrigatório, 3 a 30 caracteres, apenas letras minúsculas e algarismos,
     * sem espaços (mesmo formato exigido na autenticação).
     */
    private void validarNomeUsuario(String nomeUsuario, StringBuilder erros) {
        if (nomeUsuario.isEmpty()) {
            erros.append("Nome de usuário é obrigatório.\n");
            return;
        }
        if (nomeUsuario.length() < 3 || nomeUsuario.length() > 30) {
            erros.append("Nome de usuário deve conter de 3 a 30 caracteres.\n");
            return;
        }
        if (!nomeUsuario.matches("[a-z0-9]+")) {
            erros.append("Nome de usuário deve conter apenas letras minúsculas e algarismos, sem espaços.\n");
        }
    }

    /**
     * Valida a senha conforme regras da US02 e US01:
     * obrigatória, 8 a 64 caracteres.
     * A senha não é armazenada nem apresentada em texto aberto.
     */
    private void validarSenha(String senha, StringBuilder erros) {
        if (senha.isEmpty()) {
            erros.append("Senha é obrigatória.\n");
            return;
        }
        if (senha.length() < 8) {
            erros.append("Senha deve conter no mínimo 8 caracteres.\n");
            return;
        }
        if (senha.length() > 64) {
            erros.append("Senha deve conter no máximo 64 caracteres.\n");
        }
    }

    /**
     * Verifica se o nome de usuário já está cadastrado (Cenário 3).
     * A verificação é feita em todos os cadastros, inclusive pendentes
     * ou não autorizados, conforme requisito da US02.
     */
    private boolean isNomeUsuarioDuplicado(String nomeUsuario) {
        Optional<Usuario> existente = this.usuarioRepository.getPorUserName(nomeUsuario);
        return existente.isPresent();
    }

    /**
     * Verifica se não há nenhum usuário persistido, para determinar
     * se o cadastro atual é o primeiro do sistema (Cenário 1).
     */
    private boolean isPrimeiroUsuario() {
        List<Usuario> usuarios = this.usuarioRepository.listarUsuarios();
        return usuarios.isEmpty();
    }

    /**
     * Exibe mensagem ao usuário utilizando a janela principal da view
     * como componente pai do diálogo.
     */
    private void exibirMensagem(String mensagem, String titulo, int tipo) {
        JOptionPane.showMessageDialog(
                this.view.getJanelaPrincipal(),
                mensagem,
                titulo,
                tipo);
    }
}
