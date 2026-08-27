package br.com.fourbank.fourbank.application.usecase.conta;

import br.com.fourbank.fourbank.application.command.conta.CriarContaCommand;
import br.com.fourbank.fourbank.application.exception.UsuarioNaoEncontradoException;
import br.com.fourbank.fourbank.application.model.conta.Conta;
import br.com.fourbank.fourbank.application.model.conta.ContaConstants;
import br.com.fourbank.fourbank.application.model.usuario.Usuario;
import br.com.fourbank.fourbank.application.port.in.conta.CriarContaUseCase;
import br.com.fourbank.fourbank.application.port.out.conta.ContaRepositoryPort;
import br.com.fourbank.fourbank.application.port.out.usuario.UsuarioRepositoryPort;
import br.com.fourbank.fourbank.application.result.conta.ContaResult;
import br.com.fourbank.fourbank.application.service.conta.GerarNumeroContaService;
import br.com.fourbank.fourbank.application.validator.conta.TipoContaDisponivelValidator;

import java.util.Objects;

public class CriarContaUseCaseImpl implements CriarContaUseCase {

    private final ContaRepositoryPort contaRepository;

    private final UsuarioRepositoryPort usuarioRepository;

    private final GerarNumeroContaService gerarNumeroContaService;

    private final TipoContaDisponivelValidator tipoContaDisponivelValidator;

    public CriarContaUseCaseImpl(
            ContaRepositoryPort contaRepository,
            UsuarioRepositoryPort usuarioRepository,
            GerarNumeroContaService gerarNumeroContaService,
            TipoContaDisponivelValidator tipoContaDisponivelValidator
    ) {
        this.contaRepository = contaRepository;
        this.usuarioRepository = usuarioRepository;
        this.gerarNumeroContaService = gerarNumeroContaService;
        this.tipoContaDisponivelValidator = tipoContaDisponivelValidator;
    }

    @Override
    public ContaResult criar(CriarContaCommand command) {
        Objects.requireNonNull(command, "O comando de criação de conta é obrigatório");
        Objects.requireNonNull(command.tipoConta(), "O tipo da conta é obrigatório");

        Usuario usuario = usuarioRepository
                .buscarPorEmail(command.emailUsuario())
                .orElseThrow(UsuarioNaoEncontradoException::new);

        tipoContaDisponivelValidator.validar(usuario.getId(), command.tipoConta());

        String agencia = ContaConstants.AGENCIA_PADRAO;
        String numero = gerarNumeroContaService.gerar(agencia);

        Conta conta = Conta.nova(
                usuario.getId(),
                numero,
                agencia,
                command.tipoConta()
        );

        Conta contaSalva = contaRepository.salvar(conta);

        return ContaResult.from(contaSalva);
    }
}
