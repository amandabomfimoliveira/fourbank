package br.com.fourbank.fourbank.application.port.in.usuario;

import br.com.fourbank.fourbank.application.result.usuario.UsuarioResult;

public interface ConsultarUsuarioUseCase {

    UsuarioResult consultarPorEmail(String email);
}
