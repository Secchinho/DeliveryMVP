/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.ufes.delivery;

import br.ufes.logauditoria.CsvLoggerImpl;
import br.ufes.logauditoria.ILogger;
import com.ufes.delivery.desconto.pedido.AplicadorCupomPedidoService;
import com.ufes.delivery.presenters.BuscarClientePresenter;
import com.ufes.delivery.presenters.BuscarProdutoPresenter;
import com.ufes.delivery.presenters.CadastrarUsuarioPresenter;
import com.ufes.delivery.presenters.ClientePresenter;
import com.ufes.delivery.presenters.LoginPresenter;
import com.ufes.delivery.presenters.MovimentacaoEstoquePresenter;
import com.ufes.delivery.presenters.PedidoPresenter;
import com.ufes.delivery.repository.ClienteRepositorySQLite;
import com.ufes.delivery.repository.CupomPedidoRepositoryEmMemoria;
import com.ufes.delivery.repository.IClienteRepository;
import com.ufes.delivery.repository.ICupomRepository;
import com.ufes.delivery.repository.IPedidoRepository;
import com.ufes.delivery.repository.IProdutoRepository;
import com.ufes.delivery.repository.PedidoRepositoryEmMemoria;
import com.ufes.delivery.repository.ProdutoRepositorySQLite;
import com.ufes.delivery.repository.UsuarioRepositorySQLite;
import com.ufes.delivery.state.CriarPedidoState;
import com.ufes.delivery.view.BuscaClienteView;
import com.ufes.delivery.view.BuscarProdutoView;
import com.ufes.delivery.view.CadastrarUsuarioView;
import com.ufes.delivery.view.ClienteView;
import com.ufes.delivery.view.IBuscarClienteView;
import com.ufes.delivery.view.IBuscarProdutoView;
import com.ufes.delivery.view.ICadastrarUsuarioView;
import com.ufes.delivery.view.IClienteView;
import com.ufes.delivery.view.ILoginView;
import com.ufes.delivery.view.IMovimentacaoEstoqueView;
import com.ufes.delivery.view.IPagamentoView;
import com.ufes.delivery.view.IPedidoView;
import com.ufes.delivery.view.IProdutoView;
import com.ufes.delivery.view.LoginView;
import com.ufes.delivery.view.MovimentacaoEstoqueView;
import com.ufes.delivery.view.PagamentoView;
import com.ufes.delivery.view.PedidoView;
import com.ufes.delivery.view.ProdutoView;
import com.ufes.util.AutenticacaoUsuarioService;

/**
 *
 * @author lucas
 */
public class Principal {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        // --- Tela de pedido (US09 / US10) ---
//        // Logger de auditoria - troque por JsonlLoggerImpl/XmlLoggerImpl se preferir.
//        ILogger logger = new CsvLoggerImpl("PedidoLog.csv");
//
//        // Repository em memória (substituir por PedidoRepositorySQLite quando disponível).
//        IPedidoRepository pedidoRepository = new PedidoRepositoryEmMemoria();
//
//        // Service de aplicação de cupom com repositório em memória.
//        ICupomRepository cupomRepository = new CupomPedidoRepositoryEmMemoria();
//        AplicadorCupomPedidoService aplicadorCupom
//                = new AplicadorCupomPedidoService(cupomRepository, logger);
//
//        IPedidoView pedidoView = new PedidoView();
//        IClienteRepository clienteRepository = new ClienteRepositorySQLite();
//        IProdutoRepository produtoRepository = new ProdutoRepositorySQLite();
//        PedidoPresenter pedidoPresenter = new PedidoPresenter(
//                pedidoView, pedidoRepository, clienteRepository,
//                produtoRepository, logger, aplicadorCupom);
//        pedidoPresenter.setEstado(new CriarPedidoState(pedidoPresenter));
//        pedidoPresenter.iniciar();

    }

}
