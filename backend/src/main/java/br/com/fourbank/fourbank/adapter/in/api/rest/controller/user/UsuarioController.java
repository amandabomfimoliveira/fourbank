package br.com.fourbank.fourbank.adapter.in.api.rest.controller.user;

import br.com.fourbank.fourbank.adapter.in.api.rest.dto.user.UsuarioDto;
import br.com.fourbank.fourbank.adapter.in.api.rest.mapper.user.UsuarioMapper;
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
    public UsuarioDto usuarioAtual(@AuthenticationPrincipal Jwt jwt) {
        var result = consultarUsuarioUseCase.consultarPorEmail(jwt.getSubject());
        return UsuarioMapper.toDto(result);
    }
}
