package br.com.fourbank.fourbank.application.port.in.transferencia;

import br.com.fourbank.fourbank.application.command.transferencia.ListarTransferenciasDaContaCommand;
import br.com.fourbank.fourbank.application.result.transferencia.TransferenciaResult;

public interface ListarTransferenciasDaContaUseCase {
    TransferenciaResult listar(ListarTransferenciasDaContaCommand command);
}
