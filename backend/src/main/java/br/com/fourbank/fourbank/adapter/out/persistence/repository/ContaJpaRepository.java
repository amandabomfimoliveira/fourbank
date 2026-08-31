package br.com.fourbank.fourbank.adapter.out.persistence.repository;

import br.com.fourbank.fourbank.adapter.out.persistence.data.conta.ContaData;
import br.com.fourbank.fourbank.application.model.conta.StatusConta;
import br.com.fourbank.fourbank.application.model.conta.TipoConta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ContaJpaRepository extends JpaRepository<ContaData, Long> {
    Optional<ContaData> findByNumero(String numero);

    List<ContaData> findByUsuarioId(Long usuarioId);

    Optional<ContaData> findFirstByUsuarioIdAndTipoAndStatusNot(
            Long usuarioId,
            TipoConta tipo,
            StatusConta status
    );

    Optional<ContaData> findByAgenciaAndNumero(
        String agencia,
        String numero
    );

    boolean existsByUsuarioIdAndTipoAndStatusNot(Long usuarioId, TipoConta tipo, StatusConta status);

    boolean existsByAgenciaAndNumero(String agencia, String numero);
}
