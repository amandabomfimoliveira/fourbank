package br.com.fourbank.fourbank.application.port.out.autenticacao;

import br.com.fourbank.fourbank.application.model.usuario.Usuario;

public interface TokenProviderPort {

    String gerarToken(Usuario usuario);

    long getExpirationSeconds();
}
