package br.com.fourbank.fourbank.adapter.out.persistence.data.usuario;

import br.com.fourbank.fourbank.application.model.usuario.Role;
import br.com.fourbank.fourbank.application.model.usuario.TipoPessoa;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "usuarios")
public class UsuarioData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false, unique = true, length = 14)
    private String documento;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_pessoa", nullable = false, length = 20)
    private TipoPessoa tipoPessoa;

    @Column(nullable = false, unique = true, length = 160)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(name = "perfil", nullable = false, length = 20)
    private Role role;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private Instant criadoEm;

    protected UsuarioData() {
    }

    public UsuarioData(
            Long id,
            String nome,
            String documento,
            TipoPessoa tipoPessoa,
            String email,
            String senha,
            Role role,
            Instant criadoEm
    ) {
        this.id = id;
        this.nome = nome;
        this.documento = documento;
        this.tipoPessoa = tipoPessoa;
        this.email = email;
        this.senha = senha;
        this.role = role;
        this.criadoEm = criadoEm;
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

    public String getSenha() {
        return senha;
    }

    public Role getRole() {
        return role;
    }

    public Instant getCriadoEm() {
        return criadoEm;
    }
}
