package br.com.fourbank.fourbank.application.result.auth;

public record AuthResult(String token, String tipo, long expiraEmSegundos) {
}
