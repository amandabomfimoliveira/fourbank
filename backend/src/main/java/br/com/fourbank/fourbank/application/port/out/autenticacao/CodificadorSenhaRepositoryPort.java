package br.com.fourbank.fourbank.application.port.out.autenticacao;

public interface CodificadorSenhaRepositoryPort {

    String codificar(String senhaPura);
}
