package br.com.fourbank.fourbank.adapter.in.api.rest.controller.conta;

import br.com.fourbank.fourbank.adapter.in.api.rest.dto.conta.ContaDto;
import br.com.fourbank.fourbank.adapter.in.api.rest.dto.conta.TipoContaDto;
import br.com.fourbank.fourbank.adapter.in.api.rest.mapper.conta.ContaMapper;
import br.com.fourbank.fourbank.application.command.conta.*;
import br.com.fourbank.fourbank.application.port.in.conta.*;
import br.com.fourbank.fourbank.application.result.conta.ContaResult;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contas")
public class ContaController {

    private final CriarContaUseCase criarContaUseCase;

    private final BloquearContaUseCase bloquearContaUseCase;

    private final DesbloquearContaUseCase desbloquearContaUseCase;

    private final EncerrarContaUseCase encerrarContaUseCase;

    private final ConsultarContaUseCase consultarContaUseCase;

    private final ListarContasDoUsuarioUseCase listarContasDoUsuarioUseCase;

    public ContaController(CriarContaUseCase criarContaUseCase, BloquearContaUseCase bloquearContaUseCase, DesbloquearContaUseCase desbloquearContaUseCase, EncerrarContaUseCase encerrarContaUseCase, ConsultarContaUseCase consultarContaUseCase, ListarContasDoUsuarioUseCase listarContasDoUsuarioUseCase) {
        this.criarContaUseCase = criarContaUseCase;
        this.bloquearContaUseCase = bloquearContaUseCase;
        this.desbloquearContaUseCase = desbloquearContaUseCase;
        this.encerrarContaUseCase = encerrarContaUseCase;
        this.consultarContaUseCase = consultarContaUseCase;
        this.listarContasDoUsuarioUseCase = listarContasDoUsuarioUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ContaDto criarConta(
        @Valid @RequestBody TipoContaDto dto,
        @AuthenticationPrincipal Jwt jwt
    ) {
        CriarContaCommand command = ContaMapper.toCriarContaCommand(dto, jwt.getSubject());

        ContaResult result = criarContaUseCase.criar(command);

        return ContaMapper.toDto(result);
    }

    @PatchMapping("/bloquear")
    @ResponseStatus(HttpStatus.OK)
    public ContaDto bloquearConta(
        @Valid @RequestBody TipoContaDto dto,
        @AuthenticationPrincipal Jwt jwt
    ) {
        BloquearContaCommand command = ContaMapper.toBloquearContaCommand(dto, jwt.getSubject());
        ContaResult result = bloquearContaUseCase.bloquear(command);

        return ContaMapper.toDto(result);
    }

    @PatchMapping("/desbloquear")
    @ResponseStatus(HttpStatus.OK)
    public ContaDto desbloquearConta(
        @Valid @RequestBody TipoContaDto dto,
        @AuthenticationPrincipal Jwt jwt
    ) {
        DesbloquearContaCommand command = ContaMapper.toDesbloquearContaCommand(dto, jwt.getSubject());

        ContaResult result = desbloquearContaUseCase.desbloquear(command);

        return ContaMapper.toDto(result);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void encerrarConta(
        @Valid @RequestBody TipoContaDto dto,
        @AuthenticationPrincipal Jwt jwt
    ) {
        EncerrarContaCommand command = ContaMapper.toEncerrarContaCommand(dto, jwt.getSubject());

        encerrarContaUseCase.encerrar(command);
    }

    @GetMapping("/{numero}")
    @ResponseStatus(HttpStatus.OK)
    public ContaDto consultarConta(
        @PathVariable String numero,
        @AuthenticationPrincipal Jwt jwt
    ) {
        ConsultarContaCommand command = ContaMapper.toConsultarContaCommand(numero, jwt.getSubject());

        ContaResult result = consultarContaUseCase.consultar(command);

        return ContaMapper.toDto(result);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ContaDto> listarConta(
        @AuthenticationPrincipal Jwt jwt
    ) {
        ListarContasDoUsuarioCommand command = ContaMapper.toListarContasDoUsuarioCommand(jwt.getSubject());

        List<ContaResult> results = listarContasDoUsuarioUseCase.listar(command);

        return results.stream()
            .map(ContaMapper::toDto)
            .toList();
    }
}
