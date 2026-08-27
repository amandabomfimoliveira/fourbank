package br.com.fourbank.fourbank.adapter.in.api.rest.mapper.autenticacao;

import br.com.fourbank.fourbank.adapter.in.api.rest.dto.autenticacao.AutenticacaoDto;
import br.com.fourbank.fourbank.adapter.in.api.rest.dto.autenticacao.CadastrarUsuarioDto;
import br.com.fourbank.fourbank.adapter.in.api.rest.dto.autenticacao.LoginDto;
import br.com.fourbank.fourbank.application.command.autenticacao.CadastrarUsuarioCommand;
import br.com.fourbank.fourbank.application.command.autenticacao.LoginCommand;
import br.com.fourbank.fourbank.application.model.conta.TipoConta;
import br.com.fourbank.fourbank.application.model.usuario.TipoPessoa;
import br.com.fourbank.fourbank.application.result.autenticacao.AutenticacaoResult;

public final class AutenticacaoMapper {

    private AutenticacaoMapper() {
    }

    public static CadastrarUsuarioCommand toCommand(CadastrarUsuarioDto dto) {
        return new CadastrarUsuarioCommand(
                dto.nome(),
                dto.documento(),
                TipoPessoa.valueOf(dto.tipoPessoa()),
                dto.email(),
                dto.senha(),
                TipoConta.valueOf(dto.tipoConta())
        );
    }

    public static LoginCommand toCommand(LoginDto dto) {
        return new LoginCommand(dto.email(), dto.senha());
    }

    public static AutenticacaoDto toDto(AutenticacaoResult result) {
        return new AutenticacaoDto(
                result.token(),
                result.tipo(),
                result.expiraEmSegundos()
        );
    }
}
