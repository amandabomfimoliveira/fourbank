package br.com.fourbank.fourbank.adapter.in.api.rest.dto.usuario;

public record UsuarioDto(
        Long id,
        String nome,
        String documento,
        String tipoPessoa,
        String email,
        String perfil
) {
}
