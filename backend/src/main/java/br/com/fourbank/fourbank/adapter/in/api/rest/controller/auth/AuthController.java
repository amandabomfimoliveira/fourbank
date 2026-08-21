package br.com.fourbank.fourbank.adapter.in.api.rest.controller.auth;

import br.com.fourbank.fourbank.adapter.in.api.rest.dto.auth.AutenticacaoDto;
import br.com.fourbank.fourbank.adapter.in.api.rest.dto.auth.CadastrarUsuarioDto;
import br.com.fourbank.fourbank.adapter.in.api.rest.dto.auth.LoginDto;
import br.com.fourbank.fourbank.adapter.in.api.rest.mapper.auth.AutenticacaoMapper;
import br.com.fourbank.fourbank.application.port.in.auth.CadastrarUsuarioUseCase;
import br.com.fourbank.fourbank.application.port.in.auth.LoginUseCase;
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

    private final CadastrarUsuarioUseCase cadastrarUsuarioUseCase;
    private final LoginUseCase loginUseCase;

    public AuthController(
            CadastrarUsuarioUseCase cadastrarUsuarioUseCase,
            LoginUseCase loginUseCase
    ) {
        this.cadastrarUsuarioUseCase = cadastrarUsuarioUseCase;
        this.loginUseCase = loginUseCase;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AutenticacaoDto cadastrar(@Valid @RequestBody CadastrarUsuarioDto dto) {
        var command = AutenticacaoMapper.toCommand(dto);
        var result = cadastrarUsuarioUseCase.cadastrar(command);
        return AutenticacaoMapper.toDto(result);
    }

    @PostMapping("/login")
    public AutenticacaoDto login(@Valid @RequestBody LoginDto dto) {
        var command = AutenticacaoMapper.toCommand(dto);
        var result = loginUseCase.login(command);
        return AutenticacaoMapper.toDto(result);
    }
}
