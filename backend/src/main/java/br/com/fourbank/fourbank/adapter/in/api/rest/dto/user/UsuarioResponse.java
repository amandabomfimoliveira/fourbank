package br.com.fourbank.fourbank.adapter.in.api.rest.dto.user;

import br.com.fourbank.fourbank.application.result.user.UsuarioResult;

public record UsuarioResponse(Long id, String nome, String email, String perfil) {

    public static UsuarioResponse from(UsuarioResult result) {
        return new UsuarioResponse(result.id(), result.nome(), result.email(), result.perfil());
    }
}
