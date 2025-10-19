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

### Via Swagger UI
1. Acesse http://localhost:8080/swagger-ui.html
2. Expanda os endpoints desejados
3. Use "Try it out" para testar

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
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
    <dependency>
        <groupId>org.springdoc</groupId>
        <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
        <version>2.3.0</version>
    </dependency>
</dependencies>
```

## 🎓 Para Mentores e Educadores

Este projeto é ideal para:
- **Ensino de Spring Boot** - Cobre todos os conceitos fundamentais
- **Mentorias técnicas** - Pode ser construído em ~1 hora
- **Code reviews** - Estrutura limpa e organizada
- **Entrevistas técnicas** - Demonstra conhecimento prático

### Roteiro de Mentoria Sugerido
1. Configuração do projeto (5 min)
2. Entidade e Repository (7 min)
3. Service com regras de negócio (15 min)
4. Controller e endpoints (8 min)
5. Tratamento de exceções (5 min)
6. Testes unitários (10 min)
7. Testes manuais no Swagger (10 min)

## 🤝 Contribuindo

Contribuições são bem-vindas! Sinta-se à vontade para:
- Reportar issues
- Sugerir novas funcionalidades
- Enviar pull requests
- Melhorar documentação

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