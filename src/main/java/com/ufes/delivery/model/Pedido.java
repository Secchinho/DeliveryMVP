package com.ufes.delivery.model;

import br.ufes.logauditoria.ILogger;
import com.ufes.delivery.configuracao.ConfiguracaoService;
import com.ufes.util.Mapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.time.LocalDateTime;
import java.util.Optional;

public class Pedido {

    private double taxaEntrega = ConfiguracaoService.getTaxaEntregaPadrao();
    private List<Item> itens = new ArrayList<>();
    private Cliente cliente;
    private List<CupomDescontoEntrega> cuponsDescontoEntrega = new ArrayList<>();
    private LocalDateTime data;
    private int codigoPedido;
    private ILogger logger;

    private CupomDescontoPedido cupomPedidoAplicado;

    public Pedido(LocalDateTime data, Cliente cliente, int codigoPedido, ILogger logger) {
        if (data == null) {
            throw new IllegalArgumentException("Data do pedido deve ser informada");
        }

        if (cliente == null) {
            throw new IllegalArgumentException("Cliente do pedido deve ser informado");
        }

        this.cliente = cliente;
        this.data = data;
        this.codigoPedido = codigoPedido;
        this.logger = logger;
    }

    public void adicionarItem(Item objeto) {
        if (objeto == null) {
            this.logger.criarLog(Mapper.convertToLog(this.getCodigoPedido(),
                    "Exceção: Item do Pedido deve ser Informado", this.getCliente().getNome()));
            throw new IllegalArgumentException("Item do pedido deve ser informado");
        }

        itens.add(objeto);
    }

    public int getCodigoPedido() {
        return codigoPedido;
    }

    public double getValorPedido() {
        double valor = 0;
        for (Item item : itens) {
            valor += item.valorTotal();
        }

        this.logger.criarLog(Mapper.convertToLog(this.getCodigoPedido(),
                "get Valor Pedido", this.getCliente().getNome()));
        return valor;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public List<Item> getItens() {
        return Collections.unmodifiableList(itens);
    }

    public double getTaxaEntrega() {
        return taxaEntrega;
    }

    public void setLogger(ILogger logger) {
        this.logger = logger;
    }

    public double getTotalDescontosTaxaEntrega() {
        double desconto = 0;

        for (CupomDescontoEntrega cupom : cuponsDescontoEntrega) {
            desconto += cupom.getValorDesconto();
        }

        return desconto;
    }

    public List<CupomDescontoEntrega> getCupomDescontoEntrega() {
        return Collections.unmodifiableList(cuponsDescontoEntrega);
    }

    public void limparCuponsDescontoEntrega() {
        cuponsDescontoEntrega.clear();
    }

    public void adicionarCupomDescontoEntrega(CupomDescontoEntrega cupom) {
        if (cupom == null) {
            throw new IllegalArgumentException("Cupom de desconto da entrega deve ser informado");
        }

        double totalDescontosAposAdicionar = getTotalDescontosTaxaEntrega() + cupom.getValorDesconto();
        double limiteAplicavel = taxaEntrega;

        if (cupom.getValorDesconto() < 0) {
            this.logger.criarLog(Mapper.convertToLog(this.getCodigoPedido(),
                    "Exceção: Desconto na taxa não pode ser negativo", this.getCliente().getNome()));

            throw new IllegalArgumentException("Desconto na taxa de entrega nao pode ser negativo");
        }

        if (totalDescontosAposAdicionar > limiteAplicavel) {
            this.logger.criarLog(Mapper.convertToLog(this.getCodigoPedido(),
                    "Exceção: Desconto total na taxa de entrega não pode ultrapassar", this.getCliente().getNome()));
            throw new IllegalStateException(
                    "Desconto total na taxa de entrega nao pode ultrapassar " + limiteAplicavel);
        }

        cuponsDescontoEntrega.add(cupom);
    }

    public double getTaxaEntregaComDesconto() {
        double taxaComDesconto = taxaEntrega - getTotalDescontosTaxaEntrega();
        if (taxaComDesconto < 0) {
            return 0;
        }
        return taxaComDesconto;
    }

    public double calcularValorTotal() {
        double valorTotal = getValorPedido() + getTaxaEntregaComDesconto();
        Optional<CupomDescontoPedido> cupomAplicado = getCupomAplicado();

        if (cupomAplicado.isPresent()) {
            CupomDescontoPedido cupom = cupomAplicado.get();
            return valorTotal - valorTotal * cupom.getPercentual() / 100;
        }

        this.logger.criarLog(Mapper.convertToLog(this.getCodigoPedido(), "Calcular Valor Total", this.getCliente().getNome()));

        return valorTotal;
    }

    public LocalDateTime getData() {
        return data;
    }

    public Optional<CupomDescontoPedido> getCupomAplicado() {
        return Optional.ofNullable(cupomPedidoAplicado);
    }

    public void setCupomAplicado(CupomDescontoPedido cupomPedidoAplicado) {
        if (cupomPedidoAplicado == null) {
            throw new IllegalArgumentException("Cupom do pedido deve ser informado");
        }

        this.cupomPedidoAplicado = cupomPedidoAplicado;
    }

    @Override
    public String toString() {
        return "Pedido{"
                + "data=" + data
                + ", cliente=" + cliente
                + ", itens=" + itens
                + ", taxaEntrega=" + taxaEntrega
                + ", cuponsDescontoEntrega=" + cuponsDescontoEntrega
                + ", cupomPedidoAplicado=" + cupomPedidoAplicado
                + ", valorPedido=" + getValorPedido()
                + ", totalDescontosTaxaEntrega=" + getTotalDescontosTaxaEntrega()
                + ", valorTotal=" + calcularValorTotal()
                + "}";
    }
}
