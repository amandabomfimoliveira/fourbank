package br.com.fourbank.fourbank.adapter.out.persistence.repository;

import br.com.fourbank.fourbank.adapter.out.persistence.data.transferencia.TransferenciaData;
import br.com.fourbank.fourbank.application.model.transferencia.StatusTransferencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface TransferenciaJpaRepository extends JpaRepository<TransferenciaData, Long> {


    List<TransferenciaData>
    findByContaOrigemIdOrContaDestinoIdOrderBySolicitadaEmDesc(
        Long contaOrigemId,
        Long contaDestinoId
    );

    List<TransferenciaData>
    findByStatusAndAgendadaParaLessThanEqual(
        StatusTransferencia status,
        Instant momento
    );

    long countByContaOrigemIdAndStatus(
        Long contaOrigemId,
        StatusTransferencia status
    );


    }
