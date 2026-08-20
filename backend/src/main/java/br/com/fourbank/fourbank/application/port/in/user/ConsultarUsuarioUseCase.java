package br.com.fourbank.fourbank.application.port.in.user;

import br.com.fourbank.fourbank.application.result.user.UsuarioResult;

public interface ConsultarUsuarioUseCase {

    UsuarioResult consultarPorEmail(String email);
}
