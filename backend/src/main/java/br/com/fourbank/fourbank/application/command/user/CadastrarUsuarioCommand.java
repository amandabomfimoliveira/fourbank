package br.com.fourbank.fourbank.application.command.user;

public record CadastrarUsuarioCommand(String nome, String email, String senha) {
}
