package br.com.fourbank.fourbank.application.usecase.transferencia;

import br.com.fourbank.fourbank.application.exception.ContaBloqueadaException;
import br.com.fourbank.fourbank.application.exception.ContaEncerradaException;
import br.com.fourbank.fourbank.application.exception.ContaNaoEncontradaException;
import br.com.fourbank.fourbank.application.exception.SaldoInsuficienteException;
import br.com.fourbank.fourbank.application.model.conta.Conta;
import br.com.fourbank.fourbank.application.model.transferencia.Transferencia;
import br.com.fourbank.fourbank.application.port.in.transferencia.ProcessarTransferenciasAgendadasUseCase;
import br.com.fourbank.fourbank.application.port.out.conta.ContaRepositoryPort;
import br.com.fourbank.fourbank.application.port.out.transferencia.TransferenciaRepositoryPort;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class ProcessarTransferenciasAgendadasUseCaseImpl implements ProcessarTransferenciasAgendadasUseCase {
    private final TransferenciaRepositoryPort transferenciaRepository;
    private final ContaRepositoryPort contaRepository;

    public ProcessarTransferenciasAgendadasUseCaseImpl(TransferenciaRepositoryPort transferenciaRepository, ContaRepositoryPort contaRepository) {
        this.transferenciaRepository = transferenciaRepository;
        this.contaRepository = contaRepository;
    }

    @Override
    @Transactional
    public void processarPendentes() {
        List<Transferencia> transferencias = transferenciaRepository.listarAgendadasAte(Instant.now());

        for (Transferencia transferencia : transferencias){
           processar(transferencia);
        }

    }

    private void processar(Transferencia transferencia){
        transferencia.iniciarProcessamento();

        try{
            Conta contaOrigem = contaRepository.buscarPorId(transferencia.getContaOrigemId()).orElseThrow(ContaNaoEncontradaException::new);

            Conta contaDestino = contaRepository.buscarPorId(transferencia.getContaDestinoId()).orElseThrow(ContaNaoEncontradaException::new);

            BigDecimal valorTotal = transferencia.getValor()
                .add(transferencia.getTaxa());

            contaOrigem.debitar(valorTotal);
            contaDestino.creditar(transferencia.getValor());

            contaRepository.salvar(contaOrigem);
            contaRepository.salvar(contaDestino);

            transferencia.concluir();
        }catch (SaldoInsuficienteException
                | ContaBloqueadaException
                | ContaEncerradaException
                | ContaNaoEncontradaException exception){
            transferencia.falhar();
        }
        transferenciaRepository.salvar(transferencia);
    }
}
