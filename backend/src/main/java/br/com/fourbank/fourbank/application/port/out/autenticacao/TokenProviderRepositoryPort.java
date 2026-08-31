package br.com.fourbank.fourbank.application.port.out.autenticacao;

import br.com.fourbank.fourbank.application.model.usuario.Usuario;

public interface TokenProviderRepositoryPort {

    String gerarToken(Usuario usuario);

    long getExpirationSeconds();
}
