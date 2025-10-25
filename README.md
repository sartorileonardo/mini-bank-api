# Mini Bank API 🏦

Uma API REST simples para sistema bancário desenvolvida com Java 17 e Spring Boot, perfeita para aprendizado e mentorias.

## 📋 Sobre o Projeto

Esta é uma API de mini banco que oferece operações bancárias básicas como cadastro de clientes, depósitos, saques e transferências. Desenvolvida com foco educacional para demonstrar os conceitos fundamentais do Spring Boot em uma mentoria de 1 hora.

## 🚀 Funcionalidades

- ✅ **Cadastro de clientes** com dados completos da conta
- ✅ **Depósito** em contas existentes
- ✅ **Saque** com validação de saldo suficiente
- ✅ **Transferência** entre contas
- ✅ **Validações** de valores positivos e saldo disponível
- ✅ **Documentação interativa** com Swagger UI
- ✅ **Banco em memória** H2 para desenvolvimento

## 🛠 Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **H2 Database** (em memória)
- **Lombok** - Redução de boilerplate code
- **SpringDoc OpenAPI** - Documentação Swagger
- **JUnit 5 & Mockito** - Testes unitários
- **Maven** - Gerenciamento de dependências

## 📦 Estrutura do Projeto

```
src/main/java/com/example/minibank/
├── entity/
│   └── Cliente.java          # Entidade JPA com Lombok
├── repository/
│   └── ClienteRepository.java # Interface Spring Data JPA
├── service/
│   └── ClienteService.java    # Lógica de negócio e regras
├── controller/
│   └── ClienteController.java # Endpoints REST
├── exception/
│   ├── ClienteNotFoundException.java
│   ├── SaldoInsuficienteException.java
│   └── GlobalExceptionHandler.java
└── MiniBankApplication.java   # Classe principal
```

## 🎯 Endpoints da API

### Clientes

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/api/clientes` | Cadastrar novo cliente |
| `GET` | `/api/clientes` | Listar todos os clientes |
| `GET` | `/api/clientes/{id}` | Buscar cliente por ID |
| `GET` | `/api/clientes/conta/{numeroConta}` | Buscar por número da conta |

### Operações Bancárias

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/api/clientes/{numeroConta}/deposito` | Realizar depósito |
| `POST` | `/api/clientes/{numeroConta}/saque` | Realizar saque |
| `POST` | `/api/clientes/transferir` | Transferir entre contas |

## 🚀 Como Executar

### Pré-requisitos
- Java 17 ou superior
- Maven 3.6+

### Passo a Passo

1. **Clone o repositório:**
```bash
git clone https://github.com/sartorileonardo/mini-bank-api.git
cd mini-bank-api
```

2. **Execute a aplicação:**
```bash
./mvnw spring-boot:run
# ou
mvn spring-boot:run
```

3. **Acesse os endpoints:**
    - **API:** http://localhost:8080/api/clientes
    - **Swagger UI:** http://localhost:8080/swagger-ui.html
    - **H2 Console:** http://localhost:8080/h2-console

### Configuração do H2 Console
- **JDBC URL:** `jdbc:h2:mem:bankdb`
- **User:** `sa`
- **Password:** (vazio)

## 🧪 Testando a API

### 🚀 Testando a API via Swagger UI

Após iniciar a aplicação, você pode testar os endpoints através do Swagger UI:

1. Certifique-se de que a aplicação está rodando
2. Acesse: http://localhost:8080/swagger-ui/index.html#/
3. Você verá uma interface interativa com todos os endpoints disponíveis
4. Clique em qualquer endpoint para expandir e ver os detalhes
5. Use o botão "Try it out" para testar os endpoints diretamente pela interface
6. Preencha os parâmetros necessários e execute as requisições

O Swagger UI fornece documentação interativa e permite testar a API sem necessidade de ferramentas externas.
### Exemplo de Fluxo via cURL

1. **Cadastrar cliente João:**
```bash
curl -X POST "http://localhost:8080/api/clientes" \
-H "Content-Type: application/json" \
-d '{
  "nome": "João Silva",
  "numeroConta": "12345",
  "agencia": "001",
  "saldo": 1000.00
}'
```

2. **Cadastrar cliente Maria:**
```bash
curl -X POST "http://localhost:8080/api/clientes" \
-H "Content-Type: application/json" \
-d '{
  "nome": "Maria Santos", 
  "numeroConta": "67890",
  "agencia": "001",
  "saldo": 500.00
}'
```

3. **Realizar depósito:**
```bash
curl -X POST "http://localhost:8080/api/clientes/12345/deposito?valor=500"
```

4. **Realizar transferência:**
```bash
curl -X POST "http://localhost:8080/api/clientes/transferir?contaOrigem=12345&contaDestino=67890&valor=300"
```

## 🧪 Testes Unitários

