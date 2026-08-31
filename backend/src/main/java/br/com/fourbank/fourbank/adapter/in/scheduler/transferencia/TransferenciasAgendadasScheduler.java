package br.com.fourbank.fourbank.adapter.in.scheduler.transferencia;

import br.com.fourbank.fourbank.application.port.in.transferencia.ProcessarTransferenciasAgendadasUseCase;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TransferenciasAgendadasScheduler {
    private final ProcessarTransferenciasAgendadasUseCase useCase;

    public TransferenciasAgendadasScheduler(
        ProcessarTransferenciasAgendadasUseCase useCase
    ) {
        this.useCase = useCase;
    }

    @Scheduled(fixedDelayString =
        "${fourbank.transferencias-agendadas.intervalo-ms:5000}")
    public void executar() {
        useCase.processarPendentes();
    }
}
