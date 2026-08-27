package br.com.fourbank.fourbank.application.model.usuario;

import java.time.Instant;
import java.util.Objects;

public final class Usuario {

    private final Long id;
    private final String nome;
    private final String documento;
    private final TipoPessoa tipoPessoa;
    private final String email;
    private final String senhaHash;
    private final Role role;
    private final Instant criadoEm;

    private Usuario(
            Long id,
            String nome,
            String documento,
            TipoPessoa tipoPessoa,
            String email,
            String senhaHash,
            Role role,
        Instant criadoEm
    ) {
        this.id = id;
        this.nome = Objects.requireNonNull(nome, "O nome é obrigatório");
        this.documento = Objects.requireNonNull(documento, "O documento é obrigatório");
        this.tipoPessoa = Objects.requireNonNull(tipoPessoa, "O tipo de pessoa é obrigatório");
        this.email = Objects.requireNonNull(email, "O e-mail é obrigatório");
        this.senhaHash = Objects.requireNonNull(senhaHash, "O hash da senha é obrigatório");
        this.role = Objects.requireNonNull(role, "O perfil é obrigatório");
        this.criadoEm = Objects.requireNonNull(criadoEm, "A data de criação é obrigatória");
    }

    public static Usuario novo(
            String nome,
            String documento,
            TipoPessoa tipoPessoa,
            String email,
            String senhaHash
    ) {
        return new Usuario(
                null,
                nome,
                documento,
                tipoPessoa,
                email,
                senhaHash,
                Role.USER,
                Instant.now()
        );
    }

    public static Usuario restaurar(
            Long id,
            String nome,
            String documento,
            TipoPessoa tipoPessoa,
            String email,
            String senhaHash,
            Role role,
            Instant criadoEm
    ) {
        Objects.requireNonNull(
                id,
                "O ID do usuário restaurado é obrigatório"
        );
        return new Usuario(id, nome, documento, tipoPessoa, email, senhaHash, role, criadoEm);
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDocumento() {
        return documento;
    }

    public TipoPessoa getTipoPessoa() {
        return tipoPessoa;
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
