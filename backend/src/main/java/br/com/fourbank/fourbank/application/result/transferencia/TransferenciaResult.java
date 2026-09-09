package br.com.fourbank.fourbank.application.result.transferencia;

import br.com.fourbank.fourbank.application.model.transferencia.StatusTransferencia;
import br.com.fourbank.fourbank.application.model.transferencia.Transferencia;

import java.math.BigDecimal;
import java.time.Instant;

public record TransferenciaResult(
    Long id,
    Long contaOrigemId,
    Long contaDestinoId,
    BigDecimal valor,
    BigDecimal taxa,
    StatusTransferencia status,
    Instant solicitadaEm,
    Instant agendadaPara,
    Instant realizadaEm
) {
    public static TransferenciaResult from(Transferencia transferencia) {
        return new TransferenciaResult(
            transferencia.getId(),
            transferencia.getContaOrigemId(),
            transferencia.getContaDestinoId(),
            transferencia.getValor(),
            transferencia.getTaxa(),
            transferencia.getStatus(),
            transferencia.getSolicitadaEm(),
            transferencia.getAgendadaPara(),
            transferencia.getRealizadaEm()
        );
    }
}
