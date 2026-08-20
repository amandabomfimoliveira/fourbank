package br.com.fourbank.fourbank.application.exception;

public class EmailJaCadastradoException extends RuntimeException {

    public EmailJaCadastradoException() {
        super("Já existe uma conta cadastrada com este e-mail");
    }
}
