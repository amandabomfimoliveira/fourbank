package br.com.fourbank.fourbank.application.exception;

public class ContaNaoBloqueadaException extends RuntimeException {
    public ContaNaoBloqueadaException() {
        super("A conta não está bloqueada");
    }
}
