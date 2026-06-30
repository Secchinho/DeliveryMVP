/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufes.delivery.command;

import com.ufes.delivery.model.Cliente;
import com.ufes.delivery.model.Endereco;
import com.ufes.delivery.presenters.ClientePresenter;
import java.util.List;

/**
 * Command concreto responsável por <b>atualizar</b> um cliente existente.
 *
 * <p>Implementa a lógica de validação e atualização conforme a US06 —
 * Cadastrar, editar e visualizar cliente (Cenário 5: Visualizar e editar
 * cliente).</p>
 *
 * <p>As validações compartilhadas (nome, CPF, endereços) são herdadas de
 * {@link ClientePresenterCommand}.</p>
 *
 * @author lucas
 */
public class AtualizarClienteCommand extends ClientePresenterCommand {

    public AtualizarClienteCommand(ClientePresenter clientePresenter) {
        super(clientePresenter);
    }
    
    @Override
    public void salvar() {
        // ---- 0. Deve existir um cliente em edição ----
        Cliente clienteExistente = clientePresenter.getCliente();
        if (clienteExistente == null) {
            clientePresenter.exibirMensagem(
                    "Nenhum cliente selecionado para atualização.",
                    "Validação",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

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

        // ---- 3. Valida o CPF ----
        String cpf = cpfComMascara.replaceAll("[^0-9]", "");
        if (cpf.length() != 11 || !validarDigitosCPF(cpf)) {
            clientePresenter.exibirMensagem(
                    "CPF inválido. Informe 11 dígitos válidos.",
                    "Validação",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        // ---- 4. Se o CPF mudou, verifica duplicidade em outro cliente ----
        if (!cpf.equals(clienteExistente.getCPF())) {
            try {
                Cliente outro = clientePresenter.getClienteRepository().getPorCPF(cpf).get();
                if (outro != null) {
                    clientePresenter.exibirMensagem(
                            "CPF já vinculado a outro cliente existente.",
                            "Validação",
                            javax.swing.JOptionPane.WARNING_MESSAGE);
                    return;
                }
            } catch (RuntimeException ex) {
                // CPF não encontrado em outro cadastro — pode prosseguir
            }
        }

        // ---- 5. Lê e valida os endereços (US06) ----
        List<Object[]> enderecosData = clientePresenter.lerEnderecosDaView();
        String erroEnd = validarEnderecos(enderecosData);
        if (erroEnd != null) {
            clientePresenter.exibirMensagem(erroEnd, "Validação",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        // ---- 6. Atualiza os dados do cliente existente ----
        // Nota: os setters de nome/cpf não aparecem no diagrama, mas são
        // necessários para cumprir a US06 (Cenário 5). Caso a classe Cliente
        // não os exponha, eles deverão ser adicionados ao modelo.
        clienteExistente.setNome(nome.trim());
        clienteExistente.setCpf(cpf);

        // Substitui os endereços: remove os antigos e adiciona os novos.
        // Usa removerEndereco um a um para respeitar o contrato do modelo
        // (getEnderecos() pode retornar lista não modificável).
        List<Endereco> antigos = new java.util.ArrayList<>(clienteExistente.getEnderecos());
        for (Endereco antigo : antigos) {
            clienteExistente.removerEndereco(antigo);
        }
        for (Object[] endData : enderecosData) {
            clienteExistente.addEndereco(construirEndereco(endData));
        }

        // ---- 7. Persiste a atualização no repositório ----
        clientePresenter.getClienteRepository().atualizar(clienteExistente);

        // ---- 8. Informa sucesso ----
        clientePresenter.exibirMensagem(
                "Cliente atualizado com sucesso!",
                "Sucesso",
                javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }
}
