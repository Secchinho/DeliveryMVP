/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.ufes.delivery.presenters;

import com.ufes.delivery.model.Pedido;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Serviço responsável por simular uma tentativa de pagamento (US11).
 *
 * <p>Produz um {@link ResultadoPagamento} contendo:
 * <ul>
 *   <li>resultado (aprovado/reprovado), com 50% para cada;</li>
 *   <li>forma de pagamento, com 25% para cada uma das quatro alternativas
 *       (Open Finance, PIX chave, PIX QR Code, Cartão de crédito);</li>
 *   <li>identificador único da transação (somente se aprovado);</li>
 *   <li>data e hora do pagamento;</li>
 *   <li>valor pago, igual ao total do pedido calculado no instante da
 *       tentativa (somente se aprovado);</li>
 *   <li>prazo estimado de entrega, entre o instante da aprovação e o mesmo
 *       dia do mês subsequente (somente se aprovado).</li>
 * </ul>
 * </p>
 *
 * <p><b>Reprovação</b> não altera estoque, situação do pedido, total do pedido
 * ou dados do cliente, e preserva o pedido para nova tentativa. Essa
 * responsabilidade (baixa de estoque, mudança de estado do pedido e registro
 * de auditoria, em transação única) fica a cargo de um orquestrador/camada de
 * aplicação que consuma o {@code ResultadoPagamento} retornado.</p>
 *
 * @author lucas
 */
public class PagamentoService {

    private final ISorteioPagamento sorteio;
    private final AtomicLong sequencialTransacao = new AtomicLong(0);

    public PagamentoService() {
        this(new SorteioPagamentoAleatorio());
    }

    /**
     * Permite injetar uma fonte de aleatoriedade própria, em especial para
     * testes determinísticos (DoD da US11).
     */
    public PagamentoService(ISorteioPagamento sorteio) {
        this.sorteio = Objects.requireNonNull(sorteio, "Insira uma fonte de sorteio.");
    }

    /**
     * Processa a tentativa simulada de pagamento para o pedido informado.
     *
     * <p>O método não altera o pedido: apenas deriva o resultado e os valores
     * que devem ser exibidos. Quem chama é responsável por aplicar os efeitos
     * colaterais (baixa de estoque, mudança de situação, auditoria) quando
     * {@code resultado.isAprovado() == true}, em transação única.</p>
     *
     * @param pedido pedido cujo pagamento está sendo simulado. Não pode ser
     *               nulo.
     * @return resultado da tentativa simulada.
     */
    public ResultadoPagamento processar(Pedido pedido) {
        Objects.requireNonNull(pedido, "Insira um Pedido.");

        // A data/hora do pagamento é o instante em que a tentativa ocorre.
        LocalDateTime dataHoraPagamento = LocalDateTime.now();

        // Sorteio do resultado (50/50) e da forma de pagamento (25% cada).
        boolean aprovado = this.sorteio.sortearAprovacao();
        String formaPagamento = this.sorteio.sortearFormaPagamento();

        if (!aprovado) {
            // Reprovação: não há identificador, valor pago nem prazo.
            // Estoque e situação do pedido permanecem inalterados (US11).
            return new ResultadoPagamento(
                    false,
                    formaPagamento,
                    null,
                    dataHoraPagamento,
                    0.0,
                    null
            );
        }

        // Aprovação: gera identificador único, valor pago (= total do pedido)
        // e prazo estimado de entrega.
        String identificadorTransacao = this.gerarIdentificadorTransacao(dataHoraPagamento);
        double valorPago = pedido.calcularValorTotal();
        LocalDateTime prazoEstimadoEntrega = this.sorteio.sortearPrazoEntrega(dataHoraPagamento);

        return new ResultadoPagamento(
                true,
                formaPagamento,
                identificadorTransacao,
                dataHoraPagamento,
                valorPago,
                prazoEstimadoEntrega
        );
    }

    /**
     * Gera um identificador legível e único para a transação aprovada.
     *
     * <p>Formato: {@code TRX-yyyyMMddHHmmss-NNNNNN-XXXX}, onde:
     * <ul>
     *   <li>{@code yyyyMMddHHmmss} é o instante da aprovação;</li>
     *   <li>{@code NNNNNN} é um sequencial atômico crescente dentro da JVM,
     *       garantindo unicidade mesmo se duas aprovações ocorrerem no mesmo
     *       segundo;</li>
     *   <li>{@code XXXX} é um sufixo aleatório curto derivado de UUID, para
     *       reduzir ainda mais a probabilidade de colisão entre instâncias
     *       distintas da aplicação.</li>
     * </ul>
     * </p>
     */
    private String gerarIdentificadorTransacao(LocalDateTime instante) {
        String dataHora = instante.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        long sequencial = this.sequencialTransacao.incrementAndGet();
        String sufixo = UUID.randomUUID().toString().replace("-", "").substring(0, 4).toUpperCase();
        return String.format("TRX-%s-%06d-%s", dataHora, sequencial, sufixo);
    }
}
