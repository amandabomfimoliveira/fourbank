package br.com.fourbank.fourbank.application.port.out.auth;

import br.com.fourbank.fourbank.domain.model.Usuario;

public interface AutenticadorPort {

    Usuario autenticar(String email, String senhaPura);
}
