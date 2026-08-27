package br.com.fourbank.fourbank.adapter.out.persistence.repository;

import br.com.fourbank.fourbank.adapter.out.persistence.data.usuario.UsuarioData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioJpaRepository extends JpaRepository<UsuarioData, Long> {

    Optional<UsuarioData> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByDocumento(String documento);
}
