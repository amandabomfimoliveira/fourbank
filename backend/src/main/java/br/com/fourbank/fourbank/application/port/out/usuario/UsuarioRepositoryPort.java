package br.com.fourbank.fourbank.application.port.out.usuario;

import br.com.fourbank.fourbank.application.model.usuario.Usuario;

import java.util.Optional;

public interface UsuarioRepositoryPort {

    Optional<Usuario> buscarPorId(Long id);

    Optional<Usuario> buscarPorEmail(String email);

    boolean existePorEmail(String email);

    boolean existePorDocumento(String documento);

    Usuario salvar(Usuario usuario);
}
