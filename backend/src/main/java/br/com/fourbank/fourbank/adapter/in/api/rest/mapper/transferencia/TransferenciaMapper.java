package br.com.fourbank.fourbank.adapter.in.api.rest.mapper.transferencia;

import br.com.fourbank.fourbank.adapter.in.api.rest.dto.conta.TipoContaDto;
import br.com.fourbank.fourbank.adapter.in.api.rest.dto.transferencia.AgendarTransferenciaDto;
import br.com.fourbank.fourbank.adapter.in.api.rest.dto.transferencia.RealizarTransferenciaDto;
import br.com.fourbank.fourbank.adapter.in.api.rest.dto.transferencia.TransferenciaDto;
import br.com.fourbank.fourbank.application.command.transferencia.AgendarTransferenciaCommand;
import br.com.fourbank.fourbank.application.command.transferencia.ConsultarTransferenciaCommand;
import br.com.fourbank.fourbank.application.command.transferencia.ListarTransferenciasDaContaCommand;
import br.com.fourbank.fourbank.application.command.transferencia.RealizarTransferenciaCommand;
import br.com.fourbank.fourbank.application.model.conta.TipoConta;
import br.com.fourbank.fourbank.application.result.transferencia.TransferenciaResult;

public class TransferenciaMapper {
    private TransferenciaMapper() {
    }

    public static RealizarTransferenciaCommand toRealizarTransferenciaCommand(
        String emailUsuario,
        RealizarTransferenciaDto dto

    ){
        return new RealizarTransferenciaCommand(
            emailUsuario,
            dto.tipoContaOrigem(),
            dto.nomeDestinatario(),
            dto.documentoDestinatario(),
            dto.agenciaDestino(),
            dto.numeroContaDestino(),
            dto.tipoContaDestino(),
            dto.valor());
    }

    public static AgendarTransferenciaCommand toAgendarTransferenciaCommand(
        String emailUsuario,
        AgendarTransferenciaDto dto
    ){
      return new AgendarTransferenciaCommand(
          emailUsuario,
          dto.tipoContaOrigem(),
          dto.nomeDestinatario(),
          dto.documentoDestinatario(),
          dto.agenciaDestino(),
          dto.numeroContaDestino(),
          dto.tipoContaDestino(),
          dto.valor(),
          dto.agendadaPara()
      );
    }

    public static ConsultarTransferenciaCommand toConsultarTransferenciaCommand(
        String emailUsuario,
        Long transferenciaId
    ){
        return new ConsultarTransferenciaCommand(
            emailUsuario,
            transferenciaId
        );
    }

    public static ListarTransferenciasDaContaCommand toListarTransferenciasDaContaCommand(
        String emailUsuario,
        TipoConta tipoConta
    ){
        return new ListarTransferenciasDaContaCommand(
            emailUsuario,
            tipoConta
        );
    }

    public static TransferenciaDto toDto(
        TransferenciaResult result
    ) {
        return new TransferenciaDto(
            result.id(),
            result.contaOrigemId(),
            result.contaDestinoId(),
            result.valor(),
            result.taxa(),
            result.status(),
            result.solicitadaEm(),
            result.agendadaPara(),
            result.realizadaEm()
        );
    }
}
