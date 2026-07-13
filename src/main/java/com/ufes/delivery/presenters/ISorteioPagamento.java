/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufes.delivery.presenters;

import java.time.LocalDateTime;

/**
 * Fonte de aleatoriedade usada por {@link PagamentoService} na simulação do
 * pagamento (US11).
 *
 * <p>A DoD da US11 exige: <i>"A fonte de aleatoriedade deve permitir
 * substituição por valor determinístico nos testes, permitindo verificar
 * aprovação, reprovação e cada forma de pagamento."</i></p>
 *
 * <p>Por isso a fonte é isolada nesta interface. A implementação padrão
 * ({@link SorteioPagamentoAleatorio}) usa {@link java.util.Random}; em testes
 * pode-se fornecer uma implementação determinística que retorna valores
 * fixos.</p>
 *
 * @author lucas
 */
public interface ISorteioPagamento {

    /**
     * Sorteia o resultado da tentativa de pagamento. Deve aplicar probabilidade
     * de 50% para aprovado e 50% para reprovado (US11).
     *
     * @return {@code true} se aprovado, {@code false} se reprovado.
     */
    boolean sortearAprovacao();

    /**
     * Sorteia a forma de pagamento entre Open Finance, PIX chave, PIX QR Code
     * e Cartão de crédito, cada uma com peso de 25% (US11).
     *
     * @return uma das quatro formas de pagamento.
     */
    String sortearFormaPagamento();

    /**
     * Sorteia o prazo estimado de entrega, que deve ser igual ou posterior ao
     * instante da aprovação e não ultrapassar o mesmo dia do mês subsequente
     * (US11). Por exemplo, se aprovado em 20/06/2026 às 10:24, o prazo deve
     * estar entre 20/06/2026 10:24 e 20/07/2026 10:24.
     *
     * @param instanteAprovacao instante em que a aprovação ocorreu.
     * @return data/hora sorteada dentro do intervalo válido.
     */
    LocalDateTime sortearPrazoEntrega(LocalDateTime instanteAprovacao);
}
