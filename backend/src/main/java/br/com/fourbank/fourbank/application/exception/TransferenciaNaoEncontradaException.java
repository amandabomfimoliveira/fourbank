package br.com.fourbank.fourbank.application.exception;

public class TransferenciaNaoEncontradaException extends RuntimeException {
    public TransferenciaNaoEncontradaException() {
        super("Transferência não encontrada");
    }
}
