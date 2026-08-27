package br.com.fourbank.fourbank.application.exception;

public class ContaBloqueadaException extends RuntimeException {
    public ContaBloqueadaException() {
        super("A conta está bloqueada");
    }
}
