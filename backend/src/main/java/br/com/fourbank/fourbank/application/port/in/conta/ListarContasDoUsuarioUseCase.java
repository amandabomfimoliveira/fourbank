package br.com.fourbank.fourbank.application.port.in.conta;

import br.com.fourbank.fourbank.application.command.conta.ListarContasDoUsuarioCommand;
import br.com.fourbank.fourbank.application.result.conta.ContaResult;

import java.util.List;

public interface ListarContasDoUsuarioUseCase {
    List<ContaResult> listar(ListarContasDoUsuarioCommand command);
}