Execute os testes com:
```bash
./mvnw test
```

Os testes cobrem os principais cenários da camada Service:
- Depósito com sucesso
- Saque com saldo insuficiente
- Transferência entre contas

## 📚 Conceitos Spring Boot Demonstrados

- **Injeção de Dependência** - `@Autowired` e `@RequiredArgsConstructor`
- **Spring Data JPA** - Repositórios com métodos de consulta
- **Spring MVC** - Controladores REST com `@RestController`
- **Tratamento de Exceções** - `@RestControllerAdvice`
- **Transações** - `@Transactional`
- **Validação** - Validações customizadas de negócio
- **Testes Unitários** - Mockito e JUnit 5
- **Documentação** - Swagger/OpenAPI automática

## 🔧 Configurações Principais

### application.properties
```properties
spring.datasource.url=jdbc:h2:mem:bankdb
spring.h2.console.enabled=true
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
server.port=8080
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
```

### Dependências Maven
```xml

<dependencies>
    <!-- Dependências geradas pelo Spring Initializr -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-devtools</artifactId>
        <scope>runtime</scope>
        <optional>true</optional>
    </dependency>

    <!-- Dependência atualizada para Swagger -->
    <dependency>
        <groupId>org.springdoc</groupId>
        <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
        <version>2.5.0</version>
    </dependency>

    <!-- Lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>

    <!-- Test -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>

```

## 📚 Glossário para Pessoas Não Técnicas

**API (Interface de Programação de Aplicações)**
- Um "mensageiro" que permite que diferentes programas de computador conversem entre si

**Conta Bancária Digital**
- Uma conta virtual onde você pode guardar e gerenciar seu dinheiro eletronicamente

**Saldo**
- O valor total de dinheiro disponível em uma conta bancária

**Transação**
- Qualquer operação que envolva dinheiro, como depósito, saque ou transferência

**Depósito**
- Adicionar dinheiro em uma conta bancária

**Saque**
- Retirar dinheiro de uma conta bancária

**Transferência**
- Enviar dinheiro de uma conta para outra

**Titular da Conta**
- A pessoa que é dona da conta bancária

**Número da Conta**
- Código único que identifica cada conta bancária

**Extrato**
- Lista de todas as transações realizadas em uma conta

## 🔧 Glossário de Anotações Técnicas

**@Entity**
- Marca uma classe como uma tabela no banco de dados

**@Id**
- Indica que um campo é a chave primária (identificador único) da tabela

**@GeneratedValue**
- Configura como os IDs são gerados automaticamente

**@Column**
- Define propriedades específicas para uma coluna do banco de dados

**@RestController**
- Marca uma classe como controlador que processa requisições web

**@RequestMapping**
- Define a URL base para todas as operações de um controlador

**@GetMapping / @PostMapping / @PutMapping / @DeleteMapping**
- Especifica o tipo de operação HTTP (GET, POST, PUT, DELETE)

**@RequestBody**
- Indica que o parâmetro vem do corpo da requisição

**@PathVariable**
- Extrai valores da URL da requisição

**@Service**
- Marca uma classe como serviço que contém regras de negócio

**@Autowired**
- Injeta automaticamente dependências entre classes

## 📦 Glossário de Bibliotecas Utilizadas

**Spring Boot**
- Framework principal para criar aplicações web em Java

**Spring Data JPA**
- Facilita a integração com bancos de dados relacionais

**H2 Database**
- Banco de dados em memória para desenvolvimento e testes

**SpringDoc OpenAPI**
- Gera documentação automática da API

**Lombok**
- Reduz código boilerplate com anotações automáticas

**JUnit**
- Framework para escrever testes automatizados

**Mockito**
- Biblioteca para criar objetos de simulação em testes

**Maven**
- Ferramenta para gerenciar dependências e build do projeto

## 🏗️ Glossário de Tipos de Classe

**Entity Classes**
- Representam tabelas do banco de dados (ex: Account, Transaction)

**Controller Classes**
- Gerenciam requisições HTTP e respostas da API

**Service Classes**
- Contêm a lógica de negócio da aplicação

**Repository Interfaces**
- Fornecem operações de acesso a dados

**DTO (Data Transfer Object)**
- Objetos para transferir dados entre camadas

**Exception Classes**
- Tratam erros e exceções específicas da aplicação

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

## 👨‍💻 Autor

**Leonardo Sartori**

- GitHub: [@sartorileonardo](https://github.com/sartorileonardo)
- LinkedIn: [Leonardo Sartori](https://linkedin.com/in/sartorileonardo)

## 🙏 Agradecimentos

- Equipe Spring Boot pelo framework incrível
- Comunidade Java por todo o conhecimento compartilhado
- Todos os contribuidores que ajudaram a melhorar este projeto

---

**⭐ Se este projeto foi útil para você, deixe uma estrela no repositório!**