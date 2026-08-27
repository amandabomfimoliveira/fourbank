package br.com.fourbank.fourbank.adapter.out.persistence.boundary.usuario;

import br.com.fourbank.fourbank.adapter.out.persistence.mapper.UsuarioPersistenceMapper;
import br.com.fourbank.fourbank.adapter.out.persistence.repository.UsuarioJpaRepository;
import br.com.fourbank.fourbank.application.port.out.usuario.UsuarioRepositoryPort;
import br.com.fourbank.fourbank.application.model.usuario.Usuario;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UsuarioPersistenceImpl implements UsuarioRepositoryPort {

    private final UsuarioJpaRepository repository;

    public UsuarioPersistenceImpl(UsuarioJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        return repository.findById(id)
                .map(UsuarioPersistenceMapper::toModel);
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return repository.findByEmailIgnoreCase(email)
                .map(UsuarioPersistenceMapper::toModel);
    }

    @Override
    public boolean existePorEmail(String email) {
        return repository.existsByEmailIgnoreCase(email);
    }

    @Override
    public boolean existePorDocumento(String documento) {
        return repository.existsByDocumento(documento);
    }

    @Override
    public Usuario salvar(Usuario usuario) {
        var entity = repository.save(UsuarioPersistenceMapper.toData(usuario));
        return UsuarioPersistenceMapper.toModel(entity);
    }
}
