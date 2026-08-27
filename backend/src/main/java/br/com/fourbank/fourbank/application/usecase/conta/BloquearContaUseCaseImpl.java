package br.com.fourbank.fourbank.application.usecase.conta;

import br.com.fourbank.fourbank.application.command.conta.BloquearContaCommand;
import br.com.fourbank.fourbank.application.model.conta.Conta;
import br.com.fourbank.fourbank.application.port.in.conta.BloquearContaUseCase;
import br.com.fourbank.fourbank.application.port.out.conta.ContaRepositoryPort;
import br.com.fourbank.fourbank.application.result.conta.ContaResult;
import br.com.fourbank.fourbank.application.service.conta.BuscarContaDoUsuarioService;
import br.com.fourbank.fourbank.application.validator.usuario.TextoObrigatorioValidator;
import jakarta.transaction.Transactional;
import java.util.Objects;

public class BloquearContaUseCaseImpl implements BloquearContaUseCase {
    private final ContaRepositoryPort contaRepository;


    private final BuscarContaDoUsuarioService buscarContaDoUsuarioService;

    public BloquearContaUseCaseImpl(ContaRepositoryPort contaRepository, BuscarContaDoUsuarioService buscarContaDoUsuarioService) {
        this.contaRepository = contaRepository;
        this.buscarContaDoUsuarioService = buscarContaDoUsuarioService;
    }

    @Override
    @Transactional
    public ContaResult bloquear(BloquearContaCommand command) {
        Objects.requireNonNull(command, "O comando de bloquear conta é obrigatório");
        Objects.requireNonNull(command.tipo(), "O tipo de conta é obrigatório");
        TextoObrigatorioValidator.validar(command.email(), "e-mail");


        Conta contaEncontrada = buscarContaDoUsuarioService.buscar(command.email(), command.tipo());

        contaEncontrada.bloquear();

        Conta contaSalva = contaRepository.salvar(contaEncontrada);

        return ContaResult.from(contaSalva);

    }
}
