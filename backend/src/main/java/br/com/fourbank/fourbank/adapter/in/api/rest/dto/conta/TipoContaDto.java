package br.com.fourbank.fourbank.adapter.in.api.rest.dto.conta;

import br.com.fourbank.fourbank.application.model.conta.TipoConta;
import jakarta.validation.constraints.NotNull;

public record TipoContaDto(
        @NotNull(message = "O tipo da conta é obrigatório")
        TipoConta tipo
) {
}