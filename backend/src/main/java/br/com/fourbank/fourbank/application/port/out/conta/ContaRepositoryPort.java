package br.com.fourbank.fourbank.application.port.out.conta;

import br.com.fourbank.fourbank.application.model.conta.Conta;
import br.com.fourbank.fourbank.application.model.conta.TipoConta;

import java.util.List;
import java.util.Optional;

public interface ContaRepositoryPort {
    Conta salvar(Conta conta);

    Optional<Conta> buscarPorId(Long id);

    Optional<Conta> buscarPorNumero(String numero);

    List<Conta> listarPorUsuario(Long usuarioId);

    Optional<Conta> buscarNaoEncerradaPorUsuarioIdETipo(Long usuarioId, TipoConta tipo);

    Optional<Conta> buscarPorAgenciaENumero(String agencia, String numero);


    boolean existeNaoEncerradaPorUsuarioIdETipo(Long usuarioId, TipoConta tipo);

    boolean existePorAgenciaENumero(String agencia, String numero);
}
