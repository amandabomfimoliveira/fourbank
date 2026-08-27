package br.com.fourbank.fourbank.application.exception;

public class ContaNaoEncontradaException extends RuntimeException {
    public ContaNaoEncontradaException() {
        super("A Conta informada não foi encontrada");
    }
}
