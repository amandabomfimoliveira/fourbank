package br.com.fourbank.fourbank.application.exception;

public class ContaEncerradaException extends RuntimeException {
    public ContaEncerradaException() {
        super("A conta foi encerrada.");
    }
}
