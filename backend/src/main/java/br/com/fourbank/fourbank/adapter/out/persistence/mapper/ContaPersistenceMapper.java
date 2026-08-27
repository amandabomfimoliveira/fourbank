package br.com.fourbank.fourbank.adapter.out.persistence.mapper;

import br.com.fourbank.fourbank.adapter.out.persistence.data.conta.ContaData;

import br.com.fourbank.fourbank.application.model.conta.Conta;


public class ContaPersistenceMapper {
    public ContaPersistenceMapper() {
    }

    public static ContaData toData(Conta conta){
        return new ContaData(
                conta.getId(),
                conta.getUsuarioId(),
                conta.getNumero(),
                conta.getAgencia(),
                conta.getTipo(),
                conta.getSaldo(),
                conta.getStatus()
        );
    }

    public static Conta toModel(ContaData entity){
        return Conta.restaurar(
                entity.getId(),
                entity.getUsuarioId(),
                entity.getNumero(),
                entity.getAgencia(),
                entity.getTipo(),
                entity.getSaldo(),
                entity.getStatus()
        );
    }
}
