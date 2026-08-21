package br.com.fourbank.fourbank.application.service.auth;

import java.util.Locale;
import java.util.Objects;

public class NormalizarEmailService {

    public String normalizar(String email) {
        Objects.requireNonNull(email, "O e-mail é obrigatório");
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
