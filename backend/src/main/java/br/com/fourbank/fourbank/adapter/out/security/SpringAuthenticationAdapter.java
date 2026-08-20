package br.com.fourbank.fourbank.adapter.out.security;

import br.com.fourbank.fourbank.application.port.out.auth.AutenticadorPort;
import br.com.fourbank.fourbank.domain.model.Usuario;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class SpringAuthenticationAdapter implements AutenticadorPort {

    private final AuthenticationManager authenticationManager;

    public SpringAuthenticationAdapter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Usuario autenticar(String email, String senhaPura) {
        var authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(email, senhaPura)
        );
        return ((SpringUserPrincipal) authentication.getPrincipal()).getUsuario();
    }
}
