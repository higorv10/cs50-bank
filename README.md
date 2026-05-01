# 🏦 Banking API

Uma API bancária RESTful construída com **Java** e **Spring Boot**, com gerenciamento de contas, operações financeiras e autenticação segura via JWT.

---

## 📋 Índice

- [Sobre o Projeto](#sobre-o-projeto)
- [Funcionalidades](#funcionalidades)
- [Tecnologias](#tecnologias)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Como Rodar](#como-rodar)
  - [Pré-requisitos](#pré-requisitos)
  - [Instalação](#instalação)
  - [Configuração](#configuração)
  - [Executando a Aplicação](#executando-a-aplicação)
- [Endpoints da API](#endpoints-da-api)
- [Fluxo de Autenticação](#fluxo-de-autenticação)
- [Visão Geral da Arquitetura](#visão-geral-da-arquitetura)
- [Banco de Dados](#banco-de-dados)
- [Licença](#licença)

---

## 📖 Sobre o Projeto

Esta é uma API bancária baseada em Java/Spring Boot cujas principais funcionalidades são a criação de contas **correntes** ou **poupança** e a realização de **depósitos** ou **saques**. O acesso a essas funcionalidades requer autenticação via cadastro e login.

- **Contas correntes** possuem limite de cheque especial de **R$ 500**
- **Contas poupança** possuem taxa de juros de **0,03%**
- Senhas são criptografadas com **bcrypt**
- A autenticação é feita via **tokens JWT** (expiram em 30 minutos), protegendo contra ataques CSRF
- DTOs de resposta customizados evitam vazamento de dados sensíveis

---

## ✨ Funcionalidades

- Cadastro e login de usuários com senhas criptografadas (bcrypt)
- Autenticação e autorização baseadas em JWT
- Criação de contas correntes ou poupança
- Depósito em conta própria ou de outros usuários
- Saque com proteção de limite de cheque especial (contas correntes)
- Validação de campos com Bean Validation e mensagens de erro customizadas
- Tratamento global de exceções com respostas estruturadas
- Documentação da API via Swagger/OpenAPI

---

## 🛠️ Tecnologias

| Tecnologia | Finalidade |
|---|---|
| Java + Spring Web | Criação e exposição dos endpoints REST |
| Spring Security | Autenticação e autorização |
| Spring Data JPA | Camada de acesso ao banco de dados |
| PostgreSQL | Banco de dados relacional |
| Flyway | Migrações de banco de dados |
| JJWT | Geração e validação de tokens JWT |
| Lombok | Redução de boilerplate |
| Spring Boot DevTools | Reload automático em desenvolvimento |
| SpringDoc (OpenAPI/Swagger) | Documentação da API |

---

## 📁 Estrutura do Projeto

```
src/main/java/com/higor/cs50x/
├── config/
│   ├── JWTFilter.java
│   └── SecurityConfig.java
├── controller/
│   ├── AccountController.java
│   └── UserController.java
├── dto/
│   ├── request/
│   │   ├── AccountRequest.java
│   │   ├── DepositRequest.java
│   │   ├── LoginRequest.java
│   │   ├── RegisterUserRequest.java
│   │   └── WithdrawRequest.java
│   └── response/
│       ├── AccountResponse.java
│       ├── DepositResponse.java
│       ├── ErrorResponse.java
│       ├── LoginResponse.java
│       ├── MyAccountResponse.java
│       ├── ProfileResponse.java
│       ├── RegisterUserResponse.java
│       └── WithdrawResponse.java
├── exceptions/
│   ├── BusinessException.java
│   ├── GlobalExceptionHandler.java
│   ├── InvalidCredentialsException.java
│   ├── MyAccountNotFoundException.java
│   └── UserAlreadyExistsException.java
├── model/
│   ├── adapter/
│   │   └── UserPrincipal.java
│   ├── entity/
│   │   ├── Account.java          ← Classe abstrata base
│   │   ├── CheckingAccount.java
│   │   ├── SavingsAccount.java
│   │   └── User.java
│   └── enums/
│       └── AccountType.java
├── repository/
│   ├── AccountRepository.java
│   └── UserRepository.java
├── service/
│   ├── AccountService.java
│   ├── JWTService.java
│   ├── MyUserDetailsService.java
│   └── UserService.java
└── Cs50xApplication.java

src/main/resources/
├── db/migration/         ← Migrações SQL do Flyway
├── static/
├── templates/
├── application.yaml
└── ValidationMessages.properties
```

---

## 🚀 Como Rodar

### Pré-requisitos

- Java 17+
- Maven
- [PostgreSQL](https://www.postgresql.org/download/)

### Instalação

1. **Clone o repositório**
   ```bash
   git clone https://github.com/seu-usuario/banking-api.git
   cd banking-api
   ```

2. **Instale as dependências**
   ```bash
   mvn install
   ```

### Configuração

Defina as variáveis de ambiente abaixo ou edite diretamente o arquivo `src/main/resources/application.yaml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/seu_banco
    username: seu_usuario
    password: sua_senha
```

Ou via variáveis de ambiente:

```bash
export DB_URL=jdbc:postgresql://localhost:5432/seu_banco
export DB_USERNAME=seu_usuario
export DB_PASSWORD=sua_senha
export JWT_SECRET=sua_chave_secreta_jwt
```

> ⚠️ **Importante:** Certifique-se de que o PostgreSQL está em execução e que o banco de dados existe antes de iniciar a aplicação. O Flyway criará as tabelas automaticamente.

### Executando a Aplicação

```bash
mvn spring-boot:run
```

A API estará disponível em `http://localhost:8080`.

Swagger UI: `http://localhost:8080/swagger-ui.html`

---

## 📡 Endpoints da API

> Todos os endpoints protegidos 🔒 exigem o header: `Authorization: Bearer <seu_token>`

### 👤 Usuário — `/auth`

| Método | Endpoint | Auth | Descrição |
|---|---|---|---|
| `POST` | `/auth/register` | ❌ | Cadastrar novo usuário |
| `POST` | `/auth/login` | ❌ | Login e recebimento do token JWT |
| `GET` | `/auth/my/profile` | 🔒 | Retorna o perfil do usuário logado |

<details>
<summary><code>POST</code> <strong>/auth/register</strong></summary>

**Request body:**
```json
{
  "name": "string",
  "cpf": "59835673300",
  "cellphone": "15784368239582",
  "email": "user@example.com",
  "password": "stringst"
}
```

**Response `201 Created`:**
```json
{
  "message": "string",
  "name": "string",
  "email": "string"
}
```

**Response `400 Bad Request`:** campos preenchidos incorretamente.
</details>

<details>
<summary><code>POST</code> <strong>/auth/login</strong></summary>

**Request body:**
```json
{
  "email": "user@example.com",
  "password": "string"
}
```

**Response `200 OK`:**
```json
{
  "message": "string",
  "email": "string",
  "token": "string",
  "tokenType": "string",
  "expiresIn": 0
}
```
</details>

<details>
<summary><code>GET</code> <strong>/auth/my/profile</strong></summary>

**Response `200 OK`:**
```json
{
  "id": 0,
  "name": "string",
  "cpf": "string",
  "cellphone": "string",
  "email": "string"
}
```

**Response `401 Unauthorized`:** autenticação necessária.
</details>

---

### 🏦 Contas — `/api/accounts`

| Método | Endpoint | Auth | Descrição |
|---|---|---|---|
| `POST` | `/api/accounts/create` | 🔒 | Criar conta corrente ou poupança |
| `GET` | `/api/accounts/me` | 🔒 | Listar contas do usuário logado |
| `POST` | `/api/accounts/deposit` | 🔒 | Depositar em conta própria ou de outro usuário |
| `POST` | `/api/accounts/withdraw` | 🔒 | Sacar da própria conta |

<details>
<summary><code>POST</code> <strong>/api/accounts/create</strong></summary>

**Request body:**
```json
{
  "type": "CHECKING",
  "password": "9193544549"
}
```
> Valores aceitos para `type`: `CHECKING` (corrente) ou `SAVINGS` (poupança)

**Response `201 Created`:**
```json
{
  "message": "string",
  "branch": "string",
  "number": "string"
}
```

**Response `401 Unauthorized`:** não autenticado.
</details>

<details>
<summary><code>GET</code> <strong>/api/accounts/me</strong></summary>

**Response `200 OK`:**
```json
[
  {
    "branch": "string",
    "number": "string",
    "type": "CHECKING",
    "balance": 0
  }
]
```

**Response `401 Unauthorized`:** autenticação necessária.
</details>

<details>
<summary><code>POST</code> <strong>/api/accounts/deposit</strong></summary>

**Request body:**
```json
{
  "branch": "str",
  "accountNumber": "string",
  "amount": 0
}
```

**Response `200 OK`:**
```json
{
  "message": "string",
  "accountType": "CHECKING",
  "timestamp": "2026-05-01T21:08:51.742Z"
}
```
</details>

<details>
<summary><code>POST</code> <strong>/api/accounts/withdraw</strong></summary>

**Request body:**
```json
{
  "branch": "str",
  "accountNumber": "string",
  "amount": 0,
  "password": "string"
}
```

**Response `200 OK`:**
```json
{
  "message": "string",
  "currentBalance": 0,
  "timestamp": "2026-05-01T21:09:22.355Z"
}
```
</details>

---

## 🔐 Fluxo de Autenticação

1. **Cadastre** um usuário via `POST /auth/register`
2. **Faça login** via `POST /auth/login` — você receberá um token JWT
3. Inclua o token no cabeçalho `Authorization` em todos os endpoints protegidos:
   ```
   Authorization: Bearer <seu_token>
   ```
4. Os tokens expiram após **30 minutos**

---

## 🏗️ Visão Geral da Arquitetura

### Entidades

- **Account** — Classe abstrata mapeada para a tabela única `accounts` usando `@Inheritance` do JPA com `discriminator_column` (`account_type`)
- **CheckingAccount** — Estende `Account`, adiciona `overdraftLimit`; valor do discriminador: `CHECKING`
- **SavingsAccount** — Estende `Account`, adiciona `interestRate`; valor do discriminador: `SAVINGS`
- **User** — Mapeia os dados do usuário; chamada de `users` no banco para evitar conflito com a palavra reservada `user` do PostgreSQL. Possui relacionamento `OneToMany` com `Account` via `HashSet`

### Segurança

- **SecurityConfig** — Configura o `SecurityFilterChain`, desabilita CSRF, define sessão stateless e instancia `AuthenticationManager`, `PasswordEncoder` (bcrypt) e `DaoAuthenticationProvider`
- **JWTFilter** — Estende `OncePerRequestFilter`; valida o JWT a cada requisição e define o contexto de segurança
- **JWTService** — Gera e valida tokens usando HMAC-SHA256

### DTOs

Os DTOs de requisição e resposta são implementados como **Java Records**, garantindo imutabilidade e reduzindo boilerplate. As anotações `@Valid` aplicam validação em nível de campo sem verificações manuais. As respostas customizadas evitam a exposição das entidades e o vazamento de dados.

---

## 🗄️ Banco de Dados

As tabelas são criadas automaticamente pelas migrações do **Flyway**, localizadas em `src/main/resources/db/migration/`.

| Tabela | Descrição |
|---|---|
| `users` | Armazena os dados pessoais dos usuários e as senhas criptografadas |
| `accounts` | Herança de tabela única para contas correntes e poupança |

Os números de conta e de agência são gerados aleatoriamente, com um loop que consulta o banco de dados para garantir unicidade antes de persistir.

---

## 📄 Licença

Este projeto está licenciado sob a licença **MIT**. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.
