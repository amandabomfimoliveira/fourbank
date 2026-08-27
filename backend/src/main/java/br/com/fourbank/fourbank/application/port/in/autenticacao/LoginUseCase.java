package br.com.fourbank.fourbank.application.port.in.autenticacao;

import br.com.fourbank.fourbank.application.command.autenticacao.LoginCommand;
import br.com.fourbank.fourbank.application.result.autenticacao.AutenticacaoResult;

public interface LoginUseCase {

    AutenticacaoResult login(LoginCommand command);
}
