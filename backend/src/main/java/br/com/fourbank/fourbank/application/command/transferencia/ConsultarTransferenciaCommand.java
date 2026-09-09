package br.com.fourbank.fourbank.application.command.transferencia;

public record ConsultarTransferenciaCommand(
    String emailUsuario,
    Long transferenciaId
) {

}
