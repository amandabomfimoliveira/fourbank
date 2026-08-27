package br.com.fourbank.fourbank.application.usecase.usuario;

import br.com.fourbank.fourbank.application.exception.UsuarioNaoEncontradoException;
import br.com.fourbank.fourbank.application.port.in.usuario.ConsultarUsuarioUseCase;
import br.com.fourbank.fourbank.application.port.out.usuario.UsuarioRepositoryPort;
import br.com.fourbank.fourbank.application.result.usuario.UsuarioResult;

public class ConsultarUsuarioUseCaseImpl implements ConsultarUsuarioUseCase {

    private final UsuarioRepositoryPort usuarioRepository;

    public ConsultarUsuarioUseCaseImpl(UsuarioRepositoryPort usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UsuarioResult consultarPorEmail(String email) {
        return usuarioRepository.buscarPorEmail(email)
                .map(UsuarioResult::from)
                .orElseThrow(UsuarioNaoEncontradoException::new);
    }
}
