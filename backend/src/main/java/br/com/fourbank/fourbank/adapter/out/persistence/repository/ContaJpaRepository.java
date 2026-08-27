package br.com.fourbank.fourbank.adapter.out.persistence.repository;

import br.com.fourbank.fourbank.adapter.out.persistence.data.conta.ContaData;
import br.com.fourbank.fourbank.application.model.conta.TipoConta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ContaJpaRepository extends JpaRepository<ContaData, Long> {
    Optional<ContaData> findByNumero(String numero);

    List<ContaData> findByUsuarioId(Long usuarioId);

    Optional<ContaData>findByUsuarioIdAndTipo(Long usuarioId, TipoConta tipo);

    boolean existsByUsuarioIdAndTipo(Long usuarioId, TipoConta tipo);

    boolean existsByAgenciaAndNumero(String agencia, String numero);
}
