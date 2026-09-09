package br.com.fourbank.fourbank.application.model.transferencia;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

public final class Transferencia {
    private final Long id;
    private final Long contaOrigemId;
    private final Long contaDestinoId;
    private final BigDecimal valor;
    private final BigDecimal taxa;
    private StatusTransferencia status;
    private final Instant solicitadaEm;
    private final Instant agendadaPara;
    private Instant realizadaEm;

    private Transferencia(
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
        this.contaOrigemId = Objects.requireNonNull(
                contaOrigemId,
                "O ID da conta de origem é obrigatório"
        );
        this.contaDestinoId = Objects.requireNonNull(
                contaDestinoId,
                "O ID da conta de destino é obrigatório"
        );
        this.valor = validarValor(valor);
        this.taxa = validarTaxa(taxa);
        this.status = Objects.requireNonNull(status, "O status é obrigatório");
        this.solicitadaEm = Objects.requireNonNull(
                solicitadaEm,
                "A data de solicitação é obrigatória"
        );
        this.agendadaPara = agendadaPara;
        this.realizadaEm = realizadaEm;

        validarContasDiferentes(contaOrigemId, contaDestinoId);
        validarDatasDoStatus(status, agendadaPara, realizadaEm);
    }

    public static Transferencia novaImediata(
            Long contaOrigemId,
            Long contaDestinoId,
            BigDecimal valor,
            BigDecimal taxa
    ) {
        return new Transferencia(
                null,
                contaOrigemId,
                contaDestinoId,
                valor,
                taxa,
                StatusTransferencia.PROCESSANDO,
                Instant.now(),
                null,
                null
        );
    }

    public static Transferencia novaAgendada(
            Long contaOrigemId,
            Long contaDestinoId,
            BigDecimal valor,
            BigDecimal taxa,
            Instant agendadaPara
    ) {
        Instant solicitadaEm = Instant.now();
        Objects.requireNonNull(
                agendadaPara,
                "A data do agendamento é obrigatória"
        );

        if (!agendadaPara.isAfter(solicitadaEm)) {
            throw new IllegalArgumentException(
                    "A data do agendamento deve estar no futuro"
            );
        }

        return new Transferencia(
                null,
                contaOrigemId,
                contaDestinoId,
                valor,
                taxa,
                StatusTransferencia.AGENDADA,
                solicitadaEm,
                agendadaPara,
                null
        );
    }

    public static Transferencia restaurar(
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
        Objects.requireNonNull(
                id,
                "O ID da transferência restaurada é obrigatório"
        );
        return new Transferencia(
                id,
                contaOrigemId,
                contaDestinoId,
                valor,
                taxa,
                status,
                solicitadaEm,
                agendadaPara,
                realizadaEm
        );
    }

    public void iniciarProcessamento() {
        if (status != StatusTransferencia.AGENDADA) {
            throw new IllegalStateException(
                    "Somente uma transferência agendada pode iniciar o processamento"
            );
        }
        status = StatusTransferencia.PROCESSANDO;
    }

    public void concluir() {
        if (status != StatusTransferencia.PROCESSANDO) {
            throw new IllegalStateException(
                    "Somente uma transferência em processamento pode ser concluída"
            );
        }
        status = StatusTransferencia.CONCLUIDA;
        realizadaEm = Instant.now();
    }

    public void falhar() {
        if (status != StatusTransferencia.PROCESSANDO) {
            throw new IllegalStateException(
                    "Somente uma transferência em processamento pode falhar"
            );
        }
        status = StatusTransferencia.FALHA;
    }

    public void cancelar() {
        if (status != StatusTransferencia.AGENDADA) {
            throw new IllegalStateException(
                    "Somente uma transferência agendada pode ser cancelada"
            );
        }
        status = StatusTransferencia.CANCELADA;
    }

    private static BigDecimal validarValor(BigDecimal valor) {
        Objects.requireNonNull(valor, "O valor é obrigatório");
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "O valor da transferência deve ser maior que zero"
            );
        }
        return valor;
    }

    private static BigDecimal validarTaxa(BigDecimal taxa) {
        Objects.requireNonNull(taxa, "A taxa é obrigatória");
        if (taxa.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "A taxa da transferência não pode ser negativa"
            );
        }
        return taxa;
    }

    private static void validarContasDiferentes(
            Long contaOrigemId,
            Long contaDestinoId
    ) {
        if (contaOrigemId.equals(contaDestinoId)) {
            throw new IllegalArgumentException(
                    "A conta de origem deve ser diferente da conta de destino"
            );
        }
    }

    private static void validarDatasDoStatus(
            StatusTransferencia status,
            Instant agendadaPara,
            Instant realizadaEm
    ) {
        if (status == StatusTransferencia.AGENDADA && agendadaPara == null) {
            throw new IllegalArgumentException(
                    "Uma transferência agendada deve possuir a data do agendamento"
            );
        }
        if (status == StatusTransferencia.CONCLUIDA && realizadaEm == null) {
            throw new IllegalArgumentException(
                    "Uma transferência concluída deve possuir a data de realização"
            );
        }
    }

    public Long getId() {
        return id;
    }

    public Long getContaOrigemId() {
        return contaOrigemId;
    }

    public Long getContaDestinoId() {
        return contaDestinoId;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public BigDecimal getTaxa() {
        return taxa;
    }

    public StatusTransferencia getStatus() {
        return status;
    }

    public Instant getSolicitadaEm() {
        return solicitadaEm;
    }

    public Instant getAgendadaPara() {
        return agendadaPara;
    }

    public Instant getRealizadaEm() {
        return realizadaEm;
    }
}
