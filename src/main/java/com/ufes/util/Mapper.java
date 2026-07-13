/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufes.util;

import br.ufes.logauditoria.RegistroDeLogDTO;
import java.time.LocalDateTime;


/**
 *
 * @author raphael
 */
public class Mapper {
    public static RegistroDeLogDTO convertToLog(int codigoPedido, 
            String nomeMetodo, String nomeCliente){
        UsuarioLogadoService user = UsuarioLogadoService.getInstance();
        String nomeUsuario = user.getNome();
        LocalDateTime momentoExato = LocalDateTime.now();
        String nomeOperacao = "Calculo do valor total do pedido (" + nomeMetodo + ")";
                
        return new RegistroDeLogDTO(nomeUsuario, momentoExato, codigoPedido,
        nomeOperacao, nomeCliente);
    }
}
