package br.com.fourbank.fourbank.application.usecase.transferencia;

import br.com.fourbank.fourbank.application.command.transferencia.ConsultarTransferenciaCommand;
import br.com.fourbank.fourbank.application.exception.ContaNaoEncontradaException;
import br.com.fourbank.fourbank.application.exception.TransferenciaNaoEncontradaException;
import br.com.fourbank.fourbank.application.exception.UsuarioNaoEncontradoException;
import br.com.fourbank.fourbank.application.model.conta.Conta;
import br.com.fourbank.fourbank.application.model.transferencia.Transferencia;
import br.com.fourbank.fourbank.application.model.usuario.Usuario;
import br.com.fourbank.fourbank.application.port.in.transferencia.ConsultarTransferenciaUseCase;
import br.com.fourbank.fourbank.application.port.out.conta.ContaRepositoryPort;
import br.com.fourbank.fourbank.application.port.out.transferencia.TransferenciaRepositoryPort;
import br.com.fourbank.fourbank.application.port.out.usuario.UsuarioRepositoryPort;
import br.com.fourbank.fourbank.application.result.transferencia.TransferenciaResult;

import java.util.Objects;

public class ConsultarTransferenciaUseCaseImpl implements ConsultarTransferenciaUseCase {
    private final ContaRepositoryPort contaRepository;

    private final UsuarioRepositoryPort usuarioRepository;

    private final TransferenciaRepositoryPort transferenciaRepository;

    public ConsultarTransferenciaUseCaseImpl(ContaRepositoryPort contaRepository, UsuarioRepositoryPort usuarioRepository, TransferenciaRepositoryPort transferenciaRepository) {
        this.contaRepository = contaRepository;
        this.usuarioRepository = usuarioRepository;
        this.transferenciaRepository = transferenciaRepository;
    }

    @Override
    public TransferenciaResult consultar(ConsultarTransferenciaCommand command) {
        Objects.requireNonNull(command, "O comando de consulta de transferência é obrigatório");

        Usuario usuario = usuarioRepository
            .buscarPorEmail(command.emailUsuario())
            .orElseThrow(UsuarioNaoEncontradoException::new);


        Transferencia transferencia = transferenciaRepository.buscarPorId(command.transferenciaId()).orElseThrow(TransferenciaNaoEncontradaException::new);

        Conta contaOrigem = contaRepository.buscarPorId(transferencia.getContaOrigemId()).orElseThrow(ContaNaoEncontradaException::new);

        Conta contaDestino = contaRepository.buscarPorId(transferencia.getContaDestinoId()).orElseThrow(ContaNaoEncontradaException::new);

        boolean usuarioParticipaDaTransferencia = contaOrigem.getUsuarioId().equals(usuario.getId())
                || contaDestino.getUsuarioId().equals(usuario.getId());

        if (!usuarioParticipaDaTransferencia) {
            throw new TransferenciaNaoEncontradaException();
        }

        return TransferenciaResult.from(transferencia);

    }
}
