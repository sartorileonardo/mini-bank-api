package com.example.mini_bank_api.service;

import static org.junit.jupiter.api.Assertions.*;

import com.example.mini_bank_api.entity.Cliente;
import com.example.mini_bank_api.exception.SaldoInsuficienteException;
import com.example.mini_bank_api.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    @Test
    void deveDepositarComSucesso() {
        // Arrange
        Cliente cliente = new Cliente("João", "12345", "001");
        cliente.setSaldo(new BigDecimal("100.00"));

        when(clienteRepository.findByNumeroConta("12345"))
                .thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class)))
                .thenReturn(cliente);

        // Act
        Cliente resultado = clienteService.depositar("12345", new BigDecimal("50.00"));

        // Assert
        assertEquals(new BigDecimal("150.00"), resultado.getSaldo());
        verify(clienteRepository).save(cliente);
    }

    @Test
    void deveLancarExcecaoQuandoSaldoInsuficiente() {
        // Arrange
        Cliente cliente = new Cliente("Maria", "67890", "001");
        cliente.setSaldo(new BigDecimal("50.00"));

        when(clienteRepository.findByNumeroConta("67890"))
                .thenReturn(Optional.of(cliente));

        // Act & Assert
        assertThrows(SaldoInsuficienteException.class,
                () -> clienteService.sacar("67890", new BigDecimal("100.00")));
    }

    @Test
    void deveTransferirComSucesso() {
        // Arrange
        Cliente origem = new Cliente("João", "12345", "001");
        origem.setSaldo(new BigDecimal("100.00"));

        Cliente destino = new Cliente("Maria", "67890", "001");
        destino.setSaldo(new BigDecimal("50.00"));

        when(clienteRepository.findByNumeroConta("12345"))
                .thenReturn(Optional.of(origem));
        when(clienteRepository.findByNumeroConta("67890"))
                .thenReturn(Optional.of(destino));
        when(clienteRepository.save(any(Cliente.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        clienteService.transferir("12345", "67890", new BigDecimal("30.00"));

        // Assert
        assertEquals(new BigDecimal("70.00"), origem.getSaldo());
        assertEquals(new BigDecimal("80.00"), destino.getSaldo());
    }
}