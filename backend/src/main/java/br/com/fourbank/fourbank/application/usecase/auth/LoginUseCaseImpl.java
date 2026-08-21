package br.com.fourbank.fourbank.application.usecase.auth;

import br.com.fourbank.fourbank.application.command.auth.LoginCommand;
import br.com.fourbank.fourbank.application.model.Usuario;
import br.com.fourbank.fourbank.application.port.in.auth.LoginUseCase;
import br.com.fourbank.fourbank.application.port.out.auth.AutenticadorPort;
import br.com.fourbank.fourbank.application.result.auth.AuthResult;
import br.com.fourbank.fourbank.application.service.auth.GerarResultadoAutenticacaoService;
import br.com.fourbank.fourbank.application.service.auth.NormalizarEmailService;

import java.util.Objects;

public class LoginUseCaseImpl implements LoginUseCase {

    private final AutenticadorPort autenticador;
    private final NormalizarEmailService normalizarEmailService;
    private final GerarResultadoAutenticacaoService gerarResultadoAutenticacaoService;

    public LoginUseCaseImpl(
            AutenticadorPort autenticador,
            NormalizarEmailService normalizarEmailService,
            GerarResultadoAutenticacaoService gerarResultadoAutenticacaoService
    ) {
        this.autenticador = autenticador;
        this.normalizarEmailService = normalizarEmailService;
        this.gerarResultadoAutenticacaoService = gerarResultadoAutenticacaoService;
    }

    @Override
    public AuthResult login(LoginCommand command) {
        Objects.requireNonNull(command, "O comando de login é obrigatório");

        Usuario usuario = autenticador.autenticar(
                normalizarEmailService.normalizar(command.email()),
                command.senha()
        );

        return gerarResultadoAutenticacaoService.gerar(usuario);
    }
}
