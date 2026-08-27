package br.com.fourbank.fourbank.application.usecase.conta;

import br.com.fourbank.fourbank.application.command.conta.ListarContasDoUsuarioCommand;
import br.com.fourbank.fourbank.application.exception.ContaNaoEncontradaException;
import br.com.fourbank.fourbank.application.model.usuario.Usuario;
import br.com.fourbank.fourbank.application.port.in.conta.ListarContasDoUsuarioUseCase;
import br.com.fourbank.fourbank.application.port.out.conta.ContaRepositoryPort;
import br.com.fourbank.fourbank.application.port.out.usuario.UsuarioRepositoryPort;
import br.com.fourbank.fourbank.application.result.conta.ContaResult;
import br.com.fourbank.fourbank.application.validator.usuario.TextoObrigatorioValidator;

import java.util.List;
import java.util.Objects;

public class ListarContasDoUsuarioUseCaseImpl implements ListarContasDoUsuarioUseCase {
    private final ContaRepositoryPort contaRepository;

    private final UsuarioRepositoryPort usuarioRepository;
    public ListarContasDoUsuarioUseCaseImpl(ContaRepositoryPort contaRepository, UsuarioRepositoryPort usuarioRepository) {
        this.contaRepository = contaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public List<ContaResult> listar(ListarContasDoUsuarioCommand command) {
        Objects.requireNonNull(command, "O comando de listagem de contas é obrigatório");
        TextoObrigatorioValidator.validar(command.email(), "e-mail");

        Usuario usuario = usuarioRepository.buscarPorEmail(command.email()).orElseThrow(ContaNaoEncontradaException::new);

         return contaRepository.listarPorUsuario(usuario.getId()).stream()
                .map(ContaResult::from)
                 .toList();
    }
}
