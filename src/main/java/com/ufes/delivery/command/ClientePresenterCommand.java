/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufes.delivery.command;

import com.ufes.delivery.model.Endereco;
import com.ufes.delivery.presenters.ClientePresenter;
import java.util.List;


//@author lucas
 
public abstract class ClientePresenterCommand {

    protected final ClientePresenter clientePresenter;

    public ClientePresenterCommand(ClientePresenter clientePresenter) {
        this.clientePresenter = java.util.Objects.requireNonNull(
                clientePresenter, "Presenter não pode ser nulo");
    }

    /**
     * Operação executada quando o usuário aciona "Salvar".
     * Implementada por cada command concreto.
     */
    public abstract void salvar();
    public abstract void iniciar();

    // =========================================================================
    // Validações compartilhadas (US06)
    // =========================================================================

    /**
     * Valida os dígitos verificadores do CPF.
     *
     * @param cpf CPF contendo exatamente 11 dígitos numéricos (sem máscara)
     * @return {@code true} se os dígitos verificadores forem válidos
     */
    protected boolean validarDigitosCPF(String cpf) {
        if (cpf == null || cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
            return false; // nulo, tamanho incorreto ou sequência de dígitos iguais
        }
        int soma1 = 0;
        for (int i = 0; i < 9; i++) {
            soma1 += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
        }
        int dig1 = 11 - (soma1 % 11);
        if (dig1 >= 10) {
            dig1 = 0;
        }
        if (dig1 != Character.getNumericValue(cpf.charAt(9))) {
            return false;
        }

        int soma2 = 0;
        for (int i = 0; i < 10; i++) {
            soma2 += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
        }
        int dig2 = 11 - (soma2 % 11);
        if (dig2 >= 10) {
            dig2 = 0;
        }
        return dig2 == Character.getNumericValue(cpf.charAt(10));
    }

    /**
     * Valida o nome do cliente conforme US06:
     * obrigatório, 2 a 120 caracteres, aceita letras, espaços, apóstrofos
     * e hífens (incluindo acentos).
     *
     * @param nome nome informado
     * @return {@code null} se válido, ou mensagem de erro
     */
    protected String validarNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            return "Nome é obrigatório.";
        }
        String nomeTrim = nome.trim();
        if (nomeTrim.length() < 2 || nomeTrim.length() > 120) {
            return "Nome deve conter de 2 a 120 caracteres.";
        }
        // Letras (incluindo acentuadas), espaços, apóstrofos e hífens
        if (!nomeTrim.matches("[\\p{L}\\s''-]+")) {
            return "Nome deve conter apenas letras, espaços, apóstrofos e hífens.";
        }
        return null;
    }

    /**
     * Valida a lista de endereços ativos conforme US06.
     *
     * <p>Regras verificadas (a primeira falha aborta e retorna a mensagem):</p>
     * <ul>
     *   <li>Pelo menos 1 endereço e no máximo 3;</li>
     *   <li>Exatamente 1 endereço marcado como padrão;</li>
     *   <li>Logradouro, número, bairro e cidade obrigatórios em cada linha;</li>
     *   <li>UF com 2 letras;</li>
     *   <li>CEP com 8 dígitos (quando informado).</li>
     * </ul>
     *
     * @param enderecosData lista de arrays {@code Object[8]} com os dados da
     *                      tabela de endereços
     * @return {@code null} se tudo válido, ou mensagem de erro
     */
    protected String validarEnderecos(List<Object[]> enderecosData) {
        if (enderecosData == null || enderecosData.isEmpty()) {
            return "Pelo menos um endereço de entrega é obrigatório.";
        }
        if (enderecosData.size() > 3) {
            return "O cadastro permite no máximo três endereços de entrega.";
        }

        // Endereço padrão: exatamente um
        int qtdPadrao = 0;
        for (Object[] end : enderecosData) {
            if (Boolean.TRUE.equals(end[0])) {
                qtdPadrao++;
            }
        }
        if (qtdPadrao == 0) {
            return "Um endereço padrão é obrigatório.";
        }
        if (qtdPadrao > 1) {
            return "Apenas um endereço pode ser marcado como padrão.";
        }

        // Campos obrigatórios de cada linha ativa
        for (int i = 0; i < enderecosData.size(); i++) {
            Object[] end = enderecosData.get(i);
            String logradouro = texto(end[1]);
            String numero = texto(end[2]);
            String bairro = texto(end[4]);
            String cidade = texto(end[5]);
            String uf = texto(end[6]);
            String cep = texto(end[7]).replaceAll("[^0-9]", "");

            if (logradouro.isEmpty() || bairro.isEmpty() || cidade.isEmpty()) {
                return "Logradouro, bairro e cidade são obrigatórios no endereço "
                        + (i + 1) + ".";
            }
            // Número é obrigatório conforme US06
            if (numero.isEmpty()) {
                return "Número é obrigatório no endereço " + (i + 1) + ".";
            }
            try {
                int n = Integer.parseInt(numero);
                if (n <= 0) {
                    return "Número deve ser um valor positivo no endereço "
                            + (i + 1) + ".";
                }
            } catch (NumberFormatException e) {
                return "Número inválido no endereço " + (i + 1) + ".";
            }

            // UF: 2 letras
            if (uf.length() != 2 || !uf.matches("[A-Za-z]{2}")) {
                return "UF deve conter 2 letras válidas no endereço " + (i + 1) + ".";
            }

            // CEP: 8 dígitos (quando informado)
            if (!cep.isEmpty() && cep.length() != 8) {
                return "CEP deve conter 8 dígitos no endereço " + (i + 1) + ".";
            }
        }
        return null;
    }

    /**
     * Constrói um {@link Endereco} a partir de uma linha da tabela de
     * endereços da View.
     *
     * <p>Pré-condição: a linha já deve ter sido validada por
     * {@link #validarEnderecos}.</p>
     *
     * @param endData array {@code Object[8]} com os dados do endereço
     * @return instância de {@link Endereco} preenchida
     */
    protected Endereco construirEndereco(Object[] endData) {
        boolean padrao = Boolean.TRUE.equals(endData[0]);
        String logradouro = texto(endData[1]);
        int numero = Integer.parseInt(texto(endData[2]));
        String complemento = texto(endData[3]);
        String bairro = texto(endData[4]);
        String cidade = texto(endData[5]);
        String uf = texto(endData[6]);
        String cep = texto(endData[7]).replaceAll("[^0-9]", "");

        return new Endereco(padrao, logradouro, numero, complemento,
                bairro, cidade, uf.toUpperCase(), cep);
    }

    /** Utilitário: converte {@code Object} para {@code String} trim, ou vazio. */
    private static String texto(Object obj) {
        return (obj == null) ? "" : obj.toString().trim();
    }
}
