package br.com.fourbank.fourbank.adapter.out.persistence.mapper;

import br.com.fourbank.fourbank.adapter.out.persistence.data.transferencia.TransferenciaData;
import br.com.fourbank.fourbank.application.model.transferencia.Transferencia;

public class TransferenciaPersistenceMapper {
    public TransferenciaPersistenceMapper() {
    }

    public static TransferenciaData toData(Transferencia transferencia){
        return new TransferenciaData(
            transferencia.getId(),
            transferencia.getContaOrigemId(),
            transferencia.getContaDestinoId(),
            transferencia.getValor(),
            transferencia.getTaxa(),
            transferencia.getStatus(),
            transferencia.getSolicitadaEm(),
            transferencia.getAgendadaPara(),
            transferencia.getRealizadaEm()
        );
    }

    public static Transferencia toModel(TransferenciaData entity){
        return Transferencia.restaurar(
            entity.getId(),
            entity.getContaOrigemId(),
            entity.getContaDestinoId(),
            entity.getValor(),
            entity.getTaxa(),
            entity.getStatus(),
            entity.getSolicitadaEm(),
            entity.getAgendadaPara(),
            entity.getRealizadaEm()

        );
    }
}
