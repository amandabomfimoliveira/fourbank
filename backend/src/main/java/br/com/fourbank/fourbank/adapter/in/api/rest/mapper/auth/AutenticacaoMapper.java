package br.com.fourbank.fourbank.adapter.in.api.rest.mapper.auth;

import br.com.fourbank.fourbank.adapter.in.api.rest.dto.auth.AutenticacaoDto;
import br.com.fourbank.fourbank.adapter.in.api.rest.dto.auth.CadastrarUsuarioDto;
import br.com.fourbank.fourbank.adapter.in.api.rest.dto.auth.LoginDto;
import br.com.fourbank.fourbank.application.command.auth.CadastrarUsuarioCommand;
import br.com.fourbank.fourbank.application.command.auth.LoginCommand;
import br.com.fourbank.fourbank.application.result.auth.AuthResult;

public final class AutenticacaoMapper {

    private AutenticacaoMapper() {
    }

    public static CadastrarUsuarioCommand toCommand(CadastrarUsuarioDto dto) {
        return new CadastrarUsuarioCommand(dto.nome(), dto.email(), dto.senha());
    }

    public static LoginCommand toCommand(LoginDto dto) {
        return new LoginCommand(dto.email(), dto.senha());
    }

    public static AutenticacaoDto toDto(AuthResult result) {
        return new AutenticacaoDto(
                result.token(),
                result.tipo(),
                result.expiraEmSegundos()
        );
    }
}
