package br.com.fourbank.fourbank.application.port.in.transferencia;

import br.com.fourbank.fourbank.application.model.transferencia.Transferencia;
import br.com.fourbank.fourbank.application.result.transferencia.TransferenciaResult;

public interface ProcessarTransferenciasAgendadasUseCase {
    void processarPendentes();

}
