package br.com.fourbank.fourbank.application.port.in.transferencia;

import br.com.fourbank.fourbank.application.command.transferencia.ConsultarTransferenciaCommand;
import br.com.fourbank.fourbank.application.result.transferencia.TransferenciaResult;

public interface ConsultarTransferenciaUseCase {
    TransferenciaResult consultar(ConsultarTransferenciaCommand command);
}
