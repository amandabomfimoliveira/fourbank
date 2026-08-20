package br.com.fourbank.fourbank.adapter.in.api.rest.controller.user;

import br.com.fourbank.fourbank.adapter.in.api.rest.dto.user.UsuarioResponse;
import br.com.fourbank.fourbank.application.port.in.user.ConsultarUsuarioUseCase;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UsuarioController {

    private final ConsultarUsuarioUseCase consultarUsuarioUseCase;

    public UsuarioController(ConsultarUsuarioUseCase consultarUsuarioUseCase) {
        this.consultarUsuarioUseCase = consultarUsuarioUseCase;
    }

    @GetMapping("/me")
    public UsuarioResponse usuarioAtual(@AuthenticationPrincipal Jwt jwt) {
        return UsuarioResponse.from(consultarUsuarioUseCase.consultarPorEmail(jwt.getSubject()));
    }
}
