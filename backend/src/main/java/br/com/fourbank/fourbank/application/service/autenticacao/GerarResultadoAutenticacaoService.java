package br.com.fourbank.fourbank.application.service.autenticacao;

import br.com.fourbank.fourbank.application.model.usuario.Usuario;
import br.com.fourbank.fourbank.application.port.out.autenticacao.TokenProviderRepositoryPort;
import br.com.fourbank.fourbank.application.result.autenticacao.AutenticacaoResult;

public class GerarResultadoAutenticacaoService {

    private final TokenProviderRepositoryPort tokenProvider;

    public GerarResultadoAutenticacaoService(TokenProviderRepositoryPort tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    public AutenticacaoResult gerar(Usuario usuario) {
        return new AutenticacaoResult(
                tokenProvider.gerarToken(usuario),
                "Bearer",
                tokenProvider.getExpirationSeconds()
        );
    }
}
