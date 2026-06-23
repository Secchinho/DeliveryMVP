package com.ufes.delivery.repository;

import com.ufes.delivery.model.CupomDescontoPedido;
import java.util.Map;
import java.util.Optional;

public interface ICupomRepository {
    public void adicionarCupom(CupomDescontoPedido cupom);
    public void atualizarCupom(CupomDescontoPedido cupom);
    public void removerCupom(String codigo);
    public void removerCuponsExpirados();
    public Optional<CupomDescontoPedido> buscarCupom(String codigo);
    public Map<String, CupomDescontoPedido> getCuponsDisponiveis();
}
