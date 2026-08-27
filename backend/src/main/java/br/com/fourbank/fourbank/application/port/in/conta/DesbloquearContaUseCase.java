package br.com.fourbank.fourbank.application.port.in.conta;

import br.com.fourbank.fourbank.application.command.conta.DesbloquearContaCommand;
import br.com.fourbank.fourbank.application.result.conta.ContaResult;

public interface DesbloquearContaUseCase {
    ContaResult desbloquear(DesbloquearContaCommand command);
}
