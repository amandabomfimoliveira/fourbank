package br.com.fourbank.fourbank.application.result.conta;

import br.com.fourbank.fourbank.application.model.conta.Conta;
import br.com.fourbank.fourbank.application.model.conta.StatusConta;
import br.com.fourbank.fourbank.application.model.conta.TipoConta;

import java.math.BigDecimal;

public record ContaResult(
        Long id,
        String numero,
        String agencia,
        TipoConta tipo,
        StatusConta status,
        BigDecimal saldo
) {
    public static ContaResult from (Conta conta){
        return new ContaResult(
                conta.getId(),
                conta.getNumero(),
                conta.getAgencia(),
                conta.getTipo(),
                conta.getStatus(),
                conta.getSaldo()
        );
    }
}
