package com.example.mini_bank_api.controller;

import com.example.mini_bank_api.entity.Cliente;
import com.example.mini_bank_api.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
@Tag(name = "Clientes", description = "API para gerenciamento de clientes e suas operações bancárias")
public class ClienteController {

    private final ClienteService clienteService;

    @Operation(
            summary = "Cadastrar novo cliente",
            description = "Cria um novo cliente com conta bancária no sistema"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Cliente cadastrado com sucesso",
                    content = @Content(schema = @Schema(implementation = Cliente.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Número da conta já existe ou dados inválidos"
            )
    })
    @PostMapping
    public ResponseEntity<Cliente> cadastrar(
            @Parameter(description = "Dados do cliente a ser cadastrado", required = true)
            @RequestBody Cliente cliente) {
        Cliente clienteSalvo = clienteService.cadastrarCliente(cliente);
        return ResponseEntity.ok(clienteSalvo);
    }

    @Operation(
            summary = "Listar todos os clientes",
            description = "Retorna uma lista com todos os clientes cadastrados no sistema"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de clientes recuperada com sucesso",
            content = @Content(schema = @Schema(implementation = Cliente[].class))
    )
    @GetMapping
    public ResponseEntity<List<Cliente>> listarTodos() {
        List<Cliente> clientes = clienteService.buscarTodos();
        return ResponseEntity.ok(clientes);
    }

    @Operation(
            summary = "Buscar cliente por ID",
            description = "Recupera os dados de um cliente específico através do seu ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Cliente encontrado com sucesso",
                    content = @Content(schema = @Schema(implementation = Cliente.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cliente não encontrado"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorId(
            @Parameter(description = "ID do cliente", example = "1", required = true)
            @PathVariable Long id) {
        Cliente cliente = clienteService.buscarPorId(id);
        return ResponseEntity.ok(cliente);
    }

    @Operation(
            summary = "Realizar depósito",
            description = "Adiciona um valor ao saldo da conta do cliente"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Depósito realizado com sucesso",
                    content = @Content(schema = @Schema(implementation = Cliente.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Valor inválido para depósito"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Conta não encontrada"
            )
    })
    @PostMapping("/{numeroConta}/deposito")
    public ResponseEntity<Cliente> depositar(
            @Parameter(description = "Número da conta para depósito", example = "12345", required = true)
            @PathVariable String numeroConta,

            @Parameter(description = "Valor a ser depositado", example = "100.50", required = true)
            @RequestParam BigDecimal valor) {
        Cliente cliente = clienteService.depositar(numeroConta, valor);
        return ResponseEntity.ok(cliente);
    }

    @Operation(
            summary = "Realizar saque",
            description = "Remove um valor do saldo da conta do cliente, desde que haja saldo suficiente"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Saque realizado com sucesso",
                    content = @Content(schema = @Schema(implementation = Cliente.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Valor inválido ou saldo insuficiente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Conta não encontrada"
            )
    })
    @PostMapping("/{numeroConta}/saque")
    public ResponseEntity<Cliente> sacar(
            @Parameter(description = "Número da conta para saque", example = "12345", required = true)
            @PathVariable String numeroConta,

            @Parameter(description = "Valor a ser sacado", example = "50.00", required = true)
            @RequestParam BigDecimal valor) {
        Cliente cliente = clienteService.sacar(numeroConta, valor);
        return ResponseEntity.ok(cliente);
    }

    @Operation(
            summary = "Realizar transferência",
            description = "Transfere um valor entre duas contas bancárias"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Transferência realizada com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Valor inválido, saldo insuficiente ou contas iguais"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Conta de origem ou destino não encontrada"
            )
    })
    @PostMapping("/transferir")
    public ResponseEntity<String> transferir(
            @Parameter(description = "Número da conta de origem", example = "12345", required = true)
            @RequestParam String contaOrigem,

            @Parameter(description = "Número da conta de destino", example = "67890", required = true)
            @RequestParam String contaDestino,

            @Parameter(description = "Valor a ser transferido", example = "75.25", required = true)
            @RequestParam BigDecimal valor) {
        clienteService.transferir(contaOrigem, contaDestino, valor);
        return ResponseEntity.ok("Transferência realizada com sucesso");
    }

    @Operation(
            summary = "Buscar cliente por número da conta",
            description = "Recupera os dados de um cliente através do número da conta bancária"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Cliente encontrado com sucesso",
                    content = @Content(schema = @Schema(implementation = Cliente.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Conta não encontrada"
            )
    })
    @GetMapping("/conta/{numeroConta}")
    public ResponseEntity<Cliente> buscarPorNumeroConta(
            @Parameter(description = "Número da conta bancária", example = "12345", required = true)
            @PathVariable String numeroConta) {
        Cliente cliente = clienteService.buscarPorNumeroConta(numeroConta);
        return ResponseEntity.ok(cliente);
    }
}