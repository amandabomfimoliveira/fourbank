package br.com.fourbank.fourbank.adapter.in.api.rest.dto.autenticacao;

public record AutenticacaoDto(String token, String tipo, long expiraEmSegundos) {
}
