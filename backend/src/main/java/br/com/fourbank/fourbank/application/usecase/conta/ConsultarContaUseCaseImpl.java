package br.com.fourbank.fourbank.application.usecase.conta;

import br.com.fourbank.fourbank.application.command.conta.ConsultarContaCommand;
import br.com.fourbank.fourbank.application.exception.ContaNaoEncontradaException;
import br.com.fourbank.fourbank.application.exception.UsuarioNaoEncontradoException;
import br.com.fourbank.fourbank.application.model.conta.Conta;
import br.com.fourbank.fourbank.application.model.usuario.Usuario;
import br.com.fourbank.fourbank.application.port.in.conta.ConsultarContaUseCase;
import br.com.fourbank.fourbank.application.port.out.conta.ContaRepositoryPort;
import br.com.fourbank.fourbank.application.port.out.usuario.UsuarioRepositoryPort;
import br.com.fourbank.fourbank.application.result.conta.ContaResult;
import br.com.fourbank.fourbank.application.validator.usuario.TextoObrigatorioValidator;

import java.util.Objects;


public class ConsultarContaUseCaseImpl implements ConsultarContaUseCase {
    private final ContaRepositoryPort contaRepository;

    private final UsuarioRepositoryPort usuarioRepository;

    public ConsultarContaUseCaseImpl(ContaRepositoryPort contaRepository, UsuarioRepositoryPort usuarioRepository) {
        this.contaRepository = contaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public ContaResult consultar(ConsultarContaCommand command) {
        Objects.requireNonNull(command, "O comando de consulta conta é obrigatório");

        TextoObrigatorioValidator.validar(command.emailUsuario(), "e-mail");
        TextoObrigatorioValidator.validar(command.numero(), "número");

        Usuario usuarioEncontrado = usuarioRepository.buscarPorEmail(command.emailUsuario()).orElseThrow(UsuarioNaoEncontradoException::new);

        Conta conta = contaRepository.buscarPorNumero(command.numero()).orElseThrow(ContaNaoEncontradaException::new);

        if (!conta.getUsuarioId().equals(usuarioEncontrado.getId())){
            throw new ContaNaoEncontradaException();
        }

        return ContaResult.from(conta);
    }
}
