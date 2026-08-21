package br.com.fourbank.fourbank.adapter.out.security;

import br.com.fourbank.fourbank.application.port.out.auth.TokenProviderPort;
import br.com.fourbank.fourbank.application.model.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class JwtTokenAdapter implements TokenProviderPort {

    private static final String ISSUER = "fourbank";

    private final JwtEncoder jwtEncoder;
    private final long expirationSeconds;

    public JwtTokenAdapter(
            JwtEncoder jwtEncoder,
            @Value("${app.jwt.expiration-seconds}") long expirationSeconds
    ) {
        this.jwtEncoder = jwtEncoder;
        this.expirationSeconds = expirationSeconds;
    }

    @Override
    public String gerarToken(Usuario usuario) {
        Instant agora = Instant.now();
        var claims = JwtClaimsSet.builder()
                .issuer(ISSUER)
                .issuedAt(agora)
                .expiresAt(agora.plusSeconds(expirationSeconds))
                .subject(usuario.getEmail())
                .claim("roles", List.of(usuario.getRole().name()))
                .build();
        var header = JwsHeader.with(MacAlgorithm.HS256).type("JWT").build();
        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }

    @Override
    public long getExpirationSeconds() {
        return expirationSeconds;
    }
}
