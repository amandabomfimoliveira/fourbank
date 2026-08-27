package br.com.fourbank.fourbank.adapter.in.api.rest.mapper.usuario;

import br.com.fourbank.fourbank.adapter.in.api.rest.dto.usuario.UsuarioDto;
import br.com.fourbank.fourbank.application.result.usuario.UsuarioResult;

public final class UsuarioMapper {

    private UsuarioMapper() {
    }

    public static UsuarioDto toDto(UsuarioResult result) {
        return new UsuarioDto(
                result.id(),
                result.nome(),
                result.documento(),
                result.tipoPessoa(),
                result.email(),
                result.perfil()
        );
    }
}
