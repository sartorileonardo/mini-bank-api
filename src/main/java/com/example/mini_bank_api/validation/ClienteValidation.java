package com.example.mini_bank_api.validation;

import com.example.mini_bank_api.exception.ContaException;
import com.example.mini_bank_api.exception.SaldoInsuficienteException;
import com.example.mini_bank_api.exception.ValorInvalidoException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ClienteValidation {

    // Validação de valor positivo
    public void validarValorPositivo(BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValorInvalidoException("Valor deve ser positivo");
        }
    }

    public void validarSaldoSuficiente(BigDecimal saldo, BigDecimal valor) {
        if (saldo.compareTo(valor) < 0) {
            throw new SaldoInsuficienteException("Saldo insuficiente");
        }
    }

    public void validarNumeroContaUnico(boolean numeroDaContaJaExiste) {
        if(numeroDaContaJaExiste){
            throw new ContaException("Número da conta já existe");
        }
    }
}
