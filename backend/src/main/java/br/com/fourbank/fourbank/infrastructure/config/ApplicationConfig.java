package br.com.fourbank.fourbank.infrastructure.config;

import br.com.fourbank.fourbank.application.port.in.auth.CadastrarUsuarioUseCase;
import br.com.fourbank.fourbank.application.port.in.auth.LoginUseCase;
import br.com.fourbank.fourbank.application.port.in.user.ConsultarUsuarioUseCase;
import br.com.fourbank.fourbank.application.port.out.auth.AutenticadorPort;
import br.com.fourbank.fourbank.application.port.out.auth.CodificadorSenhaPort;
import br.com.fourbank.fourbank.application.port.out.auth.TokenProviderPort;
import br.com.fourbank.fourbank.application.port.out.user.UsuarioRepositoryPort;
import br.com.fourbank.fourbank.application.service.auth.GerarResultadoAutenticacaoService;
import br.com.fourbank.fourbank.application.service.auth.NormalizarEmailService;
import br.com.fourbank.fourbank.application.usecase.auth.CadastrarUsuarioUseCaseImpl;
import br.com.fourbank.fourbank.application.usecase.auth.LoginUseCaseImpl;
import br.com.fourbank.fourbank.application.usecase.user.ConsultarUsuarioUseCaseImpl;
import br.com.fourbank.fourbank.application.validator.auth.EmailDisponivelValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    NormalizarEmailService normalizarEmailService() {
        return new NormalizarEmailService();
    }

    @Bean
    GerarResultadoAutenticacaoService gerarResultadoAutenticacaoService(
            TokenProviderPort tokenProvider
    ) {
        return new GerarResultadoAutenticacaoService(tokenProvider);
    }

    @Bean
    EmailDisponivelValidator emailDisponivelValidator(
            UsuarioRepositoryPort usuarioRepository
    ) {
        return new EmailDisponivelValidator(usuarioRepository);
    }

    @Bean
    CadastrarUsuarioUseCase cadastrarUsuarioUseCase(
            UsuarioRepositoryPort usuarioRepository,
            CodificadorSenhaPort codificadorSenha,
            NormalizarEmailService normalizarEmailService,
            EmailDisponivelValidator emailDisponivelValidator,
            GerarResultadoAutenticacaoService gerarResultadoAutenticacaoService
    ) {
        return new CadastrarUsuarioUseCaseImpl(
                usuarioRepository,
                codificadorSenha,
                normalizarEmailService,
                emailDisponivelValidator,
                gerarResultadoAutenticacaoService
        );
    }

    @Bean
    LoginUseCase loginUseCase(
            AutenticadorPort autenticador,
            NormalizarEmailService normalizarEmailService,
            GerarResultadoAutenticacaoService gerarResultadoAutenticacaoService
    ) {
        return new LoginUseCaseImpl(
                autenticador,
                normalizarEmailService,
                gerarResultadoAutenticacaoService
        );
    }

    @Bean
    ConsultarUsuarioUseCase consultarUsuarioUseCase(UsuarioRepositoryPort usuarioRepository) {
        return new ConsultarUsuarioUseCaseImpl(usuarioRepository);
    }
}
