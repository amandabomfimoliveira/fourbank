CREATE INDEX idx_contas_usuario_id
    ON contas (usuario_id);

ALTER TABLE contas
    DROP INDEX uk_contas_usuario_tipo;

ALTER TABLE contas
    ADD COLUMN tipo_conta_em_aberto VARCHAR(20)
        GENERATED ALWAYS AS (
            CASE
                WHEN status <> 'ENCERRADA' THEN tipo
                ELSE NULL
            END
        ) STORED,
    ADD CONSTRAINT uk_contas_usuario_tipo_em_aberto
        UNIQUE (usuario_id, tipo_conta_em_aberto);
