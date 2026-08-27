package br.com.fourbank.fourbank.application.validator.usuario;

public final class TextoObrigatorioValidator {

    private TextoObrigatorioValidator() {
    }

    public static String validar(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("O " + campo + " é obrigatório");
        }
        return valor;
    }
}
