package br.com.fourbank.fourbank.application.validator.usuario;

import br.com.fourbank.fourbank.application.model.usuario.TipoPessoa;

import java.util.Objects;

public final class DocumentoValidator {

    private DocumentoValidator() {
    }

    public static String validar(String documento, TipoPessoa tipoPessoa) {
        String documentoValidado = TextoObrigatorioValidator.validar(documento, "documento");
        Objects.requireNonNull(tipoPessoa, "O tipo de pessoa é obrigatório");

        int tamanhoEsperado = tipoPessoa == TipoPessoa.FISICA ? 11 : 14;
        if (!documentoValidado.matches("\\d{" + tamanhoEsperado + "}")) {
            throw new IllegalArgumentException(
                    "O documento deve possuir " + tamanhoEsperado + " dígitos para pessoa "
                            + tipoPessoa.name().toLowerCase()
            );
        }
        return documentoValidado;
    }
}
