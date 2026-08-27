package br.com.fourbank.fourbank.application.usecase.conta;

import br.com.fourbank.fourbank.application.command.conta.EncerrarContaCommand;
import br.com.fourbank.fourbank.application.exception.ContaNaoEncontradaException;
import br.com.fourbank.fourbank.application.model.conta.Conta;
import br.com.fourbank.fourbank.application.model.usuario.Usuario;
import br.com.fourbank.fourbank.application.port.in.conta.EncerrarContaUseCase;
import br.com.fourbank.fourbank.application.port.out.conta.ContaRepositoryPort;
import br.com.fourbank.fourbank.application.port.out.usuario.UsuarioRepositoryPort;
import br.com.fourbank.fourbank.application.service.conta.BuscarContaDoUsuarioService;
import br.com.fourbank.fourbank.application.validator.usuario.TextoObrigatorioValidator;
import jakarta.transaction.Transactional;

import java.util.Objects;

public class EncerrarContaUseCaseImpl implements EncerrarContaUseCase {
    private final ContaRepositoryPort contaRepository;

    private final BuscarContaDoUsuarioService buscarContaDoUsuarioService;


    public EncerrarContaUseCaseImpl(ContaRepositoryPort contaRepository, BuscarContaDoUsuarioService buscarContaDoUsuarioService) {
        this.contaRepository = contaRepository;
        this.buscarContaDoUsuarioService = buscarContaDoUsuarioService;
    }

    @Override
    @Transactional
    public void encerrar(EncerrarContaCommand command) {
        Objects.requireNonNull(command, "O comando de encerrar conta é obrigatório");
        Objects.requireNonNull(command.tipo(), "O tipo de conta é obrigatório");
        TextoObrigatorioValidator.validar(command.email(), "e-mail");

        Conta contaEncontrada = buscarContaDoUsuarioService.buscar(command.email(), command.tipo());

        contaEncontrada.encerrar();

       contaRepository.salvar(contaEncontrada);
    }
}
