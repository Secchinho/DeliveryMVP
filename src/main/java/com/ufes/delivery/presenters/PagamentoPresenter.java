/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufes.delivery.presenters;

import com.ufes.delivery.command.PagamentoCommand;
import com.ufes.delivery.model.Cliente;
import com.ufes.delivery.model.Endereco;
import com.ufes.delivery.model.Pedido;
import com.ufes.delivery.view.IPagamentoView;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

/**
 *
 * @author lucas
 */
public class PagamentoPresenter {

    private static final DateTimeFormatter FORMATO_DATA_HORA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private IPagamentoView view;
    private PagamentoCommand command;
    private Pedido pedido;
    private ResultadoPagamento resultado;

    /**
     * Agora recebe o {@link ResultadoPagamento} produzido pelo
     * {@code PagamentoService}, e não apenas um boolean. Isso é necessário
     * para popular forma de pagamento, identificador da transação, data/hora,
     * valor pago e prazo estimado de entrega, exigidos pela US11 (Figura 12 e
     * Cenários 1, 3 e 4).
     */
    public PagamentoPresenter(IPagamentoView view, PagamentoCommand command,
            Pedido pedido, ResultadoPagamento resultado) {
        this.view = Objects.requireNonNull(view, "Insira uma View.");
        this.command = Objects.requireNonNull(command, "Insira um Command.");
        this.pedido = Objects.requireNonNull(pedido, "Insira um Pedido.");
        this.resultado = Objects.requireNonNull(resultado, "Insira um ResultadoPagamento.");

        this.configurarEventos();
        this.popularView();
    }

    public void iniciar() {
        this.view.getJanelaPrincipal().setVisible(true);
    }

    /**
     * Permite que o Command concreto acesse a View para executar a lógica de
     * fechar (que varia conforme o resultado do pagamento - aprovado ou
     * reprovado).
     */
    public IPagamentoView getView() {
        return this.view;
    }

    public Pedido getPedido() {
        return this.pedido;
    }

    public boolean isAprovado() {
        return this.resultado.isAprovado();
    }

    public ResultadoPagamento getResultado() {
        return this.resultado;
    }

    /**
     * Configura o listener do botão Fechar. O Presenter apenas delega o evento
     * ao Command injetado - a lógica de fechar (diferente para aprovado e
     * reprovado, conforme US11) fica encapsulada nas classes concretas do
     * Command, respeitando o padrão de projeto Command.
     */
    public void configurarEventos() {
        this.view.getFecharButton().addActionListener(v -> this.command.fechar());
    }

    /**
     * Popula todos os campos da View com base nos dados do Pedido e no
     * {@link ResultadoPagamento} da tentativa simulada, conforme as regras da
     * US11:
     *
     * - Resultado aprovado: exibe "Pagamento aprovado", "Pedido pronto para
     *   entrega", situação do pedido "Aguardando entrega" e os dados
     *   completos da transação (forma de pagamento, identificador, data/hora,
     *   valor pago e prazo estimado de entrega).
     * - Resultado reprovado: exibe "Pagamento reprovado", NÃO altera estoque
     *   nem situação do pedido (permanece "Em elaboração") e preserva os
     *   dados do pedido para nova tentativa.
     */
    private void popularView() {
        // --- Resumo do pedido (comum a aprovado e reprovado) ---
        this.view.setPedidoNumero(this.extrairNumeroPedido());
        this.view.setClienteNome(this.extrairNomeCliente());
        this.view.setEnderecoEntrega(this.extrairEnderecoEntrega());
        this.view.setTotalPedido(this.formatarMoeda(this.pedido.getValorPedido()));

        boolean aprovado = this.resultado.isAprovado();

        // --- Informações do pagamento (comuns: forma de pagamento e data/hora) ---
        this.view.setFormaPagamento(this.resultado.getFormaPagamento());
        this.view.setDataHoraPagamento(this.formatarDataHora(this.resultado.getDataHoraPagamento()));

        // --- Faixa de status do pagamento ---
        if (aprovado) {
            // US11 - Cenário 1: "Pagamento aprovado"
            this.view.setStatusPagamento("Pagamento aprovado", true);
            this.view.setStatusPedido("Pedido pronto para entrega");
            this.view.setSituacaoPagamento("Aprovado");
            this.view.setIdentificadorTransacao(this.resultado.getIdentificadorTransacao());
            this.view.setValorPago(this.formatarMoeda(this.resultado.getValorPago()));
            this.view.setSituacaoPedido("Aguardando entrega");
            this.view.setPrazoEstimado(this.formatarDataHora(this.resultado.getPrazoEstimadoEntrega()));
            this.view.setObservacao("Pagamento confirmado. O pedido foi encaminhado para entrega.");
        } else {
            // US11 - Cenário 2: deve informar a reprovação, sem alterar estoque
            // nem situação do pedido, preservando os dados para nova tentativa.
            this.view.setStatusPagamento("Pagamento reprovado", false);
            this.view.setStatusPedido("Pedido em elaboração");
            this.view.setSituacaoPagamento("Reprovado");
            this.view.setIdentificadorTransacao("-");
            this.view.setValorPago("-");
            this.view.setSituacaoPedido("Em elaboração");
            this.view.setPrazoEstimado("-");
            this.view.setObservacao("Pagamento reprovado. Os dados do pedido foram preservados para nova tentativa.");
        }
    }

    private String extrairNumeroPedido() {
        Object id = this.pedido.getCodigoPedido();
        return (id != null) ? String.valueOf(id) : "-";
    }

    private String extrairNomeCliente() {
        Cliente cliente = this.pedido.getCliente();
        return (cliente != null && cliente.getNome() != null)
            ? cliente.getNome()
            : "-";
    }

    private String extrairEnderecoEntrega() {
        Cliente cliente = this.pedido.getCliente();
        if (cliente == null) {
            return "-";
        }

        Endereco endereco = null;
        for (Endereco e : cliente.getEnderecos()) {
            if (e.isPadrao()) {
                endereco = e;
                break;
            }
        }
        if (endereco == null) {
            return "-";
        }

        StringBuilder sb = new StringBuilder();
        if (endereco.getLogradouro() != null && !endereco.getLogradouro().isBlank()) {
            sb.append(endereco.getLogradouro());
        }

        sb.append(", ").append(endereco.getNumero());

        if (endereco.getComplemento() != null && !endereco.getComplemento().isBlank()) {
            sb.append(" - ").append(endereco.getComplemento());
        }
        if (endereco.getBairro() != null && !endereco.getBairro().isBlank()) {
            sb.append(" - ").append(endereco.getBairro());
        }
        if (endereco.getCidade() != null && !endereco.getCidade().isBlank()) {
            sb.append(" - ").append(endereco.getCidade());
        }
        if (endereco.getUf() != null && !endereco.getUf().isBlank()) {
            sb.append("/").append(endereco.getUf());
        }
        if (endereco.getCep() != null && !endereco.getCep().isBlank()) {
            sb.append(" - CEP ").append(endereco.getCep());
        }
        return sb.length() > 0 ? sb.toString() : "-";
    }

    private String formatarMoeda(double valor) {
        NumberFormat formato = NumberFormat.getCurrencyInstance(Locale.of("pt", "BR"));
        formato.setRoundingMode(RoundingMode.HALF_UP);
        return formato.format(valor);
    }

    private String formatarDataHora(java.time.LocalDateTime dataHora) {
        return (dataHora != null) ? dataHora.format(FORMATO_DATA_HORA) : "-";
    }
}