package br.com.fourbank.fourbank.application.port.in.autenticacao;

import br.com.fourbank.fourbank.application.command.autenticacao.CadastrarUsuarioCommand;
import br.com.fourbank.fourbank.application.result.autenticacao.AutenticacaoResult;

public interface CadastrarUsuarioUseCase {

    AutenticacaoResult cadastrar(CadastrarUsuarioCommand command);
}
