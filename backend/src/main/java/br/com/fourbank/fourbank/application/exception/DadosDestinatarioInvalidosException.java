package br.com.fourbank.fourbank.application.exception;

public class DadosDestinatarioInvalidosException extends RuntimeException {
    public DadosDestinatarioInvalidosException() {
        super("Os dados informados não correspondem à conta de destino.");
    }
}
