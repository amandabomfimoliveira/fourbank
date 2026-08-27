CREATE TABLE contas (
    id BIGINT NOT NULL AUTO_INCREMENT,
    usuario_id BIGINT NOT NULL,
    numero VARCHAR(8) NOT NULL,
    agencia VARCHAR(4) NOT NULL,
    tipo VARCHAR(20) NOT NULL,
    saldo DECIMAL(19, 2) NOT NULL DEFAULT 0.00,
    status VARCHAR(20) NOT NULL DEFAULT 'ATIVA',
    CONSTRAINT pk_contas PRIMARY KEY (id),
    CONSTRAINT fk_contas_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios (id),
    CONSTRAINT uk_contas_agencia_numero UNIQUE (agencia, numero),
    CONSTRAINT uk_contas_usuario_tipo UNIQUE (usuario_id, tipo),
    CONSTRAINT ck_contas_tipo CHECK (tipo IN ('CORRENTE', 'POUPANCA')),
    CONSTRAINT ck_contas_status CHECK (status IN ('ATIVA', 'BLOQUEADA', 'ENCERRADA')),
    CONSTRAINT ck_contas_saldo_nao_negativo CHECK (saldo >= 0)
);
