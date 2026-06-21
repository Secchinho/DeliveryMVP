package com.ufes.delivery.desconto.taxa.entrega;

import br.ufes.logauditoria.ILogger;
import com.ufes.delivery.model.CupomDescontoEntrega;
import com.ufes.delivery.model.Pedido;
import com.ufes.util.Mapper;
import java.util.ArrayList;
import java.util.List;

public class CalculadoraTaxaDescontoPedidoService {
    private List<IFormaDescontoTaxaEntrega> metodosDeDesconto;
    private ILogger logger;

    public CalculadoraTaxaDescontoPedidoService(ILogger logger) {
        metodosDeDesconto = new ArrayList<>();

        metodosDeDesconto.add(new FormaDescontoTaxaPorBairro());
        metodosDeDesconto.add(new FormaDescontoTaxaPorTipoCliente());
        metodosDeDesconto.add(new FormaDescontoTipoItem());
        metodosDeDesconto.add(new FormaDescontoValorPedido());
        this.logger = logger;
    }

    public void setLogger(ILogger logger) {
        this.logger = logger;
    }
    
    public void calcularDesconto(Pedido pedido) {
        pedido.limparCuponsDescontoEntrega();

        double limiteAplicavel = pedido.getTaxaEntrega();

        for (IFormaDescontoTaxaEntrega formaDescontoTaxaEntrega : metodosDeDesconto) {
            double totalDescontos = pedido.getTotalDescontosTaxaEntrega();

            if (formaDescontoTaxaEntrega.seAplica(pedido) && totalDescontos < limiteAplicavel) {
                CupomDescontoEntrega cupom = formaDescontoTaxaEntrega.calcularDesconto(pedido);
                double limiteRestante = limiteAplicavel - totalDescontos;
                double valorAplicado = Math.min(cupom.getValorDesconto(), limiteRestante);
                cupom.aplicar(valorAplicado);
                    
                this.logger.criarLog(Mapper.convertToLog(pedido.getCodigoPedido(), 
                        "Calcular Desconto", 
                        pedido.getCliente().getNome()));
                if (cupom.getValorDesconto() > 0) {
                    pedido.adicionarCupomDescontoEntrega(cupom);
                }
            }
        }
    }
}
