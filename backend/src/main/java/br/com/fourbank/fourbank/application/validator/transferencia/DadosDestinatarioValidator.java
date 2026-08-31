package br.com.fourbank.fourbank.application.validator.transferencia;

import br.com.fourbank.fourbank.application.exception.DadosDestinatarioInvalidosException;
import br.com.fourbank.fourbank.application.model.conta.Conta;
import br.com.fourbank.fourbank.application.model.conta.TipoConta;
import br.com.fourbank.fourbank.application.model.usuario.Usuario;

import java.util.Objects;

public class DadosDestinatarioValidator {
    public void validar(
        Conta contaDestino,
        Usuario destinatario,
        String nomeInformado,
        String documentoInformado,
        TipoConta tipoContaInformado
    ) {
        Objects.requireNonNull(
            contaDestino,
            "A conta de destino é obrigatória"
        );
        Objects.requireNonNull(
            destinatario,
            "O destinatário é obrigatório"
        );

        boolean nomeConfere = normalizarTexto(destinatario.getNome())
            .equals(normalizarTexto(nomeInformado));

        boolean documentoConfere = normalizarDocumento(
            destinatario.getDocumento()
        ).equals(normalizarDocumento(documentoInformado));


        boolean tipoContaConfere = contaDestino.getTipo()
            == tipoContaInformado;

        if (!nomeConfere
            || !documentoConfere
            || !tipoContaConfere) {
            throw new DadosDestinatarioInvalidosException();
        }
    }

    private String normalizarTexto(String texto) {
        if (texto == null || texto.isBlank()) {
            return "";
        }

        return texto.trim().toUpperCase();
    }

    private String normalizarDocumento(String documento) {
        if (documento == null || documento.isBlank()) {
            return "";
        }

        return documento.replaceAll("\\D", "");
    }
}
