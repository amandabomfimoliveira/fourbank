package br.com.fourbank.fourbank.application.result.usuario;

import br.com.fourbank.fourbank.application.model.usuario.Usuario;

public record UsuarioResult(
        Long id,
        String nome,
        String documento,
        String tipoPessoa,
        String email,
        String perfil
) {

    public static UsuarioResult from(Usuario usuario) {
        return new UsuarioResult(
                usuario.getId(),
                usuario.getNome(),
                usuario.getDocumento(),
                usuario.getTipoPessoa().name(),
                usuario.getEmail(),
                usuario.getRole().name()
        );
    }
}
