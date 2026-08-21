package br.com.fourbank.fourbank.application.port.in.auth;

import br.com.fourbank.fourbank.application.command.auth.CadastrarUsuarioCommand;
import br.com.fourbank.fourbank.application.result.auth.AuthResult;

public interface CadastrarUsuarioUseCase {

    AuthResult cadastrar(CadastrarUsuarioCommand command);
}
