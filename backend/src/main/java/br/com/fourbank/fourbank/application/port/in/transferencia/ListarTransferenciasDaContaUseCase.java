package br.com.fourbank.fourbank.application.port.in.transferencia;

import br.com.fourbank.fourbank.application.command.transferencia.ListarTransferenciasDaContaCommand;
import br.com.fourbank.fourbank.application.result.transferencia.TransferenciaResult;

import java.util.List;

public interface ListarTransferenciasDaContaUseCase {
    List<TransferenciaResult> listar(ListarTransferenciasDaContaCommand command);
}
