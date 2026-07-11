/*
 * MovimentacaoEstoquePresenter.java
 *
 * Presenter responsavel pela movimentacao de estoque (US08).
 * Segue o padrao MVP Passive View e Command Pattern conforme diagrama de classes.
 *
 * Atributos conforme diagrama:
 *   - view: IMovimentacaoEstoqueView (associacao privada)
 *   - repository: IProdutoRepository (associacao privada)
 *   - produtoSelecionado: Produto (0..1)
 *   - command: TipoMovimentacaoCommand (1..1)
 *   - produtos: Produto (0..*)
 *
 * Operacoes conforme diagrama:
 *   + iniciar(): void
 *   + configurarEventos(): void
 *   + setCommand(TipoMovimentacaoCommand): void
 */
package com.ufes.delivery.presenters;

import com.ufes.delivery.command.TipoMovimentacaoCommand;
import com.ufes.delivery.command.TipoEntradaCommand;
import com.ufes.delivery.command.TipoAjusteCommand;
import com.ufes.delivery.model.Produto;
import com.ufes.delivery.repository.IProdutoRepository;
import com.ufes.delivery.view.IMovimentacaoEstoqueView;

import javax.swing.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;

public class MovimentacaoEstoquePresenter {

    private IMovimentacaoEstoqueView view;
    private IProdutoRepository repository;
    private Produto produtoSelecionado;
    private TipoMovimentacaoCommand command;
    private List<Produto> produtos;

    private static final DateTimeFormatter FORMATADOR_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public MovimentacaoEstoquePresenter(IMovimentacaoEstoqueView view, IProdutoRepository repository) {
        this.view = Objects.requireNonNull(view, "A view não pode ser nula.");
        this.repository = Objects.requireNonNull(repository, "O repositório não pode ser nulo.");

        this.configurarEventos();
    }

    public void iniciar() {
        this.view.getJanelaPrincipal().setVisible(true);
    }

    private void configurarEventos() {
        this.view.getBuscarButton().addActionListener(e -> this.buscarProdutos());
        this.view.getSelecionarButton().addActionListener(e -> this.selecionarProduto());
        this.view.getConfirmarMovimentacaoButton().addActionListener(e -> this.confirmarMovimentacao());
        this.view.getCancelarButton().addActionListener(e -> this.cancelar());

        this.view.getTipoMovimentacaoComboBox().addActionListener(e -> this.atualizarTipoMovimentacao());
        this.view.getTxtQuantidadeMovimentar().getDocument()
                .addDocumentListener(new javax.swing.event.DocumentListener() {
                    @Override
                    public void insertUpdate(javax.swing.event.DocumentEvent e) {
                        atualizarPreviaEstoque();
                    }

                    @Override
                    public void removeUpdate(javax.swing.event.DocumentEvent e) {
                        atualizarPreviaEstoque();
                    }

                    @Override
                    public void changedUpdate(javax.swing.event.DocumentEvent e) {
                        atualizarPreviaEstoque();
                    }
                });
    }

    public void setCommand(TipoMovimentacaoCommand command) {
        this.command = Objects.requireNonNull(command, "O command não pode ser nulo.");
    }

    // -------------------------------------------------------------------------
    // Acoes de busca e selecao de produto
    // -------------------------------------------------------------------------

    private void buscarProdutos() {
        String termo = this.view.getTxtBuscarProduto().getText().trim();
        if (termo.isEmpty()) {
            this.produtos = this.repository.listarProdutos();
        } else {
            this.produtos = this.repository.buscarPorNome(termo);
        }
        // A view exibe os produtos encontrados (a implementacao concreta da view
        // decide como apresentar a lista, por exemplo em JDialog ou JTable).
        // O presenter nao acessa componentes visuais de apresentacao de lista
        // que nao estejam na interface IMovimentacaoEstoqueView.
    }

    private void selecionarProduto() {
        if (this.produtos == null || this.produtos.isEmpty()) {
            this.view.getLblAvisoRegras().setText("Nenhum produto encontrado. Realize uma busca primeiro.");
            return;
        }
        // A selecao do produto depende da implementacao concreta da view.
        // O presenter delega a exibicao da lista e aguarda que o produto
        // seja definido via setProdutoSelecionado (chamado pela view ou
        // por acao do usuario na lista).
    }

