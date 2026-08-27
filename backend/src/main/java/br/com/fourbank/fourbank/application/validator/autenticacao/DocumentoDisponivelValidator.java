package br.com.fourbank.fourbank.application.validator.autenticacao;

import br.com.fourbank.fourbank.application.exception.DocumentoJaCadastradoException;
import br.com.fourbank.fourbank.application.port.out.usuario.UsuarioRepositoryPort;

public class DocumentoDisponivelValidator {

    private final UsuarioRepositoryPort usuarioRepository;

    public DocumentoDisponivelValidator(UsuarioRepositoryPort usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public void validar(String documento) {
        if (usuarioRepository.existePorDocumento(documento)) {
            throw new DocumentoJaCadastradoException();
        }
    }
}
