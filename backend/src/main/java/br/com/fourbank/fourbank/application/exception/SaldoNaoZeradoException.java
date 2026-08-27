package br.com.fourbank.fourbank.application.exception;

public class SaldoNaoZeradoException extends RuntimeException {
    public SaldoNaoZeradoException() {
        super("A conta ainda possui saldo.");
    }
}
