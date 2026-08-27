package br.com.fourbank.fourbank.application.command.conta;

public record ConsultarContaCommand(
        String numero,
        String emailUsuario
) {
}
