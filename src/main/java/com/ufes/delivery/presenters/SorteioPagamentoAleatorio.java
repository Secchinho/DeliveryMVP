/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufes.delivery.presenters;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;

/**
 * Implementação padrão de {@link ISorteioPagamento} usando {@link Random}.
 *
 * <p>Aplica as probabilidades definidas na US11:
 * <ul>
 *   <li>50% de aprovação e 50% de reprovação;</li>
 *   <li>25% para cada forma de pagamento (Open Finance, PIX chave, PIX QR
 *       Code, Cartão de crédito);</li>
 *   <li>Prazo estimado de entrega entre o instante da aprovação e o mesmo dia
 *       do mês subsequente.</li>
 * </ul>
 * </p>
 *
 * <p>Em testes, substitua por uma implementação determinística para verificar
 * cenários específicos.</p>
 *
 * @author lucas
 */
public class SorteioPagamentoAleatorio implements ISorteioPagamento {

    private static final String[] FORMAS_PAGAMENTO = {
        "Open Finance",
        "PIX chave",
        "PIX QR Code",
        "Cartão de crédito"
    };

    private final Random random;

    public SorteioPagamentoAleatorio() {
        this(new Random());
    }

    /**
     * Permite injetar um {@link Random} com semente fixa, útil para testes
     * que precisam de reprodutibilidade sem criar uma implementação
     * determinística completa da interface.
     */
    public SorteioPagamentoAleatorio(Random random) {
        this.random = random;
    }

    @Override
    public boolean sortearAprovacao() {
        // 50% de chance de aprovação.
        return this.random.nextBoolean();
    }

    @Override
    public String sortearFormaPagamento() {
        // 4 alternativas com peso igual (25% cada).
        int indice = this.random.nextInt(FORMAS_PAGAMENTO.length);
        return FORMAS_PAGAMENTO[indice];
    }

    @Override
    public LocalDateTime sortearPrazoEntrega(LocalDateTime instanteAprovacao) {
        if (instanteAprovacao == null) {
            throw new IllegalArgumentException("Insira o instante da aprovação.");
        }

        // Limite: mesmo dia e hora do mês subsequente.
        // Ex.: 20/06/2026 10:24 -> 20/07/2026 10:24.
        // O plusMonths já trata corretamente variações de dias por mês
        // (ex.: 31/01 + 1 mês = 28/02).
        LocalDateTime limite = instanteAprovacao.plusMonths(1);

        long segundosEntre = Duration.between(instanteAprovacao, limite).getSeconds();
        if (segundosEntre <= 0) {
            return instanteAprovacao;
        }

        // Offset aleatório dentro do intervalo [0, segundosEntre].
        long offset = (long) (this.random.nextDouble() * segundosEntre);
        return instanteAprovacao.plusSeconds(offset);
    }
}
