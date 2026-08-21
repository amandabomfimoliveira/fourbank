package br.com.fourbank.fourbank.application.validator.auth;

import br.com.fourbank.fourbank.application.exception.EmailJaCadastradoException;
import br.com.fourbank.fourbank.application.port.out.user.UsuarioRepositoryPort;

public class EmailDisponivelValidator {

    private final UsuarioRepositoryPort usuarioRepository;

    public EmailDisponivelValidator(UsuarioRepositoryPort usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public void validar(String email) {
        if (usuarioRepository.existePorEmail(email)) {
            throw new EmailJaCadastradoException();
        }
    }
}
