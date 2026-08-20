package br.com.fourbank.fourbank.adapter.in.api.rest.dto.auth;

import br.com.fourbank.fourbank.application.result.auth.AuthResult;

public record AuthResponse(String token, String tipo, long expiraEmSegundos) {

    public static AuthResponse from(AuthResult result) {
        return new AuthResponse(result.token(), result.tipo(), result.expiraEmSegundos());
    }
}
