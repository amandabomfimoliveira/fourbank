ALTER TABLE usuarios
ADD COLUMN documento VARCHAR(14) NOT NULL;

ALTER TABLE usuarios
ADD COLUMN tipo_pessoa VARCHAR(20) NOT NULL;

ALTER TABLE usuarios
ADD CONSTRAINT uk_usuarios_documento UNIQUE (documento);
