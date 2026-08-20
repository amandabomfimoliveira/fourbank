package br.com.fourbank.fourbank.adapter.in.api.rest.dto.auth;

import br.com.fourbank.fourbank.application.command.auth.LoginCommand;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "Informe um e-mail válido")
        String email,

        @NotBlank(message = "A senha é obrigatória")
        String senha
) {
    public LoginCommand toCommand() {
        return new LoginCommand(email, senha);
    }
}