    public void setProdutoSelecionado(Produto produto) {
        this.produtoSelecionado = produto;
        if (produto != null) {
            this.view.getTxtProdutoSelecionado().setText(produto.getNome());
            this.view.getTxtQuantidadeAtual()
                    .setText(String.valueOf(produto.getQuantidadeInicial()));
            this.atualizarPreviaEstoque();
        } else {
            this.view.getTxtProdutoSelecionado().setText("");
            this.view.getTxtQuantidadeAtual().setText("");
            this.view.getTxtEstoqueAposMovimentacao().setText("");
        }
    }

    public Produto getProdutoSelecionado() {
        return this.produtoSelecionado;
    }

    public IMovimentacaoEstoqueView getView() {
        return this.view;
    }

    public IProdutoRepository getRepository() {
        return this.repository;
    }

    // -------------------------------------------------------------------------
    // Atualizacao de tipo de movimentacao e previa de estoque
    // -------------------------------------------------------------------------

    private void atualizarTipoMovimentacao() {
        String tipoSelecionado = (String) this.view.getTipoMovimentacaoComboBox().getSelectedItem();
        if (tipoSelecionado == null) {
            return;
        }

        boolean isEntrada = "Entrada".equalsIgnoreCase(tipoSelecionado);
        boolean isAjuste = "Ajuste de estoque".equalsIgnoreCase(tipoSelecionado)
                || "Ajuste".equalsIgnoreCase(tipoSelecionado);

        // Habilita/desabilita campos condicionais conforme US08:
        // - Entrada exige nota fiscal; Ajuste exige motivo.
        this.view.getTxtNotaFiscal().setEditable(isEntrada);
        this.view.getTxtMotivoAjuste().setEditable(isAjuste);

        if (isEntrada) {
            this.view.getTxtMotivoAjuste().setText("");
        }
        if (isAjuste) {
            this.view.getTxtNotaFiscal().setText("");
        }

        this.atualizarPreviaEstoque();
    }

    private void atualizarPreviaEstoque() {
        this.view.getLblAvisoPrevisualizacao().setText("");
        this.view.getLblAvisoRegras().setText("");

        if (this.produtoSelecionado == null) {
            this.view.getTxtEstoqueAposMovimentacao().setText("");
            return;
        }

        Integer quantidadeMovimentar = parseQuantidadeMovimentar();
        if (quantidadeMovimentar == null) {
            this.view.getTxtEstoqueAposMovimentacao().setText("");
            return;
        }

        int estoqueAtual = this.produtoSelecionado.getQuantidadeInicial();
        int estoqueResultante = estoqueAtual + quantidadeMovimentar;

        if (estoqueResultante < 0) {
            this.view.getTxtEstoqueAposMovimentacao().setText("");
            this.view.getLblAvisoRegras().setText(
                    "Estoque resultante negativo. Disponível: " + estoqueAtual + " unidades.");
            return;
        }

        this.view.getTxtEstoqueAposMovimentacao().setText(String.valueOf(estoqueResultante));
        this.view.getLblAvisoPrevisualizacao().setText("Prévia: " + estoqueResultante + " unidades");
    }

    // -------------------------------------------------------------------------
    // Confirmacao da movimentacao (delega ao Command)
    // -------------------------------------------------------------------------

    private void confirmarMovimentacao() {
        this.view.getLblAvisoRegras().setText("");

        // Validacoes basicas do presenter (regras transversais e de formato)
        String erro = validarCamposObrigatorios();
        if (erro != null) {
            this.view.getLblAvisoRegras().setText(erro);
            return;
        }

        // Define o command conforme o tipo de movimentacao selecionado
        String tipoSelecionado = (String) this.view.getTipoMovimentacaoComboBox().getSelectedItem();
        if (tipoSelecionado == null) {
            this.view.getLblAvisoRegras().setText("Selecione o tipo de movimentação.");
            return;
        }

        if ("Entrada".equalsIgnoreCase(tipoSelecionado)) {
            this.setCommand(new TipoEntradaCommand(this));
        } else if ("Ajuste de estoque".equalsIgnoreCase(tipoSelecionado)
                || "Ajuste".equalsIgnoreCase(tipoSelecionado)) {
            this.setCommand(new TipoAjusteCommand(this));
        } else {
            this.view.getLblAvisoRegras().setText(
                    "Tipo de movimentação inválido. Utilize Entrada ou Ajuste de estoque.");
            return;
        }

        // Delega a confirmacao ao command (validacao especifica do tipo + persistencia)
        this.command.confirmar();
    }

