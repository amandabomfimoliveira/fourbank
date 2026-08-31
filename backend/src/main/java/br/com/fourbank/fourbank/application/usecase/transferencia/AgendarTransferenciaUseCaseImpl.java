package br.com.fourbank.fourbank.application.usecase.transferencia;

import br.com.fourbank.fourbank.application.command.transferencia.AgendarTransferenciaCommand;
import br.com.fourbank.fourbank.application.exception.ContaNaoEncontradaException;
import br.com.fourbank.fourbank.application.exception.UsuarioNaoEncontradoException;
import br.com.fourbank.fourbank.application.model.conta.Conta;
import br.com.fourbank.fourbank.application.model.transferencia.Transferencia;
import br.com.fourbank.fourbank.application.model.usuario.Usuario;
import br.com.fourbank.fourbank.application.port.in.transferencia.AgendarTransferenciaUseCase;
import br.com.fourbank.fourbank.application.port.out.conta.ContaRepositoryPort;
import br.com.fourbank.fourbank.application.port.out.transferencia.TransferenciaRepositoryPort;
import br.com.fourbank.fourbank.application.port.out.usuario.UsuarioRepositoryPort;
import br.com.fourbank.fourbank.application.result.transferencia.TransferenciaResult;
import br.com.fourbank.fourbank.application.service.transferencia.CalcularTaxaTransferenciaService;
import br.com.fourbank.fourbank.application.validator.transferencia.DadosDestinatarioValidator;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.Objects;

public class AgendarTransferenciaUseCaseImpl implements AgendarTransferenciaUseCase {
    private final TransferenciaRepositoryPort transferenciaRepository;

    private final UsuarioRepositoryPort usuarioRepository;

    private final ContaRepositoryPort contaRepository;

    private final CalcularTaxaTransferenciaService calcularTaxaTransferenciaService;

    private final DadosDestinatarioValidator dadosDestinatarioValidator;

    public AgendarTransferenciaUseCaseImpl(TransferenciaRepositoryPort transferenciaRepository, UsuarioRepositoryPort usuarioRepository, ContaRepositoryPort contaRepository, CalcularTaxaTransferenciaService calcularTaxaTransferenciaService, DadosDestinatarioValidator dadosDestinatarioValidator) {
        this.transferenciaRepository = transferenciaRepository;
        this.usuarioRepository = usuarioRepository;
        this.contaRepository = contaRepository;
        this.calcularTaxaTransferenciaService = calcularTaxaTransferenciaService;
        this.dadosDestinatarioValidator = dadosDestinatarioValidator;
    }

    @Override
    @Transactional
    public TransferenciaResult agendar(AgendarTransferenciaCommand command) {
        Objects.requireNonNull(command, "O comando de agendar transferência é obrigatório");

        Usuario usuario = usuarioRepository.buscarPorEmail(command.emailUsuario()).orElseThrow(UsuarioNaoEncontradoException::new);

        Conta contaOrigem = contaRepository.buscarNaoEncerradaPorUsuarioIdETipo(usuario.getId(), command.tipoContaOrigem()).orElseThrow(ContaNaoEncontradaException::new);

        Conta contaDestino = contaRepository.buscarPorAgenciaENumero(command.agenciaDestino(),command.numeroContaDestino()).orElseThrow(ContaNaoEncontradaException::new);

        Usuario destinatario = usuarioRepository
            .buscarPorId(contaDestino.getUsuarioId())
            .orElseThrow(UsuarioNaoEncontradoException::new);

        dadosDestinatarioValidator.validar(
            contaDestino,
            destinatario,
            command.nomeDestinatario(),
            command.documentoDestinatario(),
            command.tipoContaDestino()
        );

        long quantidade = transferenciaRepository.contarConcluidasPorContaOrigemId(contaOrigem.getId());

        BigDecimal taxa = calcularTaxaTransferenciaService.calcular(contaOrigem.getTipo(),quantidade);

        BigDecimal valorTotal = command.valor().add(taxa);

        Transferencia transferencia = Transferencia.novaAgendada(
            contaOrigem.getId(),
            contaDestino.getId(),
            command.valor(),
            taxa,
            command.agendadaPara()
        );


        Transferencia transferenciaSalva = transferenciaRepository.salvar(transferencia);

        return TransferenciaResult.from(transferenciaSalva);

    }
}
