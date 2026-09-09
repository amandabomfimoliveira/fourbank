package br.com.fourbank.fourbank.application.port.out.transferencia;

import br.com.fourbank.fourbank.application.model.transferencia.Transferencia;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface TransferenciaRepositoryPort {
   Transferencia salvar(Transferencia transferencia);

   Optional<Transferencia> buscarPorId(Long id);

   List<Transferencia> listarPorConta(Long contaId);

    List<Transferencia> listarAgendadasAte(Instant momento);

    long contarConcluidasPorContaOrigemId(Long contaOrigemId);


}
