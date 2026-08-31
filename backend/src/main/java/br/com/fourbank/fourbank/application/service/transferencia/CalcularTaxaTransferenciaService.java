package br.com.fourbank.fourbank.application.service.transferencia;

import br.com.fourbank.fourbank.application.model.conta.TipoConta;

import java.math.BigDecimal;

public class CalcularTaxaTransferenciaService {
    private static final BigDecimal TAXA_CONTA_CORRENTE = new BigDecimal("2.50");
    private static final BigDecimal TAXA_CONTA_POUPANCA = new BigDecimal("3.50");

    private static final int GRATUITAS_CONTA_CORRENTE = 5;
    private static final int GRATUITAS_CONTA_POUPANCA = 2;

    public BigDecimal calcular(TipoConta tipoConta, long quantidadeTransferencias){
        if (tipoConta == TipoConta.CORRENTE){
            if (quantidadeTransferencias>=GRATUITAS_CONTA_CORRENTE){
                return TAXA_CONTA_CORRENTE;
            }
            return BigDecimal.ZERO;
        }
        if (quantidadeTransferencias>=GRATUITAS_CONTA_POUPANCA){
            return TAXA_CONTA_POUPANCA;
        }
        return BigDecimal.ZERO;
    }
}
