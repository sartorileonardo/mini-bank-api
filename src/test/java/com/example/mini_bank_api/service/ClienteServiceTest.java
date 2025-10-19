package com.example.mini_bank_api.service;

import com.example.mini_bank_api.entity.Cliente;
import com.example.mini_bank_api.exception.ClienteNotFoundException;
import com.example.mini_bank_api.exception.ContaException;
import com.example.mini_bank_api.exception.SaldoInsuficienteException;
import com.example.mini_bank_api.exception.ValorInvalidoException;
import com.example.mini_bank_api.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
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
    void deveCadastrarClienteComSucesso() {
        // Arrange
        Cliente cliente = new Cliente(123L, "João", "12345", "001", new BigDecimal("100.00"));

        when(clienteRepository.existsByNumeroConta("12345"))
                .thenReturn(false);
        when(clienteRepository.save(any(Cliente.class)))
                .thenReturn(cliente);

        // Act
        Cliente resultado = clienteService.cadastrarCliente(cliente);

        // Assert
        assertNotNull(resultado);
        assertEquals("João", resultado.getNome());
        assertEquals("12345", resultado.getNumeroConta());
        verify(clienteRepository).existsByNumeroConta("12345");
        verify(clienteRepository).save(cliente);
    }

    @Test
    void deveLancarExcecaoQuandoNumeroContaJaExiste() {
        // Arrange
        Cliente cliente = new Cliente(123L, "João", "12345", "001", new BigDecimal("100.00"));

        when(clienteRepository.existsByNumeroConta("12345"))
                .thenReturn(true);

        // Act & Assert
        assertThrows(ContaException.class,
                () -> clienteService.cadastrarCliente(cliente));
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void deveBuscarTodosClientes() {
        // Arrange
        Cliente cliente1 = new Cliente(123L, "João", "12345", "001", new BigDecimal("100.00"));
        Cliente cliente2 = new Cliente(321L, "Maria", "67890", "001", new BigDecimal("50.00"));
        List<Cliente> clientes = Arrays.asList(cliente1, cliente2);

        when(clienteRepository.findAll())
                .thenReturn(clientes);

        // Act
        List<Cliente> resultado = clienteService.buscarTodos();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(clienteRepository).findAll();
    }

    @Test
    void deveBuscarClientePorId() {
        // Arrange
        Cliente cliente = new Cliente(123L, "João", "12345", "001", new BigDecimal("100.00"));

        when(clienteRepository.findById(123L))
                .thenReturn(Optional.of(cliente));

        // Act
        Cliente resultado = clienteService.buscarPorId(123L);

        // Assert
        assertNotNull(resultado);
        assertEquals("João", resultado.getNome());
        assertEquals("12345", resultado.getNumeroConta());
        verify(clienteRepository).findById(123L);
    }

    @Test
    void deveLancarExcecaoQuandoBuscarPorIdNaoEncontrado() {
        // Arrange
        when(clienteRepository.findById(999L))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ClienteNotFoundException.class,
                () -> clienteService.buscarPorId(999L));
    }

    @Test
    void deveBuscarClientePorNumeroConta() {
        // Arrange
        Cliente cliente = new Cliente(123L, "João", "12345", "001", new BigDecimal("100.00"));

        when(clienteRepository.findByNumeroConta("12345"))
                .thenReturn(Optional.of(cliente));

        // Act
        Cliente resultado = clienteService.buscarPorNumeroConta("12345");

        // Assert
        assertNotNull(resultado);
        assertEquals("João", resultado.getNome());
        assertEquals("12345", resultado.getNumeroConta());
        verify(clienteRepository).findByNumeroConta("12345");
    }

    @Test
    void deveLancarExcecaoQuandoBuscarPorNumeroContaNaoEncontrado() {
        // Arrange
        when(clienteRepository.findByNumeroConta("99999"))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ClienteNotFoundException.class,
                () -> clienteService.buscarPorNumeroConta("99999"));
    }

    @Test
    void deveDepositarComSucesso() {
        // Arrange
        Cliente cliente = new Cliente(123L, "João", "12345", "001", new BigDecimal("100.00"));

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
    void deveLancarExcecaoQuandoDepositarValorInvalido() {
        // Act & Assert
        assertThrows(ValorInvalidoException.class,
                () -> clienteService.depositar("12345", new BigDecimal("0.00")));

        assertThrows(ValorInvalidoException.class,
                () -> clienteService.depositar("12345", new BigDecimal("-10.00")));
    }

    @Test
    void deveSacarComSucesso() {
        // Arrange
        Cliente cliente = new Cliente(123L, "João", "12345", "001", new BigDecimal("100.00"));

        when(clienteRepository.findByNumeroConta("12345"))
                .thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class)))
                .thenReturn(cliente);

        // Act
        Cliente resultado = clienteService.sacar("12345", new BigDecimal("30.00"));

        // Assert
        assertEquals(new BigDecimal("70.00"), resultado.getSaldo());
        verify(clienteRepository).save(cliente);
    }

    @Test
    void deveLancarExcecaoQuandoSaldoInsuficiente() {
        // Arrange
        Cliente cliente = new Cliente(321L, "Maria", "67890", "001", new BigDecimal("50.00"));

        when(clienteRepository.findByNumeroConta("67890"))
                .thenReturn(Optional.of(cliente));

        // Act & Assert
        assertThrows(SaldoInsuficienteException.class,
                () -> clienteService.sacar("67890", new BigDecimal("100.00")));
    }

    @Test
    void deveLancarExcecaoQuandoSacarValorInvalido() {
        // Act & Assert
        assertThrows(ValorInvalidoException.class,
                () -> clienteService.sacar("12345", new BigDecimal("0.00")));

        assertThrows(ValorInvalidoException.class,
                () -> clienteService.sacar("12345", new BigDecimal("-10.00")));
    }

    @Test
    void deveTransferirComSucesso() {
        // Arrange
        Cliente origem = new Cliente(123L, "João", "12345", "001", new BigDecimal("100.00"));
        Cliente destino = new Cliente(321L, "Maria", "67890", "001", new BigDecimal("50.00"));

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

    @Test
    void deveLancarExcecaoQuandoTransferirValorInvalido() {
        // Act & Assert
        assertThrows(ValorInvalidoException.class,
                () -> clienteService.transferir("12345", "67890", new BigDecimal("0.00")));

        assertThrows(ValorInvalidoException.class,
                () -> clienteService.transferir("12345", "67890", new BigDecimal("-10.00")));
    }
}