package br.com.fourbank.fourbank.application.usecase.autenticacao;

import br.com.fourbank.fourbank.application.command.autenticacao.LoginCommand;
import br.com.fourbank.fourbank.application.model.usuario.Usuario;
import br.com.fourbank.fourbank.application.port.in.autenticacao.LoginUseCase;
import br.com.fourbank.fourbank.application.port.out.autenticacao.AutenticadorPort;
import br.com.fourbank.fourbank.application.result.autenticacao.AutenticacaoResult;
import br.com.fourbank.fourbank.application.service.autenticacao.GerarResultadoAutenticacaoService;
import br.com.fourbank.fourbank.application.service.autenticacao.NormalizarEmailService;

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
    public AutenticacaoResult login(LoginCommand command) {
        Objects.requireNonNull(command, "O comando de login é obrigatório");

        Usuario usuario = autenticador.autenticar(
                normalizarEmailService.normalizar(command.email()),
                command.senha()
        );

        return gerarResultadoAutenticacaoService.gerar(usuario);
    }
}
