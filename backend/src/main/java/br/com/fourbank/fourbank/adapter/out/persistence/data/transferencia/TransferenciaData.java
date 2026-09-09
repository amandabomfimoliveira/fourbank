package br.com.fourbank.fourbank.adapter.out.persistence.data.transferencia;

import br.com.fourbank.fourbank.application.model.transferencia.StatusTransferencia;
import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Entity
@Table(name = "transferencias")
public class TransferenciaData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "conta_origem_id", nullable = false)
    private Long contaOrigemId;

    @Column(name = "conta_destino_id", nullable = false)
    private Long contaDestinoId;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal valor;

    @Column(nullable = false, precision = 4, scale = 2)
    private BigDecimal taxa;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusTransferencia status;

    @Column(name = "solicitada_em", nullable = false, updatable = false)
    private Instant solicitadaEm;

    @Column(name = "agendada_para",  updatable = false)
    private Instant agendadaPara;

    @Column(name = "realizada_em")
    private Instant realizadaEm;

    public TransferenciaData() {
    }

    public TransferenciaData(
        Long id,
        Long contaOrigemId,
        Long contaDestinoId,
        BigDecimal valor,
        BigDecimal taxa,
        StatusTransferencia status,
        Instant solicitadaEm,
        Instant agendadaPara,
        Instant realizadaEm
    ) {
        this.id = id;
        this.contaOrigemId = contaOrigemId;
        this.contaDestinoId = contaDestinoId;
        this.valor = valor;
        this.taxa = taxa;
        this.status = status;
        this.solicitadaEm = solicitadaEm;
        this.agendadaPara = agendadaPara;
        this.realizadaEm = realizadaEm;
    }

}
