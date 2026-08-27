package br.com.fourbank.fourbank.application.exception;

public class DocumentoJaCadastradoException extends RuntimeException {

    public DocumentoJaCadastradoException() {
        super("O documento informado já está cadastrado");
    }
}
