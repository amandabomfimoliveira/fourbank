package br.com.fourbank.fourbank.application.service.conta;

import br.com.fourbank.fourbank.application.port.out.conta.ContaRepositoryPort;

import java.security.SecureRandom;
import java.util.Objects;

public class GerarNumeroContaService {
    private static final int MAXIMO_TENTATIVAS = 10;
    private static final SecureRandom RANDOM = new SecureRandom();

    private final ContaRepositoryPort contaRepositoryPort;

    public GerarNumeroContaService(ContaRepositoryPort contaRepositoryPort) {
        this.contaRepositoryPort = contaRepositoryPort;
    }

    public String gerar(String agencia) {
        Objects.requireNonNull(agencia, "A agência é obrigatória");

        for (int tentativa = 0; tentativa < MAXIMO_TENTATIVAS; tentativa++) {
            String numero = "%08d".formatted(RANDOM.nextInt(100_000_000));

            boolean numeroEmUso = contaRepositoryPort.existePorAgenciaENumero(agencia, numero);

            if (!numeroEmUso) {
                return numero;
            }
        }
        throw new IllegalStateException("Não foi possível gerar um número de conta");
    }
}
