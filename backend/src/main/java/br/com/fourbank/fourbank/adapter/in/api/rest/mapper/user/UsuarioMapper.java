package br.com.fourbank.fourbank.adapter.in.api.rest.mapper.user;

import br.com.fourbank.fourbank.adapter.in.api.rest.dto.user.UsuarioDto;
import br.com.fourbank.fourbank.application.result.user.UsuarioResult;

public final class UsuarioMapper {

    private UsuarioMapper() {
    }

    public static UsuarioDto toDto(UsuarioResult result) {
        return new UsuarioDto(
                result.id(),
                result.nome(),
                result.email(),
                result.perfil()
        );
    }
}
