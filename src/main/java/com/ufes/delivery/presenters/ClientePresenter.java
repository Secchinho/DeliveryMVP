/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufes.delivery.presenters;

import com.ufes.delivery.command.AtualizarClienteCommand;
import com.ufes.delivery.command.ClientePresenterCommand;
import com.ufes.delivery.command.SalvarClienteCommand;
import com.ufes.delivery.model.Cliente;
import com.ufes.delivery.model.Endereco;
import com.ufes.delivery.repository.IClienteRepository;
import com.ufes.delivery.view.IClienteView;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.swing.table.DefaultTableModel;

/**
 * Presenter responsável por mediar a comunicação entre a {@link IClienteView}
 * (tela de cliente) e o {@link IClienteRepository} (camada de persistência).
 *
 * <p>Este Presenter opera exclusivamente com o padrão <b>Command</b> para
 * decidir a ação executada ao acionar o botão "Salvar". Não há mais
 * padrão State — a estratégia de persistência (incluir vs. atualizar) é
 * definida trocando-se o command ativo via {@link #setCommand}.</p>
 *
 * <p>Responsabilidades (alinhadas ao diagrama de classes e à US06):</p>
 * <ul>
 *   <li>Configurar os listeners da View;</li>
 *   <li>Delegar o "Salvar" ao command ativo;</li>
 *   <li>Disponibilizar métodos de leitura/escrita da View para os commands;</li>
 *   <li>Expor o repositório e o cliente corrente aos commands.</li>
 * </ul>
 *
 * @author lucas
 */
public class ClientePresenter {

    private IClienteView view;
    private IClienteRepository clienteRepository;
    private Cliente cliente;
    private ClientePresenterCommand command;

    /**
     * Constrói o Presenter criando internamente os commands de salvar e
     * atualizar. Isso resolve a dependência circular Presenter &lt;-&gt;
     * Command (cada command precisa da referência do Presenter no construtor).
     *
     * <p>Por padrão, o command ativo é o de inclusão
     * ({@link SalvarClienteCommand}). Ao entrar em modo de edição, o
     * chamador deve invocar {@link #setCommand} passando
     * {@link #getAtualizarCommand()}.</p>
     *
     * @param view              interface da tela de cliente (não nula)
     * @param clienteRepository repositório de clientes (não nulo)
     */
    public ClientePresenter(IClienteView view, IClienteRepository clienteRepository) {
        this.view = Objects.requireNonNull(view, "View não pode ser nula");
        this.clienteRepository = Objects.requireNonNull(clienteRepository,
                "Repositório não pode ser nulo");
        
        this.configurarEventos();
    }
    
    public ClientePresenter(IClienteView view, IClienteRepository clienteRepository, Cliente cliente) {
        this.view = Objects.requireNonNull(view, "View não pode ser nula");
        this.clienteRepository = Objects.requireNonNull(clienteRepository,
                "Repositório não pode ser nulo");
        this.cliente = Objects.requireNonNull(cliente);
        
        this.configurarEventos();
    }

    /**
     * Registra os listeners dos botões da View. A View permanece "burra"
     * (sem lógica) — toda ação é tratada aqui.
     */
    private void configurarEventos() {
        this.view.getBotaoCancelar()
                .addActionListener(e -> this.view.getJanelaPrincipal().dispose());
        this.view.getBotaoSalvar()
                .addActionListener(e -> this.salvarDados());
    }

    /** Torna a janela visível. */
    public void iniciar() {
        this.view.getJanelaPrincipal().setVisible(true);
    }

    /**
     * Define o command ativo. Usado para alternar entre os modos de
     * inclusão e atualização.
     *
     * @param command command a ser ativado (não nulo)
     */
    public void setCommand(ClientePresenterCommand command) {
        this.command = Objects.requireNonNull(command, "Command não pode ser nulo");
    }

    /**
     * Acionado pelo botão "Salvar". Delega a execução ao command ativo,
     * implementando o padrão Command (sem uso de State).
     */
    private void salvarDados() {
        this.command.salvar();
    }

    // =========================================================================
    // Getters e Setters
    // =========================================================================

    public IClienteView getView() {
        return this.view;
    }

    public Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public IClienteRepository getClienteRepository() {
        return this.clienteRepository;
    }

    // =========================================================================
    // Métodos auxiliares de leitura da View (usados pelos commands)
    // =========================================================================

