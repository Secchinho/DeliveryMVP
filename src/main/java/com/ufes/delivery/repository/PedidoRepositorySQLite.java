package com.ufes.delivery.repository;

import br.ufes.logauditoria.ILogger;
import com.ufes.delivery.model.CupomDescontoPedido;
import com.ufes.delivery.model.CupomDescontoEntrega;
import com.ufes.delivery.model.Pedido;
import com.ufes.delivery.model.Produto;
import com.ufes.delivery.model.Item;
import com.ufes.delivery.model.Cliente;
import com.ufes.singleton.ConexaoSQLite;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author raphael
 */
public class PedidoRepositorySQLite implements IPedidoRepository {

    private static final DateTimeFormatter FMT_DATA_HORA
            = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private final String url;
    private ILogger logger;

    public PedidoRepositorySQLite() {
        this.url = ConexaoSQLite.getInstancia().getURL();

        String sqlPedido = "CREATE TABLE IF NOT EXISTS tbPedido ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "codigo INTEGER NOT NULL UNIQUE, "
                + "dataHora TEXT NOT NULL, "
                + "estado TEXT NOT NULL, "
                + "taxaEntrega DOUBLE NOT NULL, "
                + "cliente_id INTEGER NOT NULL, "
                + "cupomCodigo TEXT, "
                + "cupomPercentual DOUBLE, "
                + "cupomDataInicio TEXT, "
                + "cupomDataFim TEXT, "
                + "FOREIGN KEY (cliente_id) REFERENCES tbCliente(id) "
                + "ON DELETE RESTRICT"
                + ");";

        String sqlItem = "CREATE TABLE IF NOT EXISTS tbPedidoItem ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "pedido_id INTEGER NOT NULL, "
                + "produto_id INTEGER NOT NULL, "
                + "quantidade INTEGER NOT NULL, "
                + "valorUnitario DOUBLE NOT NULL, "
                + "FOREIGN KEY (pedido_id) REFERENCES tbPedido(id) "
                + "ON DELETE CASCADE, "
                + "FOREIGN KEY (produto_id) REFERENCES tbProduto(id) "
                + "ON DELETE RESTRICT"
                + ");";
        String sqlCupomEntrega = "CREATE TABLE IF NOT EXISTS tbPedidoCupomEntrega ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "pedido_id INTEGER NOT NULL, "
                + "nomeMetodo TEXT NOT NULL, "
                + "valorDesconto DOUBLE NOT NULL, "
                + "FOREIGN KEY (pedido_id) REFERENCES tbPedido(id) "
                + "ON DELETE CASCADE"
                + ");";

        try (var conn = DriverManager.getConnection(this.url); var stmt = conn.createStatement()) {
            stmt.execute(sqlPedido);
            stmt.execute(sqlItem);
            stmt.execute(sqlCupomEntrega);
        } catch (SQLException e) {
            System.out.println("ERRO!!! " + e.getMessage());
        }
    }
    
    public PedidoRepositorySQLite(ILogger logger) {
        this();
        if (logger != null) {
            this.logger = logger;
        }
    }
    
    public void setLogger(ILogger logger) {
        if (logger != null) {
            this.logger = logger;
        }
    }

    @Override
    public Optional<Pedido> buscarPorId(int id) {
        String sql = "SELECT id, codigo, dataHora, estado, taxaEntrega, "
                + "cliente_id, cupomCodigo, cupomPercentual, "
                + "cupomDataInicio, cupomDataFim FROM tbPedido "
                + "WHERE codigo = ?";

        try (var conn = DriverManager.getConnection(this.url); var stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(lerPedido(conn, rs));
            }
        } catch (SQLException e) {
            System.out.println("ERRO!!! " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public void adicionar(Pedido pedido) {
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido não pode ser nulo");
        }

        if (buscarPorId(pedido.getCodigoPedido()).isPresent()) {
            throw new IllegalStateException(
                    "Já existe pedido com código " + pedido.getCodigoPedido());
        }

        String sqlInsere = "INSERT INTO tbPedido(codigo, dataHora, estado, "
                + "taxaEntrega, cliente_id, cupomCodigo, cupomPercentual, "
                + "cupomDataInicio, cupomDataFim) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (var conn = DriverManager.getConnection(this.url)) {
            conn.setAutoCommit(false);
            try {
                int clienteId = obterClienteId(pedido);
                Optional<CupomDescontoPedido> cupom = pedido.getCupomAplicado();

                try (var stmt = conn.prepareStatement(sqlInsere)) {
                    stmt.setInt(1, pedido.getCodigoPedido());
                    stmt.setString(2, pedido.getData().format(FMT_DATA_HORA));
                    stmt.setString(3, pedido.getEstado());
                    stmt.setDouble(4, pedido.getTaxaEntrega());
                    stmt.setInt(5, clienteId);

                    if (cupom.isPresent()) {
                        stmt.setString(6, cupom.get().getCodigo());
                        stmt.setDouble(7, cupom.get().getPercentual());
                        stmt.setString(8, cupom.get().getDataHoraInicio().format(FMT_DATA_HORA));
                        stmt.setString(9, cupom.get().getDataHoraFim().format(FMT_DATA_HORA));
                    } else {
                        stmt.setNull(6, java.sql.Types.VARCHAR);
                        stmt.setNull(7, java.sql.Types.DOUBLE);
                        stmt.setNull(8, java.sql.Types.VARCHAR);
                        stmt.setNull(9, java.sql.Types.VARCHAR);
                    }
                    stmt.executeUpdate();
                }

                // Itens e cupons de taxa de entrega só são persistidos
                // depois que o pedido ganhou id (FKs exigem).
                int pedidoIdInterno = obterIdInternoDoPedido(conn, pedido.getCodigoPedido());
                inserirItens(conn, pedidoIdInterno, pedido);
                inserirCuponsEntrega(conn, pedidoIdInterno, pedido);

                conn.commit();
            } catch (RuntimeException | SQLException ex) {
                conn.rollback();
                throw ex;
            }
        } catch (SQLException e) {
            System.out.println("ERRO!!! " + e.getMessage());
        }
    }

