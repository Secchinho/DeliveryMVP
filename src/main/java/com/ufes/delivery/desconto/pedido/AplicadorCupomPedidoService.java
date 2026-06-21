package com.ufes.delivery.desconto.pedido;

import br.ufes.logauditoria.ILogger;
import com.ufes.delivery.model.CupomDescontoPedido;
import com.ufes.delivery.model.Pedido;
import com.ufes.delivery.repository.ICupomRepository;
import com.ufes.util.Mapper;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

public class AplicadorCupomPedidoService {

    private ICupomRepository cupomRepository;
    private ILogger logger;

    public AplicadorCupomPedidoService(ICupomRepository cupomRepository, ILogger logger) {
        this.cupomRepository = Objects.requireNonNull(cupomRepository, "Repositorio de cupons nao pode ser nulo");
        this.logger = logger;
    }

    public void aplicarCupom(Pedido pedido, String codigoCupom, LocalDateTime dataHoraAplicacao) {
        Objects.requireNonNull(pedido, "Pedido nao pode ser nulo");
        Objects.requireNonNull(dataHoraAplicacao, "Data e hora de aplicacao nao podem ser nulas");

        if (codigoCupom == null || codigoCupom.isBlank()) {
            this.logger.criarLog(Mapper.convertToLog(pedido.getCodigoPedido(), 
                    "Exceção: Codigo do cupom nao pode ser vazio", pedido.getCliente().getNome()));
            throw new IllegalArgumentException("Codigo do cupom nao pode ser vazio");
        }

        Optional<CupomDescontoPedido> cupomEncontrado = cupomRepository.buscarCupom(codigoCupom);

        if (cupomEncontrado.isEmpty()) {
            this.logger.criarLog(Mapper.convertToLog(pedido.getCodigoPedido(), 
                    "Exceção: Cupom inexistente "  + codigoCupom, pedido.getCliente().getNome()));

            throw new IllegalArgumentException("Cupom inexistente: " + codigoCupom);
        }

        CupomDescontoPedido cupom = cupomEncontrado.get();

        if (dataHoraAplicacao.isBefore(cupom.getDataHoraInicio())
                || dataHoraAplicacao.isAfter(cupom.getDataHoraFim())) {
            this.logger.criarLog(Mapper.convertToLog(pedido.getCodigoPedido(), 
                    "Exceção: O pedido nao esta dentro da validade do cupom", pedido.getCliente().getNome()));
            throw new IllegalStateException("O pedido nao esta dentro da validade do cupom");
        }

        Optional<CupomDescontoPedido> cupomAtual = pedido.getCupomAplicado();

        if (cupomAtual.isPresent()) {
            if (cupom.getPercentual() <= cupomAtual.get().getPercentual()) {
                this.logger.criarLog(Mapper.convertToLog(pedido.getCodigoPedido(), 
                        "Exceção: O cupom " + codigoCupom + " nao tem um percentual maior que o cupom atual", 
                        pedido.getCliente().getNome()));
                throw new IllegalStateException(
                        "O cupom " + codigoCupom + " nao tem um percentual maior que o cupom atual");
            }
        }

        pedido.setCupomAplicado(cupom);
        this.logger.criarLog(Mapper.convertToLog(pedido.getCodigoPedido(), "Aplicar Cupom", pedido.getCliente().getNome()));
    }

    public void setLogger(ILogger logger) {
        this.logger = logger;
    }
    
}