    // -------------------------------------------------------------------------
    // Validacoes do presenter (regras transversais e formato)
    // -------------------------------------------------------------------------

    private String validarCamposObrigatorios() {
        // Produto selecionado
        if (this.produtoSelecionado == null) {
            return "Selecione um produto antes de confirmar a movimentação.";
        }

        // Quantidade a movimentar: obrigatória, inteira e diferente de zero
        Integer quantidade = parseQuantidadeMovimentar();
        if (quantidade == null) {
            return "Informe uma quantidade inteira válida e diferente de zero.";
        }
        if (quantidade == 0) {
            return "A quantidade a movimentar deve ser diferente de zero.";
        }

        // Estoque resultante nao pode ser negativo
        int estoqueResultante = this.produtoSelecionado.getQuantidadeInicial() + quantidade;
        if (estoqueResultante < 0) {
            return "Estoque resultante negativo. Disponível: "
                    + this.produtoSelecionado.getQuantidadeInicial() + " unidades.";
        }

        // Validacao de data da movimentacao (se houver campo de data na view)
        // A data nao pode ser posterior a data operacional vigente.
        // Essa validacao pode ser feita aqui se a view fornecer o campo.

        return null; // sem erros
    }

    private Integer parseQuantidadeMovimentar() {
        String texto = this.view.getTxtQuantidadeMovimentar().getText().trim();
        if (texto.isEmpty()) {
            return null;
        }
        try {
            int valor = Integer.parseInt(texto);
            return valor;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    // -------------------------------------------------------------------------
    // Cancelar
    // -------------------------------------------------------------------------

    private void cancelar() {
        this.produtoSelecionado = null;
        this.produtos = null;
        this.command = null;
        this.view.getJanelaPrincipal().dispose();
    }

    // -------------------------------------------------------------------------
    // Metodos auxiliares acessados pelos Commands
    // -------------------------------------------------------------------------

    /**
     * Retorna a quantidade informada no campo de quantidade a movimentar, ou null
     * se o valor nao for um inteiro valido.
     */
    public Integer getQuantidadeMovimentar() {
        return parseQuantidadeMovimentar();
    }

    /**
     * Retorna o motivo do ajuste informado, ou string vazia se nao preenchido.
     */
    public String getMotivoAjuste() {
        return this.view.getTxtMotivoAjuste().getText().trim();
    }

    /**
     * Retorna o numero da nota fiscal informado, ou string vazia se nao preenchido.
     */
    public String getNotaFiscal() {
        return this.view.getTxtNotaFiscal().getText().trim();
    }

    /**
     * Retorna o tipo de movimentacao selecionado na combo box.
     */
    public String getTipoMovimentacaoSelecionado() {
        Object item = this.view.getTipoMovimentacaoComboBox().getSelectedItem();
        return item != null ? item.toString() : null;
    }

    /**
     * Efetiva a movimentacao de estoque: atualiza a quantidade do produto,
     * persiste via repositorio e atualiza a view com o novo estoque.
     * Chamado pelos Commands apos validacao especifica de cada tipo.
     */
    public void efetivarMovimentacao(int quantidadeMovimentada) {
        if (this.produtoSelecionado == null) {
            throw new IllegalStateException("Nenhum produto selecionado para efetivar movimentação.");
        }

        int estoqueAnterior = this.produtoSelecionado.getQuantidadeInicial();
        int novoEstoque = estoqueAnterior + quantidadeMovimentada;

        this.produtoSelecionado.setQuantidadeInicial(novoEstoque);
        this.repository.atualizar(this.produtoSelecionado);

        // Atualiza a view com o estoque apos movimentacao
        this.view.getTxtQuantidadeAtual().setText(String.valueOf(novoEstoque));
        this.view.getTxtEstoqueAposMovimentacao().setText(String.valueOf(novoEstoque));
        this.view.getLblAvisoPrevisualizacao().setText("");
        this.view.getLblAvisoRegras().setText("Movimentação registrada com sucesso.");
    }

    /**
     * Exibe mensagem de erro na label de regras (usado pelos Commands).
     */
    public void exibirErro(String mensagem) {
        this.view.getLblAvisoRegras().setText(mensagem);
    }
}
