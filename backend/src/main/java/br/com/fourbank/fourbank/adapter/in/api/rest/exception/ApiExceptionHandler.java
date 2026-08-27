package br.com.fourbank.fourbank.adapter.in.api.rest.exception;

import br.com.fourbank.fourbank.application.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiError> handleValidation(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        Map<String, String> campos = new LinkedHashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error ->
                campos.putIfAbsent(error.getField(), error.getDefaultMessage()));
        return resposta(HttpStatus.BAD_REQUEST, "Os dados enviados são inválidos", request, campos);
    }

    @ExceptionHandler({
            EmailJaCadastradoException.class,
            DocumentoJaCadastradoException.class,
            DataIntegrityViolationException.class
    })
    ResponseEntity<ApiError> handleConflict(Exception exception, HttpServletRequest request) {
        String mensagem = exception instanceof EmailJaCadastradoException
                || exception instanceof DocumentoJaCadastradoException
                ? exception.getMessage()
                : "Já existe um registro com esses dados";
        return resposta(HttpStatus.CONFLICT, mensagem, request, Map.of());
    }

    @ExceptionHandler({
        UsuarioNaoEncontradoException.class,
        ContaNaoEncontradaException.class
    })
    ResponseEntity<ApiError> handleNotFound(
        RuntimeException exception,
        HttpServletRequest request
    ) {
        return resposta(
            HttpStatus.NOT_FOUND,
            exception.getMessage(),
            request,
            Map.of()
        );
    }

    @ExceptionHandler(AuthenticationException.class)
    ResponseEntity<ApiError> handleAuthentication(
            AuthenticationException exception,
            HttpServletRequest request
    ) {
        return resposta(HttpStatus.UNAUTHORIZED, "E-mail ou senha inválidos", request, Map.of());
    }

    @ExceptionHandler({
        ContaBloqueadaException.class,
        ContaEncerradaException.class,
        ContaNaoBloqueadaException.class,
        SaldoNaoZeradoException.class,
        UsuarioJaPossuiContaDoTipoException.class
    })
    ResponseEntity<ApiError> handleContaConflict(
        RuntimeException exception,
        HttpServletRequest request
    ) {
        return resposta(
            HttpStatus.CONFLICT,
            exception.getMessage(),
            request,
            Map.of()
        );
    }

    private ResponseEntity<ApiError> resposta(
            HttpStatus status,
            String mensagem,
            HttpServletRequest request,
            Map<String, String> campos
    ) {
        var body = new ApiError(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                mensagem,
                request.getRequestURI(),
                campos
        );
        return ResponseEntity.status(status).body(body);
    }
}
