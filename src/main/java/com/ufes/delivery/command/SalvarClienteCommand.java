/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufes.delivery.command;

import com.ufes.delivery.model.Cliente;
import com.ufes.delivery.presenters.ClientePresenter;
import java.util.List;

/**
 * Command concreto responsável por <b>cadastrar (incluir)</b> um novo cliente.
 *
 * <p>Implementa a lógica de validação e persistência conforme a US06 —
 * Cadastrar, editar e visualizar cliente (Cenário 1: Salvar cliente com
 * endereço padrão).</p>
 *
 * <p>As validações compartilhadas (nome, CPF, endereços) são herdadas de
 * {@link ClientePresenterCommand}.</p>
 *
 * @author lucas
 */
public class SalvarClienteCommand extends ClientePresenterCommand {

    public SalvarClienteCommand(ClientePresenter clientePresenter) {
        super(clientePresenter);
    }

    @Override
    public void salvar() {
        // ---- 1. Lê os dados do formulário via Presenter ----
        String[] dadosCliente = clientePresenter.lerDadosClienteDaView();
        if (dadosCliente == null) {
            clientePresenter.exibirMensagem(
                    "Nome e CPF são obrigatórios.",
                    "Validação",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        String nome = dadosCliente[0];
        String cpfComMascara = dadosCliente[1];

        // ---- 2. Valida o Nome (US06) ----
        String erroNome = validarNome(nome);
        if (erroNome != null) {
            clientePresenter.exibirMensagem(erroNome, "Validação",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        // ---- 3. Valida o CPF (11 dígitos + dígitos verificadores) ----
        String cpf = cpfComMascara.replaceAll("[^0-9]", "");
        if (cpf.length() != 11 || !validarDigitosCPF(cpf)) {
            clientePresenter.exibirMensagem(
                    "CPF inválido. Informe 11 dígitos válidos.",
                    "Validação",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        // ---- 4. Verifica duplicidade de CPF (US06, Cenário 2) ----
        try {
            Cliente existente = clientePresenter.getClienteRepository().getPorCPF(cpf).get();
            if (existente != null) {
                clientePresenter.exibirMensagem(
                        "CPF já vinculado a cliente existente.",
                        "Validação",
                        javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (RuntimeException ex) {
            // Repositório lançou ao não encontrar — segue o fluxo de inclusão
        }

        // ---- 5. Lê e valida os endereços (US06) ----
        List<Object[]> enderecosData = clientePresenter.lerEnderecosDaView();
        String erroEnd = validarEnderecos(enderecosData);
        if (erroEnd != null) {
            clientePresenter.exibirMensagem(erroEnd, "Validação",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        // ---- 6. Cria o cliente e adiciona os endereços ----
        Cliente cliente = new Cliente(cpf, nome.trim(), "Bronze", 1.0);
        for (Object[] endData : enderecosData) {
            cliente.addEndereco(construirEndereco(endData));
        }

        // ---- 7. Persiste no repositório ----
        clientePresenter.getClienteRepository().adicionar(cliente);

        // ---- 8. Atualiza referência no presenter e informa sucesso ----
        clientePresenter.setCliente(cliente);
        clientePresenter.exibirMensagem(
                "Cliente cadastrado com sucesso!",
                "Sucesso",
                javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }
}
