package br.com.fourbank.fourbank.application.usecase.transferencia;

import br.com.fourbank.fourbank.application.command.transferencia.ListarTransferenciasDaContaCommand;
import br.com.fourbank.fourbank.application.exception.ContaNaoEncontradaException;
import br.com.fourbank.fourbank.application.exception.TransferenciaNaoEncontradaException;
import br.com.fourbank.fourbank.application.exception.UsuarioNaoEncontradoException;
import br.com.fourbank.fourbank.application.model.conta.Conta;
import br.com.fourbank.fourbank.application.model.transferencia.Transferencia;
import br.com.fourbank.fourbank.application.model.usuario.Usuario;
import br.com.fourbank.fourbank.application.port.in.transferencia.ListarTransferenciasDaContaUseCase;
import br.com.fourbank.fourbank.application.port.out.conta.ContaRepositoryPort;
import br.com.fourbank.fourbank.application.port.out.transferencia.TransferenciaRepositoryPort;
import br.com.fourbank.fourbank.application.port.out.usuario.UsuarioRepositoryPort;
import br.com.fourbank.fourbank.application.result.transferencia.TransferenciaResult;

import java.util.List;

public class ListarTransferenciasDaContaUseCaseImpl implements ListarTransferenciasDaContaUseCase {

    private final ContaRepositoryPort contaRepository;

    private final UsuarioRepositoryPort usuarioRepository;

    private final TransferenciaRepositoryPort transferenciaRepository;

    public ListarTransferenciasDaContaUseCaseImpl(ContaRepositoryPort contaRepository, UsuarioRepositoryPort usuarioRepository, TransferenciaRepositoryPort transferenciaRepository) {
        this.contaRepository = contaRepository;
        this.usuarioRepository = usuarioRepository;
        this.transferenciaRepository = transferenciaRepository;
    }

    @Override
    public List<TransferenciaResult> listar(ListarTransferenciasDaContaCommand command) {
        Usuario usuario = usuarioRepository
            .buscarPorEmail(command.emailUsuario())
            .orElseThrow(UsuarioNaoEncontradoException::new);

        Conta conta = contaRepository.buscarNaoEncerradaPorUsuarioIdETipo(usuario.getId(), command.tipoConta()).orElseThrow(ContaNaoEncontradaException::new);

        return transferenciaRepository.listarPorConta(conta.getId()).stream()
            .map(TransferenciaResult::from)
            .toList();



    }
}