    @Override
    public void atualizar(Pedido pedido) {
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido não pode ser nulo");
        }

        String sqlBuscaIdInterno = "SELECT id FROM tbPedido WHERE codigo = ?";
        String sqlAtualiza = "UPDATE tbPedido SET dataHora = ?, estado = ?, "
                + "taxaEntrega = ?, cliente_id = ?, cupomCodigo = ?, "
                + "cupomPercentual = ?, cupomDataInicio = ?, cupomDataFim = ? "
                + "WHERE codigo = ?";

        try (var conn = DriverManager.getConnection(this.url)) {
            conn.setAutoCommit(false);
            try {
                int pedidoIdInterno = -1;
                try (var stmtBusca = conn.prepareStatement(sqlBuscaIdInterno)) {
                    stmtBusca.setInt(1, pedido.getCodigoPedido());
                    var rs = stmtBusca.executeQuery();
                    if (!rs.next()) {
                        throw new IllegalArgumentException(
                                "Pedido inexistente: " + pedido.getCodigoPedido());
                    }
                    pedidoIdInterno = rs.getInt("id");
                }

                int clienteId = obterClienteId(pedido);
                Optional<CupomDescontoPedido> cupom = pedido.getCupomAplicado();

                try (var stmt = conn.prepareStatement(sqlAtualiza)) {
                    stmt.setString(1, pedido.getData().format(FMT_DATA_HORA));
                    stmt.setString(2, pedido.getEstado());
                    stmt.setDouble(3, pedido.getTaxaEntrega());
                    stmt.setInt(4, clienteId);

                    if (cupom.isPresent()) {
                        stmt.setString(5, cupom.get().getCodigo());
                        stmt.setDouble(6, cupom.get().getPercentual());
                        stmt.setString(7, cupom.get().getDataHoraInicio().format(FMT_DATA_HORA));
                        stmt.setString(8, cupom.get().getDataHoraFim().format(FMT_DATA_HORA));
                    } else {
                        stmt.setNull(5, java.sql.Types.VARCHAR);
                        stmt.setNull(6, java.sql.Types.DOUBLE);
                        stmt.setNull(7, java.sql.Types.VARCHAR);
                        stmt.setNull(8, java.sql.Types.VARCHAR);
                    }
                    stmt.setInt(9, pedido.getCodigoPedido());
                    stmt.executeUpdate();
                }

                // Substitui todos os itens e cupons de entrega: apaga e
                // reinsere. É o equivalente SQLite ao "set(i, pedido)" da
                // versão em memória.
                apagarItens(conn, pedidoIdInterno);
                apagarCuponsEntrega(conn, pedidoIdInterno);
                inserirItens(conn, pedidoIdInterno, pedido);
                inserirCuponsEntrega(conn, pedidoIdInterno, pedido);

                conn.commit();
            } catch (RuntimeException | SQLException ex) {
                conn.rollback();
                throw ex;
            }
        } catch (SQLException e) {
            System.out.println("ERRO!!! " + e.getMessage());
        }
    }

    @Override
    public List<Pedido> listarPedidos() {
        String sql = "SELECT id, codigo, dataHora, estado, taxaEntrega, "
                + "cliente_id, cupomCodigo, cupomPercentual, "
                + "cupomDataInicio, cupomDataFim FROM tbPedido "
                + "ORDER BY codigo";

        List<Pedido> pedidos = new ArrayList<>();
        try (var conn = DriverManager.getConnection(this.url); var stmt = conn.createStatement(); var rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                pedidos.add(lerPedido(conn, rs));
            }
        } catch (SQLException e) {
            System.out.println("ERRO!!! " + e.getMessage());
        }
        return Collections.unmodifiableList(pedidos);
    }

    @Override
    public void removerPedido(int id) {
        String sqlBusca = "SELECT id FROM tbPedido WHERE codigo = ?";
        String sqlDel = "DELETE FROM tbPedido WHERE codigo = ?";

        try (var conn = DriverManager.getConnection(this.url)) {
            conn.setAutoCommit(false);
            try {
                try (var stmtBusca = conn.prepareStatement(sqlBusca)) {
                    stmtBusca.setInt(1, id);
                    var rs = stmtBusca.executeQuery();
                    if (!rs.next()) {
                        throw new IllegalArgumentException("Pedido inexistente: " + id);
                    }
                }
                // Itens e cupons de entrega caem automaticamente por
                // ON DELETE CASCADE.
                try (var stmtDel = conn.prepareStatement(sqlDel)) {
                    stmtDel.setInt(1, id);
                    stmtDel.executeUpdate();
                }
                conn.commit();
            } catch (RuntimeException | SQLException ex) {
                conn.rollback();
                throw ex;
            }
        } catch (SQLException e) {
            System.out.println("ERRO!!! " + e.getMessage());
        }
    }

    private Pedido lerPedido(java.sql.Connection conn, java.sql.ResultSet rs)
            throws SQLException {

        int codigo = rs.getInt("codigo");
        LocalDateTime dataHora = LocalDateTime.parse(
                rs.getString("dataHora"), FMT_DATA_HORA);
        String estado = rs.getString("estado");
        double taxaEntrega = rs.getDouble("taxaEntrega");
        int clienteId = rs.getInt("cliente_id");

        Cliente cliente = carregarCliente(clienteId);
        if (cliente == null) {
            // Cliente foi removido; não deveria ocorrer por causa do
            // ON DELETE RESTRICT em tbPedido, mas defendemos contra
            // estado inconsistente do banco.
            throw new SQLException("Cliente não encontrado (id=" + clienteId
                    + ") para o pedido " + codigo);
        }

        Pedido pedido = new Pedido(dataHora, cliente, codigo, logger);
        pedido.setEstado(estado);

        int pedidoIdInterno = rs.getInt("id");
        carregarItens(conn, pedidoIdInterno, pedido);
        carregarCuponsEntrega(conn, pedidoIdInterno, pedido);

        String cupomCodigo = rs.getString("cupomCodigo");
        if (cupomCodigo != null && !cupomCodigo.isBlank()) {
            double percentual = rs.getDouble("cupomPercentual");
            LocalDateTime dataInicio = LocalDateTime.parse(
                    rs.getString("cupomDataInicio"), FMT_DATA_HORA);
            LocalDateTime dataFim = LocalDateTime.parse(
                    rs.getString("cupomDataFim"), FMT_DATA_HORA);
            CupomDescontoPedido cupom = new CupomDescontoPedido(
                    cupomCodigo, percentual, dataInicio, dataFim);
            pedido.setCupomAplicado(cupom);
        }

        return pedido;
    }

    private Cliente carregarCliente(int clienteId) {
        // Reaproveita o repositório SQLite de clientes para montar o
        // Cliente (incluindo endereços), evitando duplicar a lógica de
        // leitura. O construtor de ClienteRepositorySQLite garante que
        // as tabelas existam — chamá-lo aqui é seguro.
        try {
            return new ClienteRepositorySQLite().getPorIdCliente(clienteId)
                    .orElse(null);
        } catch (RuntimeException ex) {
            return null;
        }
    }

    private void carregarItens(java.sql.Connection conn, int pedidoIdInterno,
            Pedido pedido) throws SQLException {
        String sql = "SELECT i.quantidade, i.valorUnitario, "
                + "p.id, p.nome, p.codigo, p.categoria, "
                + "p.precoUnitario, p.quantidadeInicial "
                + "FROM tbPedidoItem i "
                + "INNER JOIN tbProduto p ON p.id = i.produto_id "
                + "WHERE i.pedido_id = ?";

        try (var stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pedidoIdInterno);
            var rs = stmt.executeQuery();
            while (rs.next()) {
                Produto produto = new Produto(
                        rs.getString("nome"),
                        rs.getString("codigo"),
                        rs.getString("categoria"),
                        rs.getDouble("precoUnitario"),
                        rs.getInt("quantidadeInicial"));
                produto.setId(rs.getInt("id"));

                Item item = new Item(
                        produto.getNome(),
                        rs.getInt("quantidade"),
                        rs.getDouble("valorUnitario"),
                        produto.getCategoria());
                item.setProduto(produto);
                // adicionarItem() registra log; para reconstrução
                // silenciosa adicionamos diretamente via reflexão no
                // campo itens. Como não há setter, usamos o método
                // público adicionarItem — que é seguro e já valida
                // o item.
                pedido.adicionarItem(item);
            }
        }
    }

    private void carregarCuponsEntrega(java.sql.Connection conn,
            int pedidoIdInterno, Pedido pedido) throws SQLException {
        String sql = "SELECT nomeMetodo, valorDesconto "
                + "FROM tbPedidoCupomEntrega WHERE pedido_id = ?";

        try (var stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pedidoIdInterno);
            var rs = stmt.executeQuery();
            while (rs.next()) {
                CupomDescontoEntrega cupom = new CupomDescontoEntrega(
                        rs.getString("nomeMetodo"),
                        rs.getDouble("valorDesconto"));
                // adicionarCupomDescontoEntrega() valida o limite da
                // taxa; como a taxa já foi restaurada acima, o limite
                // deve bater com o original.
                pedido.adicionarCupomDescontoEntrega(cupom);
            }
        }
    }

    // ------------------------------------------------------------------
    // Helpers de escrita
    // ------------------------------------------------------------------
    private void inserirItens(java.sql.Connection conn, int pedidoIdInterno,
            Pedido pedido) throws SQLException {
        String sql = "INSERT INTO tbPedidoItem(pedido_id, produto_id, "
                + "quantidade, valorUnitario) VALUES (?, ?, ?, ?)";

        try (var stmt = conn.prepareStatement(sql)) {
            for (Item item : pedido.getItens()) {
                Produto p = item.getProduto();
                if (p == null || p.getId() <= 0) {
                    // Item sem produto persistido: não é possível
                    // referenciar via FK. Pula para não quebrar a
                    // transação inteira.
                    continue;
                }
                stmt.setInt(1, pedidoIdInterno);
                stmt.setInt(2, p.getId());
                stmt.setInt(3, item.getQuantidade());
                stmt.setDouble(4, item.getValorUnitario());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    private void apagarItens(java.sql.Connection conn, int pedidoIdInterno)
            throws SQLException {
        try (var stmt = conn.prepareStatement(
                "DELETE FROM tbPedidoItem WHERE pedido_id = ?")) {
            stmt.setInt(1, pedidoIdInterno);
            stmt.executeUpdate();
        }
    }

    private void inserirCuponsEntrega(java.sql.Connection conn,
            int pedidoIdInterno, Pedido pedido) throws SQLException {
        String sql = "INSERT INTO tbPedidoCupomEntrega(pedido_id, "
                + "nomeMetodo, valorDesconto) VALUES (?, ?, ?)";

        try (var stmt = conn.prepareStatement(sql)) {
            for (CupomDescontoEntrega cupom : pedido.getCupomDescontoEntrega()) {
                stmt.setInt(1, pedidoIdInterno);
                stmt.setString(2, cupom.getNomeMetodo());
                stmt.setDouble(3, cupom.getValorDesconto());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    private void apagarCuponsEntrega(java.sql.Connection conn,
            int pedidoIdInterno) throws SQLException {
        try (var stmt = conn.prepareStatement(
                "DELETE FROM tbPedidoCupomEntrega WHERE pedido_id = ?")) {
            stmt.setInt(1, pedidoIdInterno);
            stmt.executeUpdate();
        }
    }

    /**
     * Resolve o id interno (auto-incremento) do pedido a partir do código de
     * negócio.
     */
    private int obterIdInternoDoPedido(java.sql.Connection conn, int codigo)
            throws SQLException {
        try (var stmt = conn.prepareStatement(
                "SELECT id FROM tbPedido WHERE codigo = ?")) {
            stmt.setInt(1, codigo);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        }
        throw new SQLException(
                "Pedido recém-inserido não encontrado (codigo=" + codigo + ")");
    }

    /**
     * Obtém o id do cliente associado ao pedido. Reaproveita o repositório
     * SQLite para evitar duplicar a consulta.
     */
    private int obterClienteId(Pedido pedido) {
        Cliente cliente = pedido.getCliente();
        if (cliente == null) {
            throw new IllegalStateException(
                    "Pedido sem cliente não pode ser persistido");
        }
        if (cliente.getId() > 0) {
            return cliente.getId();
        }
        // Cliente ainda sem id interno (somente CPF): tenta resolver.
        Optional<Cliente> encontrado = new ClienteRepositorySQLite()
                .getPorCPF(cliente.getCPF());
        if (encontrado.isEmpty()) {
            throw new IllegalStateException(
                    "Cliente não persistido: " + cliente.getCPF());
        }
        return encontrado.get().getId();
    }
}
