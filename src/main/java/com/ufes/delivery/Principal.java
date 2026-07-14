/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.ufes.delivery;

import br.ufes.logauditoria.CsvLoggerImpl;
import br.ufes.logauditoria.ILogger;
import com.ufes.delivery.desconto.pedido.AplicadorCupomPedidoService;
import com.ufes.delivery.presenters.CadastrarUsuarioPresenter;
import com.ufes.delivery.presenters.LoginPresenter;
import com.ufes.delivery.presenters.PagamentoService;
import com.ufes.delivery.presenters.PainelOperacionalPresenter;
import com.ufes.delivery.repository.ClienteRepositorySQLite;
import com.ufes.delivery.repository.CupomPedidoRepositorySQLite;
import com.ufes.delivery.repository.IClienteRepository;
import com.ufes.delivery.repository.ICupomRepository;
import com.ufes.delivery.repository.IPedidoRepository;
import com.ufes.delivery.repository.IProdutoRepository;
import com.ufes.delivery.repository.IUsuarioRepository;
import com.ufes.delivery.repository.PedidoRepositoryEmMemoria;
import com.ufes.delivery.repository.ProdutoRepositorySQLite;
import com.ufes.delivery.repository.UsuarioRepositorySQLite;
import com.ufes.delivery.view.CadastrarUsuarioView;
import com.ufes.delivery.view.ICadastrarUsuarioView;
import com.ufes.delivery.view.ILoginView;
import com.ufes.delivery.view.IPainelOperacionalView;
import com.ufes.delivery.view.LoginView;
import com.ufes.delivery.view.PainelOperacionalView;
import com.ufes.util.AutenticacaoUsuarioService;

/**
 * Classe responsável por montar (compor) todas as dependências da aplicação
 * - repositórios, serviços e telas - e iniciar o fluxo pela tela de login.
 * <p>
 * Ao término da autenticação, o painel operacional (US04) passa a ser a tela
 * principal da aplicação, a partir da qual todas as demais funcionalidades
 * (US05 a US09) são acessadas pelo menu Operação.
 *
 * @author lucas
 */
public class Principal {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // --- Logger de auditoria (US12) ---
        // Troque por JsonlLoggerImpl/XmlLoggerImpl se preferir outra
        // modalidade de persistência de auditoria. Deve haver somente uma
        // modalidade configurada por execução.
        ILogger logger = new CsvLoggerImpl("PedidoLog.csv");

        // --- Repositórios (persistência em SQLite, conforme especificação) ---
        IUsuarioRepository usuarioRepository = new UsuarioRepositorySQLite();
        IClienteRepository clienteRepository = new ClienteRepositorySQLite();
        IProdutoRepository produtoRepository = new ProdutoRepositorySQLite();
        ICupomRepository cupomRepository = new CupomPedidoRepositorySQLite();

        // O repositório de pedidos em SQLite ainda não está disponível neste
        // pacote (ver observações ao final da implementação); usa-se por ora
        // o repositório em memória para não interromper o fluxo do painel.
        IPedidoRepository pedidoRepository = new PedidoRepositoryEmMemoria();

        // --- Serviços de domínio ---
        AplicadorCupomPedidoService aplicadorCupomService
                = new AplicadorCupomPedidoService(cupomRepository, logger);
        PagamentoService pagamentoService = new PagamentoService();
        AutenticacaoUsuarioService autenticacaoService = new AutenticacaoUsuarioService(usuarioRepository);

        // --- Tela de cadastro de usuário (US02), reaproveitada pelo login ---
        ICadastrarUsuarioView cadastrarUsuarioView = new CadastrarUsuarioView();
        CadastrarUsuarioPresenter cadastrarUsuarioPresenter
                = new CadastrarUsuarioPresenter(cadastrarUsuarioView, usuarioRepository);

        // --- Painel operacional (US04), aberto após autenticação ---
        IPainelOperacionalView painelView = new PainelOperacionalView();
        PainelOperacionalPresenter painelPresenter = new PainelOperacionalPresenter(
                painelView, pedidoRepository, clienteRepository, produtoRepository,
                logger, aplicadorCupomService, pagamentoService);

        // --- Tela de login (US01) ---
        // O painel operacional é passado ao LoginPresenter, que o inicia
        // automaticamente assim que a autenticação for concluída com sucesso.
        ILoginView loginView = new LoginView();
        LoginPresenter loginPresenter = new LoginPresenter(
                loginView, autenticacaoService, cadastrarUsuarioPresenter, painelPresenter);

        loginPresenter.iniciar();
    }

}