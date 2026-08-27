package br.com.fourbank.fourbank.application.command.conta;

import br.com.fourbank.fourbank.application.model.conta.TipoConta;

public record EncerrarContaCommand(
        String email,
        TipoConta tipo
) {
}
