package br.com.fourbank.fourbank.application.command.transferencia;

import br.com.fourbank.fourbank.application.model.conta.TipoConta;

import java.math.BigDecimal;
import java.time.Instant;

public record AgendarTransferenciaCommand(
    String emailUsuario,
    TipoConta tipoContaOrigem,
    String nomeDestinatario,
    String documentoDestinatario,
    String agenciaDestino,
    String numeroContaDestino,
    TipoConta tipoContaDestino,
    BigDecimal valor,
    Instant agendadaPara
) {
}