    /**
     * Lê os dados de nome e CPF preenchidos na view e retorna um array de
     * String com {@code [nome, cpf]}, ou {@code null} se algum campo
     * obrigatório estiver vazio.
     *
     * @return array {@code [nome, cpf]} ou {@code null}
     */
    public String[] lerDadosClienteDaView() {
        String nome = this.view.getCampoNome().getText().trim();
        String cpf = this.view.getCampoCpf().getText().trim();

        if (nome.isEmpty() || cpf.isEmpty()) {
            return null;
        }
        return new String[]{nome, cpf};
    }

    /**
     * Lê os endereços preenchidos na tabela da view e retorna uma lista de
     * arrays {@code Object[]}, onde cada array contém:
     * {@code [padrão (Boolean), logradouro, número, complemento, bairro,
     * cidade, uf, cep]}.
     *
     * <p>Apenas linhas com logradouro preenchido são consideradas ativas.
     * Retorna {@code null} se nenhum endereço válido for encontrado.</p>
     *
     * @return lista de endereços ativos ou {@code null}
     */
    public List<Object[]> lerEnderecosDaView() {
        DefaultTableModel modelo = this.view.getModeloEnderecos();
        List<Object[]> enderecos = new ArrayList<>();

        for (int i = 0; i < modelo.getRowCount(); i++) {
            Object logradouroObj = modelo.getValueAt(i, 1);
            if (logradouroObj != null && !logradouroObj.toString().trim().isEmpty()) {
                Object[] linha = new Object[8];
                linha[0] = modelo.getValueAt(i, 0); // Padrão (Boolean)
                linha[1] = logradouroObj;           // Logradouro
                linha[2] = modelo.getValueAt(i, 2); // Número
                linha[3] = modelo.getValueAt(i, 3); // Complemento
                linha[4] = modelo.getValueAt(i, 4); // Bairro
                linha[5] = modelo.getValueAt(i, 5); // Cidade
                linha[6] = modelo.getValueAt(i, 6); // UF
                linha[7] = modelo.getValueAt(i, 7); // CEP
                enderecos.add(linha);
            }
        }

        if (enderecos.isEmpty()) {
            return null;
        }
        return enderecos;
    }

    // =========================================================================
    // Métodos auxiliares de popular a View (usados em modo edição/visualização)
    // =========================================================================

    /**
     * Popula os campos da view com os dados de um cliente existente,
     * utilizado nos modos de edição e visualização.
     *
     * @param cliente cliente a ser exibido (se {@code null}, nada é feito)
     */
    public void popularViewComCliente(Cliente cliente) {
        if (cliente == null) {
            return;
        }
        this.view.getCampoNome().setText(cliente.getNome());
        this.view.getCampoCpf().setText(cliente.getCPF());

        DefaultTableModel modelo = this.view.getModeloEnderecos();
        // Limpa todas as células da tabela
        for (int i = 0; i < modelo.getRowCount(); i++) {
            for (int j = 0; j < modelo.getColumnCount(); j++) {
                modelo.setValueAt(null, i, j);
            }
        }

        List<Endereco> enderecos = cliente.getEnderecos();
        for (int i = 0; i < enderecos.size() && i < modelo.getRowCount(); i++) {
            Endereco end = enderecos.get(i);
            modelo.setValueAt(end.isPadrao(), i, 0);
            modelo.setValueAt(end.getLogradouro(), i, 1);
            modelo.setValueAt(end.getNumero(), i, 2);
            modelo.setValueAt(end.getComplemento(), i, 3);
            modelo.setValueAt(end.getBairro(), i, 4);
            modelo.setValueAt(end.getCidade(), i, 5);
            modelo.setValueAt(end.getUf(), i, 6);
            modelo.setValueAt(end.getCep(), i, 7);
        }
    }

    /**
     * Exibe uma mensagem de diálogo na view. Centraliza o acesso à JOptionPane
     * para que os commands não dependam diretamente de Swing.
     *
     * @param mensagem      texto da mensagem
     * @param titulo        título da janela
     * @param tipoMensagem  constante de JOptionPane (ERROR/WARNING/INFO)
     */
    public void exibirMensagem(String mensagem, String titulo, int tipoMensagem) {
        javax.swing.JOptionPane.showMessageDialog(
                this.view.getJanelaPrincipal(), mensagem, titulo, tipoMensagem);
    }
}
