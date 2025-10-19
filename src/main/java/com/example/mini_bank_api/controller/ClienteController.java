package com.example.mini_bank_api.controller;

import com.example.mini_bank_api.entity.Cliente;
import com.example.mini_bank_api.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    // Cadastrar cliente
    @PostMapping
    public ResponseEntity<Cliente> cadastrar(@RequestBody Cliente cliente) {
        Cliente clienteSalvo = clienteService.cadastrarCliente(cliente);
        return ResponseEntity.ok(clienteSalvo);
    }

    // Listar todos os clientes
    @GetMapping
    public ResponseEntity<List<Cliente>> listarTodos() {
        List<Cliente> clientes = clienteService.buscarTodos();
        return ResponseEntity.ok(clientes);
    }

    // Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorId(@PathVariable Long id) {
        Cliente cliente = clienteService.buscarPorId(id);
        return ResponseEntity.ok(cliente);
    }

    // Depositar
    @PostMapping("/{numeroConta}/deposito")
    public ResponseEntity<Cliente> depositar(
            @PathVariable String numeroConta,
            @RequestParam BigDecimal valor) {
        Cliente cliente = clienteService.depositar(numeroConta, valor);
        return ResponseEntity.ok(cliente);
    }

    // Sacar
    @PostMapping("/{numeroConta}/saque")
    public ResponseEntity<Cliente> sacar(
            @PathVariable String numeroConta,
            @RequestParam BigDecimal valor) {
        Cliente cliente = clienteService.sacar(numeroConta, valor);
        return ResponseEntity.ok(cliente);
    }

    // Transferir
    @PostMapping("/transferir")
    public ResponseEntity<String> transferir(
            @RequestParam String contaOrigem,
            @RequestParam String contaDestino,
            @RequestParam BigDecimal valor) {
        clienteService.transferir(contaOrigem, contaDestino, valor);
        return ResponseEntity.ok("Transferência realizada com sucesso");
    }
}
