package br.com.fourbank.fourbank.infrastructure.config;

import br.com.fourbank.fourbank.application.port.in.autenticacao.CadastrarUsuarioUseCase;
import br.com.fourbank.fourbank.application.port.in.autenticacao.LoginUseCase;
import br.com.fourbank.fourbank.application.port.in.conta.*;
import br.com.fourbank.fourbank.application.port.in.transferencia.AgendarTransferenciaUseCase;
import br.com.fourbank.fourbank.application.port.in.transferencia.ProcessarTransferenciasAgendadasUseCase;
import br.com.fourbank.fourbank.application.port.in.transferencia.RealizarTransferenciaUseCase;
import br.com.fourbank.fourbank.application.port.in.usuario.ConsultarUsuarioUseCase;
import br.com.fourbank.fourbank.application.port.out.autenticacao.AutenticadorRepositoryPort;
import br.com.fourbank.fourbank.application.port.out.autenticacao.CodificadorSenhaRepositoryPort;
import br.com.fourbank.fourbank.application.port.out.autenticacao.TokenProviderRepositoryPort;
import br.com.fourbank.fourbank.application.port.out.conta.ContaRepositoryPort;
import br.com.fourbank.fourbank.application.port.out.transferencia.TransferenciaRepositoryPort;
import br.com.fourbank.fourbank.application.port.out.usuario.UsuarioRepositoryPort;
import br.com.fourbank.fourbank.application.service.autenticacao.GerarResultadoAutenticacaoService;
import br.com.fourbank.fourbank.application.service.autenticacao.NormalizarEmailService;
import br.com.fourbank.fourbank.application.service.conta.BuscarContaDoUsuarioService;
import br.com.fourbank.fourbank.application.service.conta.GerarNumeroContaService;
import br.com.fourbank.fourbank.application.service.transferencia.CalcularTaxaTransferenciaService;
import br.com.fourbank.fourbank.application.usecase.autenticacao.CadastrarUsuarioUseCaseImpl;
import br.com.fourbank.fourbank.application.usecase.autenticacao.LoginUseCaseImpl;
import br.com.fourbank.fourbank.application.usecase.conta.*;
import br.com.fourbank.fourbank.application.usecase.transferencia.AgendarTransferenciaUseCaseImpl;
import br.com.fourbank.fourbank.application.usecase.transferencia.ProcessarTransferenciasAgendadasUseCaseImpl;
import br.com.fourbank.fourbank.application.usecase.transferencia.RealizarTransferenciaUseCaseImpl;
import br.com.fourbank.fourbank.application.usecase.usuario.ConsultarUsuarioUseCaseImpl;
import br.com.fourbank.fourbank.application.validator.autenticacao.DocumentoDisponivelValidator;
import br.com.fourbank.fourbank.application.validator.autenticacao.EmailDisponivelValidator;
import br.com.fourbank.fourbank.application.validator.conta.TipoContaDisponivelValidator;
import br.com.fourbank.fourbank.application.validator.transferencia.DadosDestinatarioValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    NormalizarEmailService normalizarEmailService() {
        return new NormalizarEmailService();
    }

    @Bean
    GerarResultadoAutenticacaoService gerarResultadoAutenticacaoService(
            TokenProviderRepositoryPort tokenProvider
    ) {
        return new GerarResultadoAutenticacaoService(tokenProvider);
    }

    @Bean
    EmailDisponivelValidator emailDisponivelValidator(
            UsuarioRepositoryPort usuarioRepository
    ) {
        return new EmailDisponivelValidator(usuarioRepository);
    }

    @Bean
    DocumentoDisponivelValidator documentoDisponivelValidator(
            UsuarioRepositoryPort usuarioRepository
    ) {
        return new DocumentoDisponivelValidator(usuarioRepository);
    }

    @Bean
    GerarNumeroContaService gerarNumeroContaService(ContaRepositoryPort contaRepository) {
        return new GerarNumeroContaService(contaRepository);
    }

    @Bean
    BuscarContaDoUsuarioService buscarContaDoUsuarioService(
            ContaRepositoryPort contaRepository,
            UsuarioRepositoryPort usuarioRepository
    ) {
        return new BuscarContaDoUsuarioService(
                contaRepository,
                usuarioRepository
        );
    }

    @Bean
    TipoContaDisponivelValidator tipoContaDisponivelValidator(ContaRepositoryPort contaRepository) {
        return new TipoContaDisponivelValidator(contaRepository);
    }

    @Bean
    CadastrarUsuarioUseCase cadastrarUsuarioUseCase(
            UsuarioRepositoryPort usuarioRepository,
            CodificadorSenhaRepositoryPort codificadorSenha,
            NormalizarEmailService normalizarEmailService,
            EmailDisponivelValidator emailDisponivelValidator,
            DocumentoDisponivelValidator documentoDisponivelValidator,
            GerarResultadoAutenticacaoService gerarResultadoAutenticacaoService,
            GerarNumeroContaService gerarNumeroContaService,
            ContaRepositoryPort contaRepository
    ) {
        return new CadastrarUsuarioUseCaseImpl(
                usuarioRepository,
                codificadorSenha,
                normalizarEmailService,
                emailDisponivelValidator,
                documentoDisponivelValidator,
                gerarResultadoAutenticacaoService,
                gerarNumeroContaService,
                contaRepository
        );
    }

    @Bean
    CriarContaUseCase criarContaUseCase(
            ContaRepositoryPort contaRepository,
            UsuarioRepositoryPort usuarioRepository,
            GerarNumeroContaService gerarNumeroContaService,
            TipoContaDisponivelValidator tipoContaDisponivelValidator
    ) {
        return new CriarContaUseCaseImpl(
                contaRepository,
                usuarioRepository,
                gerarNumeroContaService,
                tipoContaDisponivelValidator
        );
    }

    @Bean
    LoginUseCase loginUseCase(
            AutenticadorRepositoryPort autenticador,
            NormalizarEmailService normalizarEmailService,
            GerarResultadoAutenticacaoService gerarResultadoAutenticacaoService
    ) {
        return new LoginUseCaseImpl(
                autenticador,
                normalizarEmailService,
                gerarResultadoAutenticacaoService
        );
    }

    @Bean
    ConsultarUsuarioUseCase consultarUsuarioUseCase(UsuarioRepositoryPort usuarioRepository) {
        return new ConsultarUsuarioUseCaseImpl(usuarioRepository);
    }

    @Bean
    ConsultarContaUseCase consultarContaUseCase(ContaRepositoryPort contaRepositoryPort, UsuarioRepositoryPort usuarioRepositoryPort) {
        return new ConsultarContaUseCaseImpl(contaRepositoryPort, usuarioRepositoryPort);
    }

    @Bean
    ListarContasDoUsuarioUseCase listarContasDoUsuarioUseCase(ContaRepositoryPort contaRepositoryPort, UsuarioRepositoryPort usuarioRepositoryPort) {
        return new ListarContasDoUsuarioUseCaseImpl(contaRepositoryPort, usuarioRepositoryPort);
    }

    @Bean
    BloquearContaUseCase bloquearContaUseCase(ContaRepositoryPort contaRepositoryPort, BuscarContaDoUsuarioService buscarContaDoUsuarioService) {
        return new BloquearContaUseCaseImpl(contaRepositoryPort, buscarContaDoUsuarioService);
    }

    @Bean
    DesbloquearContaUseCase desbloquearContaUseCase(ContaRepositoryPort contaRepositoryPort, BuscarContaDoUsuarioService buscarContaDoUsuarioService) {
        return new DesbloquearContaUseCaseImpl(contaRepositoryPort,buscarContaDoUsuarioService );
    }

    @Bean
    EncerrarContaUseCase encerrarContaUseCase(ContaRepositoryPort contaRepositoryPort, BuscarContaDoUsuarioService buscarContaDoUsuarioService) {
        return new EncerrarContaUseCaseImpl(contaRepositoryPort, buscarContaDoUsuarioService);
    }

    @Bean
    RealizarTransferenciaUseCase realizarTransferenciaUseCase(TransferenciaRepositoryPort transferenciaRepositoryPort, UsuarioRepositoryPort usuarioRepositoryPort, ContaRepositoryPort contaRepositoryPort, CalcularTaxaTransferenciaService calcularTaxaTransferenciaService, DadosDestinatarioValidator dadosDestinatarioValidator){
        return new RealizarTransferenciaUseCaseImpl(transferenciaRepositoryPort,usuarioRepositoryPort,contaRepositoryPort,calcularTaxaTransferenciaService,dadosDestinatarioValidator);
    }

    @Bean
    CalcularTaxaTransferenciaService calcularTaxaTransferenciaService() {
        return new CalcularTaxaTransferenciaService();
    }

    @Bean
    DadosDestinatarioValidator dadosDestinatarioValidator() {
        return new DadosDestinatarioValidator();
    }

    @Bean
    AgendarTransferenciaUseCase agendarTransferenciaUseCase(TransferenciaRepositoryPort transferenciaRepositoryPort, UsuarioRepositoryPort usuarioRepositoryPort, ContaRepositoryPort contaRepositoryPort, CalcularTaxaTransferenciaService calcularTaxaTransferenciaService, DadosDestinatarioValidator dadosDestinatarioValidator){
        return new AgendarTransferenciaUseCaseImpl(transferenciaRepositoryPort,usuarioRepositoryPort,contaRepositoryPort,calcularTaxaTransferenciaService,dadosDestinatarioValidator);
    }

    @Bean
    ProcessarTransferenciasAgendadasUseCase
    processarTransferenciasAgendadasUseCase(
        TransferenciaRepositoryPort transferenciaRepository,
        ContaRepositoryPort contaRepository
    ) {
        return new ProcessarTransferenciasAgendadasUseCaseImpl(
            transferenciaRepository,
            contaRepository
        );
    }


}
