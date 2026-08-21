package br.com.fourbank.fourbank.application.port.out.auth;

import br.com.fourbank.fourbank.application.model.Usuario;

public interface TokenProviderPort {

    String gerarToken(Usuario usuario);

    long getExpirationSeconds();
}
