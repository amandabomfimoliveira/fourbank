package br.com.fourbank.fourbank.application.model.conta;

import br.com.fourbank.fourbank.application.exception.ContaBloqueadaException;
import br.com.fourbank.fourbank.application.exception.ContaEncerradaException;
import br.com.fourbank.fourbank.application.exception.ContaNaoBloqueadaException;
import br.com.fourbank.fourbank.application.exception.SaldoNaoZeradoException;

import java.math.BigDecimal;
import java.util.Objects;

public final class Conta {
    private final Long id;
    private final Long usuarioId;
    private final String numero;
    private final String agencia;
    private final TipoConta tipo;
    private final BigDecimal saldo;
    private StatusConta status;


    private Conta(
            Long id,
            Long usuarioId,
            String numero,
            String agencia,
            TipoConta tipo,
            BigDecimal saldo,
            StatusConta status
    ) {
        this.id = id;
        this.usuarioId = Objects.requireNonNull(usuarioId, "O ID do usuário é obrigatório");
        this.numero = Objects.requireNonNull(numero, "O número da conta é obrigatório");
        this.agencia = Objects.requireNonNull(agencia, "O número da agência é obrigatório");
        this.tipo = Objects.requireNonNull(tipo, "O tipo da conta é obrigatório");
        this.saldo = Objects.requireNonNull(saldo, "O saldo da conta é obrigatório");
        this.status = Objects.requireNonNull(status, "O status da conta é obrigatório");
    }

    public static Conta nova(
            Long usuarioId,
            String numero,
            String agencia,
            TipoConta tipo
    ) {
        return new Conta(
                null,
                usuarioId,
                numero,
                agencia,
                tipo,
                BigDecimal.ZERO,
                StatusConta.ATIVA
        );
    }

    public static Conta restaurar(
            Long id,
            Long usuarioId,
            String numero,
            String agencia,
            TipoConta tipo,
            BigDecimal saldo,
            StatusConta status

    ) {
        Objects.requireNonNull(
                id,
                "O ID da conta restaurada é obrigatório"
        );
        return new Conta(id, usuarioId, numero, agencia, tipo, saldo, status);
    }

    public void bloquear(){
        if (status == StatusConta.ENCERRADA) {
            throw new ContaEncerradaException();
        }

        if (status == StatusConta.BLOQUEADA) {
            throw new ContaBloqueadaException();
        }

        status = StatusConta.BLOQUEADA;
    }

    public void desbloquear(){
        if (status == StatusConta.ENCERRADA) {
            throw new ContaEncerradaException();
        }

        if (status == StatusConta.ATIVA) {
           throw new ContaNaoBloqueadaException();
        }

        status = StatusConta.ATIVA;
    }

    public void encerrar(){
        if (status==StatusConta.ENCERRADA){
            throw new ContaEncerradaException();
        }
        if (status==StatusConta.BLOQUEADA){
            throw new ContaBloqueadaException();
        }
        if (saldo.compareTo(BigDecimal.ZERO) != 0) {
            throw new SaldoNaoZeradoException();
        }
        status = StatusConta.ENCERRADA;
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
