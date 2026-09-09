package br.com.fourbank.fourbank.adapter.out.persistence.boundary.transferencia;

import br.com.fourbank.fourbank.adapter.out.persistence.data.transferencia.TransferenciaData;
import br.com.fourbank.fourbank.adapter.out.persistence.mapper.TransferenciaPersistenceMapper;
import br.com.fourbank.fourbank.adapter.out.persistence.repository.TransferenciaJpaRepository;
import br.com.fourbank.fourbank.application.model.transferencia.StatusTransferencia;
import br.com.fourbank.fourbank.application.model.transferencia.Transferencia;
import br.com.fourbank.fourbank.application.port.out.transferencia.TransferenciaRepositoryPort;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public class TransferenciaPersistenceImpl implements TransferenciaRepositoryPort {
    private final TransferenciaJpaRepository repository;

    public TransferenciaPersistenceImpl(TransferenciaJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Transferencia salvar(Transferencia transferencia) {
        TransferenciaData entity = repository.save(TransferenciaPersistenceMapper.toData(transferencia));

        return TransferenciaPersistenceMapper.toModel(entity);
    }

    @Override
    public Optional<Transferencia> buscarPorId(Long id) {
        return repository.findById(id).map(TransferenciaPersistenceMapper::toModel);
    }

    @Override
    public List<Transferencia> listarPorConta(Long contaId) {
        return repository
            .findByContaOrigemIdOrContaDestinoIdOrderBySolicitadaEmDesc(
                contaId,
                contaId
            )
            .stream()
            .map(TransferenciaPersistenceMapper::toModel)
            .toList();

    }

    @Override
    public List<Transferencia> listarAgendadasAte(Instant momento) {
        return repository.findByStatusAndAgendadaParaLessThanEqual(StatusTransferencia.AGENDADA, momento)
            .stream()
            .map(TransferenciaPersistenceMapper::toModel)
            .toList();
    }

    @Override
    public long contarConcluidasPorContaOrigemId(Long contaOrigemId) {
        return repository.countByContaOrigemIdAndStatus(
            contaOrigemId,
            StatusTransferencia.CONCLUIDA);
    }
}
