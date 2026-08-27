package br.com.fourbank.fourbank.adapter.in.api.rest.controller.autenticacao;

import br.com.fourbank.fourbank.adapter.in.api.rest.dto.autenticacao.AutenticacaoDto;
import br.com.fourbank.fourbank.adapter.in.api.rest.dto.autenticacao.CadastrarUsuarioDto;
import br.com.fourbank.fourbank.adapter.in.api.rest.dto.autenticacao.LoginDto;
import br.com.fourbank.fourbank.adapter.in.api.rest.mapper.autenticacao.AutenticacaoMapper;
import br.com.fourbank.fourbank.application.port.in.autenticacao.CadastrarUsuarioUseCase;
import br.com.fourbank.fourbank.application.port.in.autenticacao.LoginUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AutenticacaoController {

    private final CadastrarUsuarioUseCase cadastrarUsuarioUseCase;
    private final LoginUseCase loginUseCase;

    public AutenticacaoController(
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
