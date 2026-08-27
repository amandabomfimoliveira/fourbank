package br.com.fourbank.fourbank.adapter.in.api.rest.dto.conta;

import br.com.fourbank.fourbank.application.model.conta.StatusConta;
import br.com.fourbank.fourbank.application.model.conta.TipoConta;

import java.math.BigDecimal;

public record ContaDto(
        Long id,
        String numero,
        String agencia,
        TipoConta tipo,
        BigDecimal saldo,
        StatusConta status
) {
}
