package br.com.fourbank.fourbank.adapter.in.api.rest.dto.autenticacao;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CadastrarUsuarioDto(
        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 120, message = "O nome deve ter no máximo 120 caracteres")
        String nome,

        @NotBlank(message = "O documento é obrigatório")
        @Pattern(regexp = "(?:\\d{11}|\\d{14})", message = "O documento deve possuir 11 ou 14 dígitos")
        String documento,

        @NotBlank(message = "O tipo de pessoa é obrigatório")
        @Pattern(regexp = "FISICA|JURIDICA", message = "O tipo de pessoa deve ser FISICA ou JURIDICA")
        String tipoPessoa,

        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "Informe um e-mail válido")
        @Size(max = 160, message = "O e-mail deve ter no máximo 160 caracteres")
        String email,

        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 8, max = 72, message = "A senha deve ter entre 8 e 72 caracteres")
        String senha,

        @NotBlank(message = "O tipo da conta é obrigatório")
        @Pattern(regexp = "CORRENTE|POUPANCA", message = "O tipo da conta deve ser CORRENTE ou POUPANCA")
        String tipoConta
) {
}
