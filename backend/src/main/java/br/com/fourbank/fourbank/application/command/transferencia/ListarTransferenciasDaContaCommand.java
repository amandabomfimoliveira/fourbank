package br.com.fourbank.fourbank.application.command.transferencia;

import br.com.fourbank.fourbank.application.model.conta.TipoConta;

public record ListarTransferenciasDaContaCommand(
    String emailUsuario,
    TipoConta tipoConta
) {

}
