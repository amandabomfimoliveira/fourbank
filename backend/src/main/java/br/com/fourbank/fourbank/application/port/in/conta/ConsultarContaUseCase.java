package br.com.fourbank.fourbank.application.port.in.conta;

import br.com.fourbank.fourbank.application.command.conta.ConsultarContaCommand;
import br.com.fourbank.fourbank.application.result.conta.ContaResult;

public interface ConsultarContaUseCase {
    ContaResult consultar(ConsultarContaCommand command);
}
