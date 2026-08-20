package br.com.fourbank.fourbank.application.service.user;

import br.com.fourbank.fourbank.application.port.in.user.ConsultarUsuarioUseCase;
import br.com.fourbank.fourbank.application.port.out.user.UsuarioRepositoryPort;
import br.com.fourbank.fourbank.application.result.user.UsuarioResult;
import br.com.fourbank.fourbank.application.exception.UsuarioNaoEncontradoException;

public class UsuarioApplicationService implements ConsultarUsuarioUseCase {

    private final UsuarioRepositoryPort usuarioRepository;

    public UsuarioApplicationService(UsuarioRepositoryPort usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UsuarioResult consultarPorEmail(String email) {
        return usuarioRepository.buscarPorEmail(email)
                .map(UsuarioResult::from)
                .orElseThrow(UsuarioNaoEncontradoException::new);
    }
}
