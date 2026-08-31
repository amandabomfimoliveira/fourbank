package br.com.fourbank.fourbank.application.port.in.transferencia;

import br.com.fourbank.fourbank.application.command.transferencia.RealizarTransferenciaCommand;
import br.com.fourbank.fourbank.application.result.transferencia.TransferenciaResult;

public interface RealizarTransferenciaUseCase {
    TransferenciaResult realizar(RealizarTransferenciaCommand command);
}
