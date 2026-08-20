package br.com.fourbank.fourbank.application.service.auth;

import br.com.fourbank.fourbank.application.command.user.CadastrarUsuarioCommand;
import br.com.fourbank.fourbank.application.command.auth.LoginCommand;
import br.com.fourbank.fourbank.application.port.in.auth.AutenticacaoUseCase;
import br.com.fourbank.fourbank.application.port.out.auth.AutenticadorPort;
import br.com.fourbank.fourbank.application.port.out.auth.CodificadorSenhaPort;
import br.com.fourbank.fourbank.application.port.out.auth.TokenProviderPort;
import br.com.fourbank.fourbank.application.port.out.user.UsuarioRepositoryPort;
import br.com.fourbank.fourbank.application.result.auth.AuthResult;
import br.com.fourbank.fourbank.application.exception.EmailJaCadastradoException;
import br.com.fourbank.fourbank.domain.model.Usuario;

import java.util.Locale;

public class AuthApplicationService implements AutenticacaoUseCase {

    private final UsuarioRepositoryPort usuarioRepository;
    private final CodificadorSenhaPort codificadorSenha;
    private final AutenticadorPort autenticador;
    private final TokenProviderPort tokenProvider;

    public AuthApplicationService(
            UsuarioRepositoryPort usuarioRepository,
            CodificadorSenhaPort codificadorSenha,
            AutenticadorPort autenticador,
            TokenProviderPort tokenProvider
    ) {
        this.usuarioRepository = usuarioRepository;
        this.codificadorSenha = codificadorSenha;
        this.autenticador = autenticador;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public AuthResult cadastrar(CadastrarUsuarioCommand command) {
        String email = normalizarEmail(command.email());
        if (usuarioRepository.existePorEmail(email)) {
            throw new EmailJaCadastradoException();
        }

        var usuario = Usuario.novo(
                command.nome().trim(),
                email,
                codificadorSenha.codificar(command.senha())
        );
        Usuario usuarioSalvo = usuarioRepository.salvar(usuario);
        return criarResultado(usuarioSalvo);
    }

    @Override
    public AuthResult login(LoginCommand command) {
        Usuario usuario = autenticador.autenticar(
                normalizarEmail(command.email()),
                command.senha()
        );
        return criarResultado(usuario);
    }

    private AuthResult criarResultado(Usuario usuario) {
        return new AuthResult(
                tokenProvider.gerarToken(usuario),
                "Bearer",
                tokenProvider.getExpirationSeconds()
        );
    }

    private String normalizarEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
