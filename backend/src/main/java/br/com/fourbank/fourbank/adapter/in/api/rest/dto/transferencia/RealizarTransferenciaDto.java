package br.com.fourbank.fourbank.adapter.in.api.rest.dto.transferencia;

import br.com.fourbank.fourbank.application.model.conta.TipoConta;

import java.math.BigDecimal;

public record RealizarTransferenciaDto(
    TipoConta tipoContaOrigem,
    String nomeDestinatario,
    String documentoDestinatario,
    String agenciaDestino,
    String numeroContaDestino,
    TipoConta tipoContaDestino,
    BigDecimal valor
) {
}
