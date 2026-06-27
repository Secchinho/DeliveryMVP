package com.ufes.delivery;

import br.ufes.logauditoria.CsvLoggerImpl;
import br.ufes.logauditoria.ILogger;
import br.ufes.logauditoria.JsonlLoggerImpl;
import br.ufes.logauditoria.XmlLoggerImpl;
import com.ufes.delivery.desconto.pedido.AplicadorCupomPedidoService;
import com.ufes.delivery.desconto.taxa.entrega.CalculadoraTaxaDescontoPedidoService;
import com.ufes.delivery.model.Cliente;
import com.ufes.delivery.model.CupomDescontoPedido;
import com.ufes.delivery.model.Item;
import com.ufes.delivery.model.Pedido;
import com.ufes.delivery.repository.CupomPedidoRepositoryEmMemoria;
import java.time.LocalDateTime;

public class UmCasoDeUsoDePedido {

    public static void main(String[] args) {

        Cliente cliente = new Cliente("12345678910","Maria", "Ouro", 1, "Limoeiro", "Cidade Maravilhosa", "Castelo");
        LocalDateTime dataPedido = LocalDateTime.now();
        ILogger logCSV = new CsvLoggerImpl("TesteCSV.csv");
        ILogger logJSONL = new JsonlLoggerImpl("TesteJSONL.jsonl");
        ILogger logXML = new XmlLoggerImpl("TesteXML.xml");

        //Testando CSV
        Pedido pedido = new Pedido(dataPedido, cliente, 1001, logCSV);
        pedido.adicionarItem(new Item("Caderno", 2, 10.50, "Educacao"));
        pedido.adicionarItem(new Item("Biscoito", 4, 5.80, "Alimentacao"));

        System.out.println("Chamando getValorPedido()...");
        System.out.println("Valor: R$ " + String.format("%.2f", pedido.getValorPedido()));

        //Testando JSONL
        pedido.setLogger(logJSONL);

        System.out.println("Chamando calcularValorTotal()...");
        System.out.println("Total: R$ " + String.format("%.2f", pedido.calcularValorTotal()));

        //Testando XML
        pedido.setLogger(logXML);

        System.out.println("Chamando getValorPedido()...");
        System.out.println("Valor: R$ " + String.format("%.2f", pedido.getValorPedido()));

        //Testando cupom CSV
        pedido.setLogger(logCSV);

        CupomPedidoRepositoryEmMemoria cupomRepo = new CupomPedidoRepositoryEmMemoria();
        cupomRepo.adicionarCupom(
                new CupomDescontoPedido("PROMO15", 15.0,
                        dataPedido.minusDays(1), dataPedido.plusDays(1)));

        AplicadorCupomPedidoService aplicador = new AplicadorCupomPedidoService(cupomRepo, logCSV);
        System.out.println("Aplicando cupom PROMO15...");
        aplicador.aplicarCupom(pedido, "PROMO15", LocalDateTime.now());
        System.out.println("Cupom aplicado!\n");

        //Testando cupom JSONL
        aplicador.setLogger(logJSONL);

        try {
            aplicador.aplicarCupom(pedido, "NAOEXISTE", LocalDateTime.now());
        } catch (RuntimeException ex) {
            System.out.println("Excecao: " + ex.getMessage());
        }

        //Testando codigo vazio XML
        aplicador.setLogger(logXML);

        try {
            aplicador.aplicarCupom(pedido, "", LocalDateTime.now());
        } catch (RuntimeException ex) {
            System.out.println("Excecao: " + ex.getMessage());
        }

        //Testando cupom fora da validade CSV
        aplicador.setLogger(logCSV);

        try {
            aplicador.aplicarCupom(pedido, "DESC10", LocalDateTime.now());
        } catch (RuntimeException ex) {
            System.out.println("Excecao: " + ex.getMessage());
        }

        //CalculadoraDesconto CSV
        CalculadoraTaxaDescontoPedidoService calculadora = new CalculadoraTaxaDescontoPedidoService(logCSV);
        System.out.println("Calculando descontos...");
        calculadora.calcularDesconto(pedido);
        pedido.getCupomDescontoEntrega().forEach(c -> System.out.println("  " + c));

        //CalculadoraDesconto JSONL
        pedido.limparCuponsDescontoEntrega();
        calculadora.setLogger(logJSONL);
        calculadora.calcularDesconto(pedido);
        pedido.getCupomDescontoEntrega().forEach(c -> System.out.println("  " + c));

        //CalculadoraDesconto XML
        pedido.limparCuponsDescontoEntrega();
        calculadora.setLogger(logXML);
        calculadora.calcularDesconto(pedido);
        pedido.getCupomDescontoEntrega().forEach(c -> System.out.println("  " + c));
    }
}
