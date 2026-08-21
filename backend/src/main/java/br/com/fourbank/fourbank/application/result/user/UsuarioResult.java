package br.com.fourbank.fourbank.application.result.user;

import br.com.fourbank.fourbank.application.model.Usuario;

public record UsuarioResult(Long id, String nome, String email, String perfil) {

    public static UsuarioResult from(Usuario usuario) {
        return new UsuarioResult(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getRole().name()
        );
    }
}
