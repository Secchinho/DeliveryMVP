package com.ufes.delivery.presenters;

import com.ufes.delivery.command.AtualizarClienteCommand;
import com.ufes.delivery.command.SalvarClienteCommand;
import com.ufes.delivery.model.Cliente;
import com.ufes.delivery.repository.IClienteRepository;
import com.ufes.delivery.view.IBuscarClienteView;
import com.ufes.delivery.view.IClienteView;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.swing.JOptionPane;

/**
 * Presenter responsável por controlar a tela de Busca de Clientes (US05).
 *
 * <p>Implementa o padrão MVP Passive View: a View é "burra" e o Presenter
 * é responsável por configurar eventos, validar entradas, acionar o
 * repositório e atualizar a tabela de resultados.</p>
 *
 * <p>Conforme o Diagrama de Classes, este Presenter possui:
 * <ul>
 *   <li><b>Atributos:</b>
 *     <ul>
 *       <li>{@code view} ({@link IBuscarClienteView}) - interface da view</li>
 *       <li>{@code clienteRepository} ({@link IClienteRepository}) - acesso aos dados</li>
 *       <li>{@code clientes} ({@code List<Cliente>}) - cache dos resultados correntes</li>
 *     </ul>
 *   </li>
 *   <li><b>Métodos:</b>
 *     <ul>
 *       <li>{@link #iniciar()} (public) - exibe a janela e carrega a lista inicial</li>
 *       <li>{@link #configurarEventos()} (private) - registra os listeners dos botões</li>
 *       <li>{@link #buscar()} (private) - executa a busca conforme atributo e valor</li>
 *       <li>{@link #adicionar()} (private) - abre a tela de manutenção em modo inclusão</li>
 *       <li>{@link #visualizar()} (private) - abre a tela de manutenção do cliente selecionado</li>
 *       <li>{@link #fechar()} (private) - encerra a janela de busca</li>
 *       <li>{@link #buscarTodos()} (private) - carrega todos os clientes do repositório</li>
 *       <li>{@link #atualizarTabela()} (private) - atualiza a tabela da view com o cache atual</li>
 *       <li>{@link #exibirMensagem(String, String, int)} (private) - delega mensagens à view</li>
 *       <li>{@link #obterAtributoSelecionado()} (private) - retorna o atributo de busca selecionado</li>
 *       <li>{@link #isCpfValido(String)} (private) - valida dígitos verificadores do CPF</li>
 *     </ul>
 *   </li>
 * </ul>
 * </p>
 *
 * <p>Cenários cobertos da US05:
 * <ul>
 *   <li>Cenário 1 - Buscar cliente por nome</li>
 *   <li>Cenário 2 - Buscar cliente por CPF com máscara (considera somente os 11 dígitos)</li>
 *   <li>Cenário 3 - Rejeitar CPF inválido (dígitos verificadores)</li>
 *   <li>Cenário 4 - Rejeitar valor de busca ausente</li>
 *   <li>Cenário 5 - Abrir cliente selecionado no modo visualização</li>
 * </ul>
 * </p>
 *
 * @author lucas
 */
public class BuscarClientePresenter {

    private final IBuscarClienteView view;
    private final IClienteRepository clienteRepository;
    private List<Cliente> clientes;
    private IClienteView clienteView;

    /**
     * Constrói o Presenter com a View e o repositório de clientes.
     *
     * @param view              interface da tela de busca de clientes
     * @param clienteRepository repositório de clientes
     * @throws NullPointerException se view ou clienteRepository forem nulos
     */
    public BuscarClientePresenter(IBuscarClienteView view, IClienteRepository clienteRepository, IClienteView clienteView) {
        this.view = Objects.requireNonNull(view, "A view não pode ser nula");
        this.clienteRepository = Objects.requireNonNull(clienteRepository, "O repositório de clientes não pode ser nulo");
        this.clienteView = Objects.requireNonNull(clienteView,"Insira uma Tela Cliente.");
        this.clientes = new ArrayList<>();
        this.configurarEventos();
    }

