package com.example.mini_bank_api.service;


import com.example.mini_bank_api.entity.Cliente;
import com.example.mini_bank_api.exception.ContaException;
import com.example.mini_bank_api.exception.SaldoInsuficienteException;
import com.example.mini_bank_api.exception.ClienteNotFoundException;
import com.example.mini_bank_api.exception.ValorInvalidoException;
import com.example.mini_bank_api.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    // Cadastrar cliente
    public Cliente cadastrarCliente(Cliente cliente) {
        if (clienteRepository.existsByNumeroConta(cliente.getNumeroConta())) {
            throw new ContaException("Número da conta já existe");
        }
        return clienteRepository.save(cliente);
    }

    // Buscar todos os clientes
    @Transactional(readOnly = true)
    public List<Cliente> buscarTodos() {
        return clienteRepository.findAll();
    }

    // Buscar por ID
    @Transactional(readOnly = true)
    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNotFoundException("Cliente não encontrado"));
    }

    // Buscar por número da conta
    @Transactional(readOnly = true)
    public Cliente buscarPorNumeroConta(String numeroConta) {
        return clienteRepository.findByNumeroConta(numeroConta)
                .orElseThrow(() -> new ClienteNotFoundException("Conta não encontrada"));
    }

    // Depositar
    public Cliente depositar(String numeroConta, BigDecimal valor) {
        validarValorPositivo(valor);

        Cliente cliente = buscarPorNumeroConta(numeroConta);
        cliente.setSaldo(cliente.getSaldo().add(valor));

        return clienteRepository.save(cliente);
    }

    // Sacar
    public Cliente sacar(String numeroConta, BigDecimal valor) {
        validarValorPositivo(valor);

        Cliente cliente = buscarPorNumeroConta(numeroConta);

        if (cliente.getSaldo().compareTo(valor) < 0) {
            throw new SaldoInsuficienteException("Saldo insuficiente");
        }

        cliente.setSaldo(cliente.getSaldo().subtract(valor));
        return clienteRepository.save(cliente);
    }

    // Transferir
    public void transferir(String contaOrigem, String contaDestino, BigDecimal valor) {
        validarValorPositivo(valor);

        // Sacar da conta origem
        Cliente origem = sacar(contaOrigem, valor);

        // Depositar na conta destino
        depositar(contaDestino, valor);
    }

    // Validação de valor positivo
    private void validarValorPositivo(BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValorInvalidoException("Valor deve ser positivo");
        }
    }
}
