package br.com.fourbank.fourbank.application.port.in.transferencia;

import br.com.fourbank.fourbank.application.command.transferencia.AgendarTransferenciaCommand;
import br.com.fourbank.fourbank.application.result.transferencia.TransferenciaResult;

public interface AgendarTransferenciaUseCase {
    TransferenciaResult agendar(AgendarTransferenciaCommand command);
}