    /**
     * Inicia o Presenter: exibe a janela e carrega a listagem inicial de
     * clientes, conforme comentário presente na própria View
     * ("O Presenter iniciará a busca").
     */
    public void iniciar() {
        this.view.getJanelaPrincipal().setVisible(true);
        this.buscarTodos();
        this.atualizarTabela();
    }

    /**
     * Configura os listeners de todos os botões expostos pela View:
     * Buscar, Novo, Visualizar e Fechar.
     */
    private void configurarEventos() {
        this.view.getBuscarClienteButton().addActionListener(e -> this.buscar());
        this.view.getNovoClienteButton().addActionListener(e -> this.adicionar());
        this.view.getVisualizarClienteButton().addActionListener(e -> this.visualizar());
        this.view.getFecharButton().addActionListener(e -> this.fechar());
    }

    /**
     * Executa a busca conforme o atributo (Nome ou CPF) e o valor informados.
     *
     * <p>Regras de validação aplicadas (US05):
     * <ul>
     *   <li><b>Atributo de busca:</b> obrigatório, somente Nome ou CPF.</li>
     *   <li><b>Valor para Nome:</b> 2 a 120 caracteres após trim.</li>
     *   <li><b>Valor para CPF:</b> 11 dígitos (com ou sem máscara), validado
     *       pelos dígitos verificadores antes da consulta.</li>
     *   <li><b>Resultado vazio:</b> tabela sem linhas e mensagem informativa.</li>
     * </ul>
     * </p>
     */
    private void buscar() {
        String atributo = this.obterAtributoSelecionado();
        String valor = this.view.getValorBusca();

        // Cenário 4: Rejeitar valor de busca ausente
        if (valor == null || valor.isEmpty()) {
            this.exibirMensagem(
                "O valor da busca é obrigatório.",
                "Atenção",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if ("Nome".equalsIgnoreCase(atributo)) {
            // Validação do Nome: 2 a 120 caracteres após trim
            if (valor.length() < 2 || valor.length() > 120) {
                this.exibirMensagem(
                    "O nome deve conter de 2 a 120 caracteres.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }
            // Busca por nome contendo (case-insensitive no repositório)
            this.clientes = this.clienteRepository.buscarPorNomeContendo(valor);
        } else if ("CPF".equalsIgnoreCase(atributo)) {
            // Remove máscara, mantendo apenas os 11 dígitos
            String cpf = valor.replaceAll("\\D", "");

            // Cenário 3: Rejeitar CPF inválido (dígitos verificadores)
            if (!this.isCpfValido(cpf)) {
                this.exibirMensagem(
                    "CPF inválido. Verifique os dígitos informados.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            // Cenário 2: Busca considera somente os 11 dígitos
            Cliente cliente = this.clienteRepository.getPorCPF(cpf).get();
            this.clientes = new ArrayList<>();
            if (cliente != null) {
                this.clientes.add(cliente);
            }
        } else {
            // Atributo inválido (combobox somente permite Nome/CPF, mas defesa)
            this.exibirMensagem(
                "Atributo de busca inválido. Selecione Nome ou CPF.",
                "Atenção",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        this.atualizarTabela();

        // Resultado vazio: tabela sem linhas e mensagem informativa
        if (this.clientes.isEmpty()) {
            this.exibirMensagem(
                "Não há clientes para o critério informado.",
                "Informação",
                JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    /**
     * Abre a tela de manutenção de cliente em modo de inclusão.
     *
     * <p>O botão "Novo" deve abrir o {@code ClientePresenter} no estado
     * {@code CadastrarClienteState}, conforme diagrama de classes.</p>
     */
    private void adicionar() {
        try {
            
            ClientePresenter presenter = new ClientePresenter(this.clienteView,this.clienteRepository);
            presenter.setCommand(new SalvarClienteCommand(presenter));
            presenter.iniciar();
        } catch (RuntimeException ex) {
            this.exibirMensagem(
                "Não foi possível abrir o cadastro de cliente: " + ex.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE
            );
        } 
    }

    /**
     * Abre a tela de manutenção do cliente selecionado (modo visualização).
     *
     * <p>Cenário 5: Abrir cliente selecionado. Os dados do cliente selecionado
     * devem ser carregados na tela de manutenção.</p>
     *
     * <p>Regra: Visualizar exige uma linha selecionada. Sem seleção, a interface
     * deve solicitar a escolha de um cliente antes da abertura.</p>
     */
    private void visualizar() {
        String cpf = this.view.getCpfClienteSelecionado();

        // Sem seleção: solicitar a escolha de um cliente antes da abertura
        if (cpf == null) {
            this.exibirMensagem(
                "Selecione um cliente na tabela antes de visualizar.",
                "Atenção",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Cliente cliente = this.clienteRepository.getPorCPF(cpf).get();
        if (cliente == null) {
            this.exibirMensagem(
                "Cliente não encontrado no repositório.",
                "Atenção",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        
        try {
            ClientePresenter presenter = new ClientePresenter(this.clienteView,this.clienteRepository,cliente);
            presenter.setCommand(new AtualizarClienteCommand(presenter));
            presenter.iniciar();
            
        } catch (RuntimeException ex) {
            this.exibirMensagem(
                "Não foi possível abrir a visualização do cliente: " + ex.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Encerra a janela de busca de clientes.
     */
    private void fechar() {
        this.view.getJanelaPrincipal().dispose();
    }

    /**
     * Carrega todos os clientes do repositório. Utilizado na inicialização
     * para preencher a tabela com a listagem completa.
     */
    private void buscarTodos() {
        List<Cliente> todos = this.clienteRepository.listarClientes();
        this.clientes = (todos == null) ? new ArrayList<>() : new ArrayList<>(todos);
    }

    /**
     * Atualiza a tabela da View com o cache atual de clientes.
     * A tabela apresenta Nome (coluna 0) e CPF (coluna 1).
     */
    private void atualizarTabela() {
        List<Object[]> dados = new ArrayList<>();
        if (this.clientes != null) {
            for (Cliente c : this.clientes) {
                if (c != null) {
                    dados.add(new Object[]{ c.getNome(), c.getCPF() });
                }
            }
        }
        this.view.exibirClientes(dados);
    }

    /**
     * Exibe uma mensagem ao usuário, delegando à View.
     *
     * @param mensagem texto da mensagem
     * @param titulo   título da janela
     * @param tipo     tipo de mensagem (ex.: JOptionPane.WARNING_MESSAGE)
     */
    private void exibirMensagem(String mensagem, String titulo, int tipo) {
        this.view.exibirMensagem(mensagem, titulo, tipo);
    }

    /**
     * Retorna o atributo de busca selecionado na View ("Nome" ou "CPF").
     *
     * @return atributo selecionado, ou {@code null} se nenhum
     */
    private String obterAtributoSelecionado() {
        return this.view.getAtributoBusca();
    }

    /**
     * Valida um CPF considerando os 11 dígitos e os dígitos verificadores.
     *
     * <p>O CPF informado deve conter somente dígitos (sem máscara). CPFs
     * compostos por dígitos repetidos (ex.: 000.000.000-00, 111.111.111-11)
     * são rejeitados, conforme regra padrão da Receita Federal.</p>
     *
     * @param cpf CPF contendo somente 11 dígitos
     * @return {@code true} se o CPF é válido, {@code false} caso contrário
     */
    private boolean isCpfValido(String cpf) {
        if (cpf == null || cpf.length() != 11 || !cpf.matches("\\d{11}")) {
            return false;
        }

        // Rejeita CPFs com todos os dígitos iguais (ex.: 00000000000)
        boolean todosIguais = true;
        for (int i = 1; i < 11; i++) {
            if (cpf.charAt(i) != cpf.charAt(0)) {
                todosIguais = false;
                break;
            }
        }
        if (todosIguais) {
            return false;
        }

        // Cálculo do 1º dígito verificador
        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += (cpf.charAt(i) - '0') * (10 - i);
        }
        int resto = soma % 11;
        int dig1 = (resto < 2) ? 0 : (11 - resto);
        if (dig1 != (cpf.charAt(9) - '0')) {
            return false;
        }

        // Cálculo do 2º dígito verificador
        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma += (cpf.charAt(i) - '0') * (11 - i);
        }
        resto = soma % 11;
        int dig2 = (resto < 2) ? 0 : (11 - resto);
        return dig2 == (cpf.charAt(10) - '0');
    }
}
