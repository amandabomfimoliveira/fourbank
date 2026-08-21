package br.com.fourbank.fourbank.adapter.in.api.rest.dto.auth;

public record AutenticacaoDto(String token, String tipo, long expiraEmSegundos) {
}
