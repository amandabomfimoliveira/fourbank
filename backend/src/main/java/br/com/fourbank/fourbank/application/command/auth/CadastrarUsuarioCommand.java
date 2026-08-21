package br.com.fourbank.fourbank.application.command.auth;

public record CadastrarUsuarioCommand(String nome, String email, String senha) {
}
