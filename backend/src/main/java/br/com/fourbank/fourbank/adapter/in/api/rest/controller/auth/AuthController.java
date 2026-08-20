package br.com.fourbank.fourbank.adapter.in.api.rest.controller.auth;

import br.com.fourbank.fourbank.adapter.in.api.rest.dto.auth.AuthResponse;
import br.com.fourbank.fourbank.adapter.in.api.rest.dto.auth.CadastroRequest;
import br.com.fourbank.fourbank.adapter.in.api.rest.dto.auth.LoginRequest;
import br.com.fourbank.fourbank.application.port.in.auth.AutenticacaoUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AutenticacaoUseCase autenticacaoUseCase;

    public AuthController(AutenticacaoUseCase autenticacaoUseCase) {
        this.autenticacaoUseCase = autenticacaoUseCase;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse cadastrar(@Valid @RequestBody CadastroRequest request) {
        return AuthResponse.from(autenticacaoUseCase.cadastrar(request.toCommand()));
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return AuthResponse.from(autenticacaoUseCase.login(request.toCommand()));
    }
}
