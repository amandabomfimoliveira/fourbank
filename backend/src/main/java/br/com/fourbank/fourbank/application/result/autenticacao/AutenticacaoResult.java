package br.com.fourbank.fourbank.application.result.autenticacao;

public record AutenticacaoResult(String token, String tipo, long expiraEmSegundos) {
}
