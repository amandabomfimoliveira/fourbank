CREATE TABLE transferencias (
    id BIGINT NOT NULL AUTO_INCREMENT,
    conta_origem_id BIGINT NOT NULL,
    conta_destino_id BIGINT NOT NULL,
    valor DECIMAL(19, 2) NOT NULL,
    taxa DECIMAL(4, 2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    solicitada_em TIMESTAMP(6) NOT NULL,
    agendada_para TIMESTAMP(6) NULL,
    realizada_em TIMESTAMP(6) NULL,
    CONSTRAINT pk_transferencias PRIMARY KEY (id),
    CONSTRAINT fk_transferencias_conta_origem
        FOREIGN KEY (conta_origem_id) REFERENCES contas (id),
    CONSTRAINT fk_transferencias_conta_destino
        FOREIGN KEY (conta_destino_id) REFERENCES contas (id),
    CONSTRAINT ck_transferencias_contas_diferentes
        CHECK (conta_origem_id <> conta_destino_id),
    CONSTRAINT ck_transferencias_valor_positivo
        CHECK (valor > 0),
    CONSTRAINT ck_transferencias_taxa_nao_negativa
        CHECK (taxa >= 0),
    CONSTRAINT ck_transferencias_status
        CHECK (status IN ('AGENDADA', 'PROCESSANDO', 'CONCLUIDA', 'FALHA', 'CANCELADA')),
    CONSTRAINT ck_transferencias_agendada_com_data
        CHECK (status <> 'AGENDADA' OR (agendada_para IS NOT NULL AND realizada_em IS NULL)),
    CONSTRAINT ck_transferencias_concluida_com_data
        CHECK (status <> 'CONCLUIDA' OR realizada_em IS NOT NULL)
);

CREATE INDEX idx_transferencias_conta_origem_solicitada
    ON transferencias (conta_origem_id, solicitada_em);

CREATE INDEX idx_transferencias_conta_destino_solicitada
    ON transferencias (conta_destino_id, solicitada_em);

CREATE INDEX idx_transferencias_status_agendada_para
    ON transferencias (status, agendada_para);
