package br.com.fourbank.fourbank.application.port.in.auth;

import br.com.fourbank.fourbank.application.command.auth.LoginCommand;
import br.com.fourbank.fourbank.application.result.auth.AuthResult;

public interface LoginUseCase {

    AuthResult login(LoginCommand command);
}
