package br.com.fourbank.fourbank.application.service.conta;

import br.com.fourbank.fourbank.application.exception.ContaNaoEncontradaException;
import br.com.fourbank.fourbank.application.model.conta.Conta;
import br.com.fourbank.fourbank.application.model.conta.TipoConta;
import br.com.fourbank.fourbank.application.model.usuario.Usuario;
import br.com.fourbank.fourbank.application.port.out.conta.ContaRepositoryPort;
import br.com.fourbank.fourbank.application.port.out.usuario.UsuarioRepositoryPort;

public class BuscarContaDoUsuarioService {
    private final ContaRepositoryPort contaRepository;

    private final UsuarioRepositoryPort usuarioRepository;

    public BuscarContaDoUsuarioService(ContaRepositoryPort contaRepository, UsuarioRepositoryPort usuarioRepository) {
        this.contaRepository = contaRepository;
        this.usuarioRepository = usuarioRepository;
    }


    public Conta buscar(String emailUsuario, TipoConta tipo){
        Usuario usuario = usuarioRepository.buscarPorEmail(emailUsuario).orElseThrow(ContaNaoEncontradaException::new);

        return contaRepository
                .buscarNaoEncerradaPorUsuarioIdETipo(usuario.getId(), tipo)
                .orElseThrow(ContaNaoEncontradaException::new);
    }
}
