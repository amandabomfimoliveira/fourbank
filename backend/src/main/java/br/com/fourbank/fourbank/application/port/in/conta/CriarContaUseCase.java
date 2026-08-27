package br.com.fourbank.fourbank.application.port.in.conta;

import br.com.fourbank.fourbank.application.command.conta.CriarContaCommand;
import br.com.fourbank.fourbank.application.result.conta.ContaResult;

public interface CriarContaUseCase {
    ContaResult criar(CriarContaCommand command);
}
