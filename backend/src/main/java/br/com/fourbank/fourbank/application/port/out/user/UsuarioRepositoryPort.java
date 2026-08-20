package br.com.fourbank.fourbank.application.port.out.user;

import br.com.fourbank.fourbank.domain.model.Usuario;

import java.util.Optional;

public interface UsuarioRepositoryPort {

    Optional<Usuario> buscarPorEmail(String email);

    boolean existePorEmail(String email);

    Usuario salvar(Usuario usuario);
}
