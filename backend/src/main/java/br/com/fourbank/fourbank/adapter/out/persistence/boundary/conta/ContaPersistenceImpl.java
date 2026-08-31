package br.com.fourbank.fourbank.adapter.out.persistence.boundary.conta;

import br.com.fourbank.fourbank.adapter.out.persistence.data.conta.ContaData;
import br.com.fourbank.fourbank.adapter.out.persistence.mapper.ContaPersistenceMapper;
import br.com.fourbank.fourbank.adapter.out.persistence.repository.ContaJpaRepository;
import br.com.fourbank.fourbank.application.model.conta.Conta;
import br.com.fourbank.fourbank.application.model.conta.StatusConta;
import br.com.fourbank.fourbank.application.model.conta.TipoConta;
import br.com.fourbank.fourbank.application.port.out.conta.ContaRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ContaPersistenceImpl implements ContaRepositoryPort {

    private final ContaJpaRepository repository;

    public ContaPersistenceImpl(ContaJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Conta salvar(Conta conta) {
        ContaData entity = repository.save(ContaPersistenceMapper.toData(conta));
        return ContaPersistenceMapper.toModel(entity);
    }

    @Override
    public Optional<Conta> buscarPorId(Long id) {
        return repository.findById(id).map(ContaPersistenceMapper::toModel);
    }

    @Override
    public Optional<Conta> buscarPorNumero(String numero) {
        return repository.findByNumero(numero).map(ContaPersistenceMapper::toModel);
    }

    @Override
    public List<Conta> listarPorUsuario(Long usuarioId) {
        return repository.findByUsuarioId(usuarioId)
                .stream()
                .map(ContaPersistenceMapper::toModel)
                .toList();
    }

    @Override
    public Optional<Conta> buscarNaoEncerradaPorUsuarioIdETipo(Long usuarioId, TipoConta tipo) {
        return repository
                .findFirstByUsuarioIdAndTipoAndStatusNot(usuarioId, tipo, StatusConta.ENCERRADA)
                .map(ContaPersistenceMapper::toModel);
    }

    @Override
    public Optional<Conta> buscarPorAgenciaENumero(
        String agencia,
        String numero
    ) {
        return repository.findByAgenciaAndNumero(agencia, numero)
            .map(ContaPersistenceMapper::toModel);
    }

    @Override
    public boolean existeNaoEncerradaPorUsuarioIdETipo(Long usuarioId, TipoConta tipo) {
        return repository.existsByUsuarioIdAndTipoAndStatusNot(usuarioId, tipo, StatusConta.ENCERRADA);
    }

    @Override
    public boolean existePorAgenciaENumero(String agencia, String numero) {
        return repository.existsByAgenciaAndNumero(agencia,numero);
    }
}
