package br.com.fourbank.fourbank.application.exception;

import br.com.fourbank.fourbank.application.model.conta.TipoConta;

public class UsuarioJaPossuiContaDoTipoException extends RuntimeException {
    public UsuarioJaPossuiContaDoTipoException(TipoConta tipo) {
        super("O usuário já possui uma conta do tipo " + tipo);
    }
}
