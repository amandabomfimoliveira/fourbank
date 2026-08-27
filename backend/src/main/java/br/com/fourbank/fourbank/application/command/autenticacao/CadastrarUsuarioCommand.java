package br.com.fourbank.fourbank.application.command.autenticacao;

import br.com.fourbank.fourbank.application.model.conta.TipoConta;
import br.com.fourbank.fourbank.application.model.usuario.TipoPessoa;

public record CadastrarUsuarioCommand(
        String nome,
        String documento,
        TipoPessoa tipoPessoa,
        String email,
        String senha,
        TipoConta tipoConta
) {
}
