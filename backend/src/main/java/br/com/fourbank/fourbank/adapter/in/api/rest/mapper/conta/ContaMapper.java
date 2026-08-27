package br.com.fourbank.fourbank.adapter.in.api.rest.mapper.conta;

import br.com.fourbank.fourbank.adapter.in.api.rest.dto.conta.ContaDto;
import br.com.fourbank.fourbank.adapter.in.api.rest.dto.conta.TipoContaDto;
import br.com.fourbank.fourbank.application.command.conta.*;
import br.com.fourbank.fourbank.application.result.conta.ContaResult;

public class ContaMapper {
    private ContaMapper() {
    }

    public static CriarContaCommand toCriarContaCommand(
            TipoContaDto dto,
            String emailUsuario
    ) {
        return new CriarContaCommand(
                emailUsuario,
                dto.tipo()
        );
    }

    public static BloquearContaCommand toBloquearContaCommand(
            TipoContaDto dto,
            String email
    ){
        return new BloquearContaCommand(email, dto.tipo());
    }

    public static DesbloquearContaCommand toDesbloquearContaCommand(
            TipoContaDto dto,
            String email
    ){
        return new DesbloquearContaCommand(
                email, dto.tipo()
        );
    }

    public static EncerrarContaCommand toEncerrarContaCommand(
            TipoContaDto dto,
            String email
    ){
        return new EncerrarContaCommand(email,dto.tipo());
    }

    public static ConsultarContaCommand toConsultarContaCommand(
            String numero,
            String email
    ){
        return new ConsultarContaCommand(numero,email);
    }

    public static ListarContasDoUsuarioCommand toListarContasDoUsuarioCommand(
            String email
    ){
        return new ListarContasDoUsuarioCommand(email);
    }
    public static ContaDto toDto(ContaResult result) {
        return new ContaDto(
                result.id(),
                result.numero(),
                result.agencia(),
                result.tipo(),
                result.saldo(),
                result.status()
        );
    }
}
