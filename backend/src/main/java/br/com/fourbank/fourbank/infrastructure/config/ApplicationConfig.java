package br.com.fourbank.fourbank.infrastructure.config;

import br.com.fourbank.fourbank.application.port.in.auth.AutenticacaoUseCase;
import br.com.fourbank.fourbank.application.port.in.user.ConsultarUsuarioUseCase;
import br.com.fourbank.fourbank.application.port.out.auth.AutenticadorPort;
import br.com.fourbank.fourbank.application.port.out.auth.CodificadorSenhaPort;
import br.com.fourbank.fourbank.application.port.out.auth.TokenProviderPort;
import br.com.fourbank.fourbank.application.port.out.user.UsuarioRepositoryPort;
import br.com.fourbank.fourbank.application.service.auth.AuthApplicationService;
import br.com.fourbank.fourbank.application.service.user.UsuarioApplicationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    AutenticacaoUseCase autenticacaoUseCase(
            UsuarioRepositoryPort usuarioRepository,
            CodificadorSenhaPort codificadorSenha,
            AutenticadorPort autenticador,
            TokenProviderPort tokenProvider
    ) {
        return new AuthApplicationService(
                usuarioRepository,
                codificadorSenha,
                autenticador,
                tokenProvider
        );
    }

    @Bean
    ConsultarUsuarioUseCase consultarUsuarioUseCase(UsuarioRepositoryPort usuarioRepository) {
        return new UsuarioApplicationService(usuarioRepository);
    }
}
