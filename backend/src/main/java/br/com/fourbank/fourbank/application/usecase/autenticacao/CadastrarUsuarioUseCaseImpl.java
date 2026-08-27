package br.com.fourbank.fourbank.application.usecase.autenticacao;

import br.com.fourbank.fourbank.application.command.autenticacao.CadastrarUsuarioCommand;
import br.com.fourbank.fourbank.application.model.conta.Conta;
import br.com.fourbank.fourbank.application.model.conta.ContaConstants;
import br.com.fourbank.fourbank.application.model.usuario.Usuario;
import br.com.fourbank.fourbank.application.port.in.autenticacao.CadastrarUsuarioUseCase;
import br.com.fourbank.fourbank.application.port.out.autenticacao.CodificadorSenhaPort;
import br.com.fourbank.fourbank.application.port.out.conta.ContaRepositoryPort;
import br.com.fourbank.fourbank.application.port.out.usuario.UsuarioRepositoryPort;
import br.com.fourbank.fourbank.application.result.autenticacao.AutenticacaoResult;
import br.com.fourbank.fourbank.application.service.autenticacao.GerarResultadoAutenticacaoService;
import br.com.fourbank.fourbank.application.service.autenticacao.NormalizarEmailService;
import br.com.fourbank.fourbank.application.service.conta.GerarNumeroContaService;
import br.com.fourbank.fourbank.application.validator.autenticacao.EmailDisponivelValidator;
import br.com.fourbank.fourbank.application.validator.autenticacao.DocumentoDisponivelValidator;
import br.com.fourbank.fourbank.application.validator.usuario.DocumentoValidator;
import br.com.fourbank.fourbank.application.validator.usuario.TextoObrigatorioValidator;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

public class CadastrarUsuarioUseCaseImpl implements CadastrarUsuarioUseCase {

    private final UsuarioRepositoryPort usuarioRepository;
    private final CodificadorSenhaPort codificadorSenha;
    private final NormalizarEmailService normalizarEmailService;
    private final EmailDisponivelValidator emailDisponivelValidator;
    private final DocumentoDisponivelValidator documentoDisponivelValidator;
    private final GerarResultadoAutenticacaoService gerarResultadoAutenticacaoService;
    private final GerarNumeroContaService gerarNumeroContaService;
    private final ContaRepositoryPort contaRepository;

    public CadastrarUsuarioUseCaseImpl(
            UsuarioRepositoryPort usuarioRepository,
            CodificadorSenhaPort codificadorSenha,
            NormalizarEmailService normalizarEmailService,
            EmailDisponivelValidator emailDisponivelValidator,
            DocumentoDisponivelValidator documentoDisponivelValidator,
            GerarResultadoAutenticacaoService gerarResultadoAutenticacaoService,
            GerarNumeroContaService gerarNumeroContaService,
            ContaRepositoryPort contaRepository
    ) {
        this.usuarioRepository = usuarioRepository;
        this.codificadorSenha = codificadorSenha;
        this.normalizarEmailService = normalizarEmailService;
        this.emailDisponivelValidator = emailDisponivelValidator;
        this.documentoDisponivelValidator = documentoDisponivelValidator;
        this.gerarResultadoAutenticacaoService = gerarResultadoAutenticacaoService;
        this.gerarNumeroContaService = gerarNumeroContaService;
        this.contaRepository = contaRepository;
    }

    @Override
    @Transactional
    public AutenticacaoResult cadastrar(CadastrarUsuarioCommand command) {
        Objects.requireNonNull(command, "O comando de cadastro é obrigatório");
        Objects.requireNonNull(command.tipoPessoa(), "O tipo de pessoa é obrigatório");
        Objects.requireNonNull(command.tipoConta(), "O tipo da conta é obrigatório");

        TextoObrigatorioValidator.validar(command.nome(), "nome");
        TextoObrigatorioValidator.validar(command.email(), "e-mail");
        TextoObrigatorioValidator.validar(command.senha(), "senha");
        DocumentoValidator.validar(command.documento(), command.tipoPessoa());

        String email = normalizarEmailService.normalizar(command.email());
        emailDisponivelValidator.validar(email);
        documentoDisponivelValidator.validar(command.documento());

        Usuario usuario = Usuario.novo(
                command.nome().trim(),
                command.documento(),
                command.tipoPessoa(),
                email,
                codificadorSenha.codificar(command.senha())
        );
        Usuario usuarioSalvo = usuarioRepository.salvar(usuario);

        String agencia = ContaConstants.AGENCIA_PADRAO;
        String numero = gerarNumeroContaService.gerar(agencia);

        Conta conta = Conta.nova(
                usuarioSalvo.getId(),
                numero,
                agencia,
                command.tipoConta()
        );
        contaRepository.salvar(conta);

        return gerarResultadoAutenticacaoService.gerar(usuarioSalvo);
    }
}
