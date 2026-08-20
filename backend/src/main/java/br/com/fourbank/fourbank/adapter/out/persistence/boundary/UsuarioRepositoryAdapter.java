package br.com.fourbank.fourbank.adapter.out.persistence.boundary;

import br.com.fourbank.fourbank.adapter.out.persistence.data.UsuarioJpaEntity;
import br.com.fourbank.fourbank.adapter.out.persistence.repository.SpringDataUsuarioRepository;
import br.com.fourbank.fourbank.application.port.out.user.UsuarioRepositoryPort;
import br.com.fourbank.fourbank.domain.model.Usuario;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UsuarioRepositoryAdapter implements UsuarioRepositoryPort {

    private final SpringDataUsuarioRepository repository;

    public UsuarioRepositoryAdapter(SpringDataUsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return repository.findByEmailIgnoreCase(email).map(this::toDomain);
    }

    @Override
    public boolean existePorEmail(String email) {
        return repository.existsByEmailIgnoreCase(email);
    }

    @Override
    public Usuario salvar(Usuario usuario) {
        UsuarioJpaEntity entity = repository.save(toEntity(usuario));
        return toDomain(entity);
    }

    private UsuarioJpaEntity toEntity(Usuario usuario) {
        return new UsuarioJpaEntity(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getSenhaHash(),
                usuario.getRole(),
                usuario.getCriadoEm()
        );
    }

    private Usuario toDomain(UsuarioJpaEntity entity) {
        return Usuario.restaurar(
                entity.getId(),
                entity.getNome(),
                entity.getEmail(),
                entity.getSenha(),
                entity.getRole(),
                entity.getCriadoEm()
        );
    }
}
