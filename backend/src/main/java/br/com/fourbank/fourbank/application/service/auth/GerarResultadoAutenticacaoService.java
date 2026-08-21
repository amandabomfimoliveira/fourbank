package br.com.fourbank.fourbank.application.service.auth;

import br.com.fourbank.fourbank.application.model.Usuario;
import br.com.fourbank.fourbank.application.port.out.auth.TokenProviderPort;
import br.com.fourbank.fourbank.application.result.auth.AuthResult;

public class GerarResultadoAutenticacaoService {

    private final TokenProviderPort tokenProvider;

    public GerarResultadoAutenticacaoService(TokenProviderPort tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    public AuthResult gerar(Usuario usuario) {
        return new AuthResult(
                tokenProvider.gerarToken(usuario),
                "Bearer",
                tokenProvider.getExpirationSeconds()
        );
    }
}
