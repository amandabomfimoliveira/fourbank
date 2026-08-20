package br.com.fourbank.fourbank.domain.model;

import java.time.Instant;
import java.util.Objects;

public final class Usuario {

    private final Long id;
    private final String nome;
    private final String email;
    private final String senhaHash;
    private final Role role;
    private final Instant criadoEm;

    private Usuario(
            Long id,
            String nome,
            String email,
            String senhaHash,
            Role role,
            Instant criadoEm
    ) {
        this.id = id;
        this.nome = validarTexto(nome, "nome");
        this.email = validarTexto(email, "e-mail");
        this.senhaHash = validarTexto(senhaHash, "hash da senha");
        this.role = Objects.requireNonNull(role, "O perfil é obrigatório");
        this.criadoEm = Objects.requireNonNull(criadoEm, "A data de criação é obrigatória");
    }

    public static Usuario novo(String nome, String email, String senhaHash) {
        return new Usuario(null, nome, email, senhaHash, Role.USER, Instant.now());
    }

    public static Usuario restaurar(
            Long id,
            String nome,
            String email,
            String senhaHash,
            Role role,
            Instant criadoEm
    ) {
        return new Usuario(id, nome, email, senhaHash, role, criadoEm);
    }

    private static String validarTexto(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("O " + campo + " é obrigatório");
        }
        return valor;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    public Role getRole() {
        return role;
    }

    public Instant getCriadoEm() {
        return criadoEm;
    }
}
