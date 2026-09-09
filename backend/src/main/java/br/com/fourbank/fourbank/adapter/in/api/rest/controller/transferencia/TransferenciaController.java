package br.com.fourbank.fourbank.adapter.in.api.rest.controller.transferencia;

import br.com.fourbank.fourbank.adapter.in.api.rest.dto.conta.TipoContaDto;
import br.com.fourbank.fourbank.adapter.in.api.rest.dto.transferencia.AgendarTransferenciaDto;
import br.com.fourbank.fourbank.adapter.in.api.rest.dto.transferencia.RealizarTransferenciaDto;
import br.com.fourbank.fourbank.adapter.in.api.rest.dto.transferencia.TransferenciaDto;
import br.com.fourbank.fourbank.adapter.in.api.rest.mapper.transferencia.TransferenciaMapper;
import br.com.fourbank.fourbank.application.command.transferencia.AgendarTransferenciaCommand;
import br.com.fourbank.fourbank.application.command.transferencia.ConsultarTransferenciaCommand;
import br.com.fourbank.fourbank.application.command.transferencia.ListarTransferenciasDaContaCommand;
import br.com.fourbank.fourbank.application.command.transferencia.RealizarTransferenciaCommand;
import br.com.fourbank.fourbank.application.model.conta.TipoConta;
import br.com.fourbank.fourbank.application.port.in.transferencia.AgendarTransferenciaUseCase;
import br.com.fourbank.fourbank.application.port.in.transferencia.ConsultarTransferenciaUseCase;
import br.com.fourbank.fourbank.application.port.in.transferencia.ListarTransferenciasDaContaUseCase;
import br.com.fourbank.fourbank.application.port.in.transferencia.RealizarTransferenciaUseCase;
import br.com.fourbank.fourbank.application.result.transferencia.TransferenciaResult;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transferencias")
public class TransferenciaController {
    private final RealizarTransferenciaUseCase realizarTransferenciaUseCase;

    private final AgendarTransferenciaUseCase agendarTransferenciaUseCase;

    private final ConsultarTransferenciaUseCase consultarTransferenciaUseCase;

    private final ListarTransferenciasDaContaUseCase listarTransferenciasDaContaUseCase;

    public TransferenciaController(RealizarTransferenciaUseCase realizarTransferenciaUseCase, AgendarTransferenciaUseCase agendarTransferenciaUseCase, ConsultarTransferenciaUseCase consultarTransferenciaUseCase, ListarTransferenciasDaContaUseCase listarTransferenciasDaContaUseCase) {
        this.realizarTransferenciaUseCase = realizarTransferenciaUseCase;
        this.agendarTransferenciaUseCase = agendarTransferenciaUseCase;
        this.consultarTransferenciaUseCase = consultarTransferenciaUseCase;
        this.listarTransferenciasDaContaUseCase = listarTransferenciasDaContaUseCase;
    }

    @PostMapping("/realizar")
    @ResponseStatus(HttpStatus.CREATED)
    public TransferenciaDto realizarTransferencia(
        @RequestBody RealizarTransferenciaDto dto,
        @AuthenticationPrincipal Jwt jwt
    ) {
        RealizarTransferenciaCommand command = TransferenciaMapper.toRealizarTransferenciaCommand(jwt.getSubject(), dto);

        TransferenciaResult result = realizarTransferenciaUseCase.realizar(command);

        return TransferenciaMapper.toDto(result);
    }

    @PostMapping("/agendar")
    @ResponseStatus(HttpStatus.CREATED)
    public TransferenciaDto agendarTransferencia(
        @RequestBody AgendarTransferenciaDto dto,
        @AuthenticationPrincipal Jwt jwt
    ) {
        AgendarTransferenciaCommand command = TransferenciaMapper.toAgendarTransferenciaCommand(jwt.getSubject(), dto);

        TransferenciaResult result = agendarTransferenciaUseCase.agendar(command);

        return TransferenciaMapper.toDto(result);
    }

    @GetMapping("/consultar-transferencias/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TransferenciaDto consultarTransferencias(
        @PathVariable Long id,
        @AuthenticationPrincipal Jwt jwt
    ){
        ConsultarTransferenciaCommand command = TransferenciaMapper.toConsultarTransferenciaCommand(jwt.getSubject(),id);

        TransferenciaResult result = consultarTransferenciaUseCase.consultar(command);

        return TransferenciaMapper.toDto(result);
    }

    @GetMapping("/listar-transferencias/{tipoConta}")
    @ResponseStatus(HttpStatus.OK)
    public List<TransferenciaDto> listarTransferencias(
        @AuthenticationPrincipal Jwt jwt,
        @PathVariable TipoConta tipoConta
    ){
        ListarTransferenciasDaContaCommand command = TransferenciaMapper.toListarTransferenciasDaContaCommand(jwt.getSubject(),tipoConta);

        List<TransferenciaResult> results = listarTransferenciasDaContaUseCase.listar(command);

        return results.stream()
            .map(TransferenciaMapper::toDto)
            .toList();
    }
}
