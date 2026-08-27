package br.com.fourbank.fourbank.adapter.out.persistence.data.conta;

import br.com.fourbank.fourbank.application.model.conta.StatusConta;
import br.com.fourbank.fourbank.application.model.conta.TipoConta;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "contas")
public class ContaData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(nullable = false, length = 8)
    private  String numero;

    @Column(nullable = false, length = 4)
    private String agencia;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoConta tipo;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal saldo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusConta status;

    public ContaData() {
    }

    public ContaData(
            Long id,
            Long usuarioId,
            String numero,
            String agencia,
            TipoConta tipo,
            BigDecimal saldo,
            StatusConta status
    ) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.numero = numero;
        this.agencia = agencia;
        this.tipo = tipo;
        this.saldo = saldo;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public String getNumero() {
        return numero;
    }

    public String getAgencia() {
        return agencia;
    }

    public TipoConta getTipo() {
        return tipo;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public StatusConta getStatus() {
        return status;
    }
}
