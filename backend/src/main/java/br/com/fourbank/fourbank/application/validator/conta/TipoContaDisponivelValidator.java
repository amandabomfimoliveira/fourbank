package br.com.fourbank.fourbank.application.validator.conta;

import br.com.fourbank.fourbank.application.exception.UsuarioJaPossuiContaDoTipoException;
import br.com.fourbank.fourbank.application.model.conta.TipoConta;
import br.com.fourbank.fourbank.application.port.out.conta.ContaRepositoryPort;

import java.util.Objects;

public final class TipoContaDisponivelValidator {
    private final ContaRepositoryPort contaRepository;

    public TipoContaDisponivelValidator(ContaRepositoryPort contaRepository) {
        this.contaRepository = contaRepository;
    }

    public void validar(Long usuarioId, TipoConta tipo) {
        Objects.requireNonNull(usuarioId, "O ID do usuário é obrigatório");
        Objects.requireNonNull(tipo, "O tipo da conta é obrigatório");

        boolean usuarioJaPossuiConta = contaRepository.existeNaoEncerradaPorUsuarioIdETipo(usuarioId, tipo);

        if (usuarioJaPossuiConta) {
            throw new UsuarioJaPossuiContaDoTipoException(tipo);
        }
    }

}
