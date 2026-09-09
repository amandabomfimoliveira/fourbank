package br.com.fourbank.fourbank.adapter.in.api.rest.dto.transferencia;

import br.com.fourbank.fourbank.application.model.transferencia.StatusTransferencia;

import java.math.BigDecimal;
import java.time.Instant;

public record TransferenciaDto(
    Long id,
    Long contaOrigemId,
    Long contaDestinoId,
    BigDecimal valor,
    BigDecimal taxa,
    StatusTransferencia status,
    Instant solicitadaEm,
    Instant agendadaPara,
    Instant realizadaEm
){
}
