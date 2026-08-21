package br.com.fourbank.fourbank.application.usecase.auth;

import br.com.fourbank.fourbank.application.command.auth.CadastrarUsuarioCommand;
import br.com.fourbank.fourbank.application.model.Usuario;
import br.com.fourbank.fourbank.application.port.in.auth.CadastrarUsuarioUseCase;
import br.com.fourbank.fourbank.application.port.out.auth.CodificadorSenhaPort;
import br.com.fourbank.fourbank.application.port.out.user.UsuarioRepositoryPort;
import br.com.fourbank.fourbank.application.result.auth.AuthResult;
import br.com.fourbank.fourbank.application.service.auth.GerarResultadoAutenticacaoService;
import br.com.fourbank.fourbank.application.service.auth.NormalizarEmailService;
import br.com.fourbank.fourbank.application.validator.auth.EmailDisponivelValidator;

import java.util.Objects;

public class CadastrarUsuarioUseCaseImpl implements CadastrarUsuarioUseCase {

    private final UsuarioRepositoryPort usuarioRepository;
    private final CodificadorSenhaPort codificadorSenha;
    private final NormalizarEmailService normalizarEmailService;
    private final EmailDisponivelValidator emailDisponivelValidator;
    private final GerarResultadoAutenticacaoService gerarResultadoAutenticacaoService;

    public CadastrarUsuarioUseCaseImpl(
            UsuarioRepositoryPort usuarioRepository,
            CodificadorSenhaPort codificadorSenha,
            NormalizarEmailService normalizarEmailService,
            EmailDisponivelValidator emailDisponivelValidator,
            GerarResultadoAutenticacaoService gerarResultadoAutenticacaoService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.codificadorSenha = codificadorSenha;
        this.normalizarEmailService = normalizarEmailService;
        this.emailDisponivelValidator = emailDisponivelValidator;
        this.gerarResultadoAutenticacaoService = gerarResultadoAutenticacaoService;
    }

    @Override
    public AuthResult cadastrar(CadastrarUsuarioCommand command) {
        Objects.requireNonNull(command, "O comando de cadastro é obrigatório");

        String email = normalizarEmailService.normalizar(command.email());
        emailDisponivelValidator.validar(email);

        Usuario usuario = Usuario.novo(
                command.nome().trim(),
                email,
                codificadorSenha.codificar(command.senha())
        );
        Usuario usuarioSalvo = usuarioRepository.salvar(usuario);

        return gerarResultadoAutenticacaoService.gerar(usuarioSalvo);
    }
}
