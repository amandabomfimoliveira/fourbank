package br.com.fourbank.fourbank.adapter.out.security;

import br.com.fourbank.fourbank.application.port.out.auth.CodificadorSenhaPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BCryptPasswordAdapter implements CodificadorSenhaPort {

    private final PasswordEncoder passwordEncoder;

    public BCryptPasswordAdapter(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String codificar(String senhaPura) {
        return passwordEncoder.encode(senhaPura);
    }
}
