package br.com.fourbank.fourbank.application.port.in.conta;

import br.com.fourbank.fourbank.application.command.conta.EncerrarContaCommand;

public interface EncerrarContaUseCase {
    void encerrar(EncerrarContaCommand command);
}
