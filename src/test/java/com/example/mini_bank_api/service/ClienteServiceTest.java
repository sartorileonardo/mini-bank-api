package com.example.mini_bank_api.service;

import com.example.mini_bank_api.entity.Cliente;
import com.example.mini_bank_api.exception.ClienteNotFoundException;
import com.example.mini_bank_api.exception.ContaException;
import com.example.mini_bank_api.exception.SaldoInsuficienteException;
import com.example.mini_bank_api.exception.ValorInvalidoException;
import com.example.mini_bank_api.repository.ClienteRepository;
import com.example.mini_bank_api.validation.ClienteValidation;
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

    @Mock
    private ClienteValidation clienteValidation;

    @InjectMocks
    private ClienteService clienteService;

    // ========== TESTES DE SUCESSO ==========

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
        verify(clienteValidation).validarNumeroContaUnico(false);
        verify(clienteRepository).existsByNumeroConta("12345");
        verify(clienteRepository).save(cliente);
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
        // Não deve interagir com validação para busca de todos
        verifyNoInteractions(clienteValidation);
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
        // Não deve interagir com validação para busca por ID
        verifyNoInteractions(clienteValidation);
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
        // Não deve interagir com validação para busca por número da conta
        verifyNoInteractions(clienteValidation);
    }

    @Test
    void deveDepositarComSucesso() {
        // Arrange
        Cliente cliente = new Cliente(123L, "João", "12345", "001", new BigDecimal("100.00"));
        BigDecimal valorDeposito = new BigDecimal("50.00");

        when(clienteRepository.findByNumeroConta("12345"))
                .thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class)))
                .thenReturn(cliente);

        // Act
        Cliente resultado = clienteService.depositar("12345", valorDeposito);

        // Assert
        assertEquals(new BigDecimal("150.00"), resultado.getSaldo());
        verify(clienteValidation).validarValorPositivo(valorDeposito);
        verify(clienteRepository).findByNumeroConta("12345");
        verify(clienteRepository).save(cliente);
    }

    @Test
    void deveSacarComSucesso() {
        // Arrange
        Cliente cliente = new Cliente(123L, "João", "12345", "001", new BigDecimal("100.00"));
        BigDecimal valorSaque = new BigDecimal("30.00");

        when(clienteRepository.findByNumeroConta("12345"))
                .thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class)))
                .thenReturn(cliente);

        // Act
        Cliente resultado = clienteService.sacar("12345", valorSaque);

        // Assert
        assertEquals(new BigDecimal("70.00"), resultado.getSaldo());
        verify(clienteValidation).validarValorPositivo(valorSaque);
        verify(clienteValidation).validarSaldoSuficiente(any(BigDecimal.class), any(BigDecimal.class));
        verify(clienteRepository).findByNumeroConta(anyString());
        verify(clienteRepository).save(cliente);
    }

    @Test
    void deveTransferirComSucesso() {
        // Arrange
        Cliente origem = new Cliente(123L, "João", "12345", "001", new BigDecimal("100.00"));
        Cliente destino = new Cliente(321L, "Maria", "67890", "001", new BigDecimal("50.00"));
        BigDecimal valorTransferencia = new BigDecimal("30.00");

        when(clienteRepository.findByNumeroConta("12345"))
                .thenReturn(Optional.of(origem));
        when(clienteRepository.findByNumeroConta("67890"))
                .thenReturn(Optional.of(destino));
        when(clienteRepository.save(any(Cliente.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        clienteService.transferir("12345", "67890", valorTransferencia);

        // Assert
        assertEquals(new BigDecimal("70.00"), origem.getSaldo());
        assertEquals(new BigDecimal("80.00"), destino.getSaldo());
        verify(clienteValidation, times(3)).validarValorPositivo(any(BigDecimal.class));
        verify(clienteValidation).validarSaldoSuficiente(any(BigDecimal.class), any(BigDecimal.class));
        verify(clienteRepository, times(2)).findByNumeroConta(anyString());
        verify(clienteRepository, times(2)).save(any(Cliente.class));
    }

    // ========== TESTES DE ERRO ==========

    @Test
    void deveLancarExcecaoQuandoNumeroContaJaExiste() {
        // Arrange
        Cliente cliente = new Cliente(123L, "João", "12345", "001", new BigDecimal("100.00"));

        when(clienteRepository.existsByNumeroConta("12345"))
                .thenReturn(true);

        // Configurar o mock da validação para lançar exceção quando receber true
        doThrow(new ContaException("Número da conta já existe"))
                .when(clienteValidation).validarNumeroContaUnico(true);

        // Act & Assert
        assertThrows(ContaException.class,
                () -> clienteService.cadastrarCliente(cliente));

        verify(clienteValidation).validarNumeroContaUnico(true);
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void deveLancarExcecaoQuandoBuscarPorIdNaoEncontrado() {
        // Arrange
        when(clienteRepository.findById(999L))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ClienteNotFoundException.class,
                () -> clienteService.buscarPorId(999L));

        verify(clienteRepository).findById(999L);
        // Não deve interagir com validação para busca por ID não encontrado
        verifyNoInteractions(clienteValidation);
    }

    @Test
    void deveLancarExcecaoQuandoBuscarPorNumeroContaNaoEncontrado() {
        // Arrange
        when(clienteRepository.findByNumeroConta("99999"))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ClienteNotFoundException.class,
                () -> clienteService.buscarPorNumeroConta("99999"));

        verify(clienteRepository).findByNumeroConta("99999");
        // Não deve interagir com validação para busca por número da conta não encontrado
        verifyNoInteractions(clienteValidation);
    }

    @Test
    void deveLancarExcecaoQuandoDepositarValorInvalido() {
        // Arrange
        BigDecimal valorInvalido1 = new BigDecimal("0.00");
        BigDecimal valorInvalido2 = new BigDecimal("-10.00");

        doThrow(new ValorInvalidoException("Valor deve ser positivo"))
                .when(clienteValidation).validarValorPositivo(valorInvalido1);
        doThrow(new ValorInvalidoException("Valor deve ser positivo"))
                .when(clienteValidation).validarValorPositivo(valorInvalido2);

        // Act & Assert - Primeiro valor inválido
        assertThrows(ValorInvalidoException.class,
                () -> clienteService.depositar("12345", valorInvalido1));
        verify(clienteValidation).validarValorPositivo(valorInvalido1);
        verify(clienteRepository, never()).findByNumeroConta(anyString());
        verify(clienteRepository, never()).save(any(Cliente.class));

        // Act & Assert - Segundo valor inválido
        assertThrows(ValorInvalidoException.class,
                () -> clienteService.depositar("12345", valorInvalido2));
        verify(clienteValidation).validarValorPositivo(valorInvalido2);
        verify(clienteRepository, never()).findByNumeroConta(anyString());
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void deveLancarExcecaoQuandoSaldoInsuficiente() {
        // Arrange
        Cliente cliente = new Cliente(321L, "Maria", "67890", "001", new BigDecimal("50.00"));
        BigDecimal valorSaque = new BigDecimal("100.00");

        when(clienteRepository.findByNumeroConta("67890"))
                .thenReturn(Optional.of(cliente));

        doThrow(new SaldoInsuficienteException("Saldo insuficiente"))
                .when(clienteValidation).validarSaldoSuficiente(cliente.getSaldo(), valorSaque);

        // Act & Assert
        assertThrows(SaldoInsuficienteException.class,
                () -> clienteService.sacar("67890", valorSaque));

        verify(clienteValidation).validarValorPositivo(valorSaque);
        verify(clienteValidation).validarSaldoSuficiente(cliente.getSaldo(), valorSaque);
        verify(clienteRepository).findByNumeroConta("67890");
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void deveLancarExcecaoQuandoSacarValorInvalido() {
        // Arrange
        BigDecimal valorInvalido1 = new BigDecimal("0.00");
        BigDecimal valorInvalido2 = new BigDecimal("-10.00");

        doThrow(new ValorInvalidoException("Valor deve ser positivo"))
                .when(clienteValidation).validarValorPositivo(valorInvalido1);
        doThrow(new ValorInvalidoException("Valor deve ser positivo"))
                .when(clienteValidation).validarValorPositivo(valorInvalido2);

        // Act & Assert - Primeiro valor inválido
        assertThrows(ValorInvalidoException.class,
                () -> clienteService.sacar("12345", valorInvalido1));
        verify(clienteValidation).validarValorPositivo(valorInvalido1);
        verify(clienteRepository, never()).findByNumeroConta(anyString());
        verify(clienteRepository, never()).save(any(Cliente.class));

        // Act & Assert - Segundo valor inválido
        assertThrows(ValorInvalidoException.class,
                () -> clienteService.sacar("12345", valorInvalido2));
        verify(clienteValidation).validarValorPositivo(valorInvalido2);
        verify(clienteRepository, never()).findByNumeroConta(anyString());
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void deveLancarExcecaoQuandoTransferirValorInvalido() {
        // Arrange
        BigDecimal valorInvalido1 = new BigDecimal("0.00");
        BigDecimal valorInvalido2 = new BigDecimal("-10.00");

        doThrow(new ValorInvalidoException("Valor deve ser positivo"))
                .when(clienteValidation).validarValorPositivo(valorInvalido1);
        doThrow(new ValorInvalidoException("Valor deve ser positivo"))
                .when(clienteValidation).validarValorPositivo(valorInvalido2);

        // Act & Assert - Primeiro valor inválido
        assertThrows(ValorInvalidoException.class,
                () -> clienteService.transferir("12345", "67890", valorInvalido1));
        verify(clienteValidation).validarValorPositivo(valorInvalido1);
        verify(clienteRepository, never()).findByNumeroConta(anyString());
        verify(clienteRepository, never()).save(any(Cliente.class));

        // Act & Assert - Segundo valor inválido
        assertThrows(ValorInvalidoException.class,
                () -> clienteService.transferir("12345", "67890", valorInvalido2));
        verify(clienteValidation).validarValorPositivo(valorInvalido2);
        verify(clienteRepository, never()).findByNumeroConta(anyString());
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void deveLancarExcecaoQuandoTransferirComSaldoInsuficiente() {
        // Arrange
        Cliente origem = new Cliente(123L, "João", "12345", "001", new BigDecimal("50.00"));
        BigDecimal valorTransferencia = new BigDecimal("100.00");

        when(clienteRepository.findByNumeroConta("12345"))
                .thenReturn(Optional.of(origem));

        doThrow(new SaldoInsuficienteException("Saldo insuficiente"))
                .when(clienteValidation).validarSaldoSuficiente(origem.getSaldo(), valorTransferencia);

        // Act & Assert
        assertThrows(SaldoInsuficienteException.class,
                () -> clienteService.transferir("12345", "67890", valorTransferencia));

        verify(clienteValidation, times(2)).validarValorPositivo(any(BigDecimal.class));
        verify(clienteValidation).validarSaldoSuficiente(any(BigDecimal.class), any(BigDecimal.class));
        verify(clienteRepository).findByNumeroConta(anyString());
        verify(clienteRepository).findByNumeroConta(anyString());
        verify(clienteRepository, never()).save(any(Cliente.class));
    }
}