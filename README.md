# coffee-break-api

É uma API, criada inicialmente como um projeto universitário, utilizada para cadastrar e avaliar os cafézinhos consumidos na pausa do trabalho.

## Objetivo

O projeto oferece uma API REST para:
- Autenticar usuários;
- Cadastrar, listar, consultar, atualizar e remover cafés;

## Stack e Versões

- Java 21
- Spring Boot 3.5.14
- Maven 3.9+ (ou Maven Wrapper `mvnw`)
- Spring Security + JWT
- Spring Data JPA
- H2 Database (memoria, ambiente local)

## Configuração de Ambiente

As configurações atuais estão em `src/main/resources/application.properties`.

Propriedades relevantes:
- `app.jwt.secret`: Chave usada para assinar JWT;
  - `app.jwt.expiration-ms`: Tempo de expiração do token;
- `spring.datasource.url`: Banco H2 em memoria (`jdbc:h2:mem:coffeebreakdb`);
- `spring.h2.console.enabled=true`: Habilita console H2.

## Executar Localmente

Linux/macOS:

```bash
./mvnw spring-boot:run
```

Windows (PowerShell):

```powershell
.\mvnw.cmd spring-boot:run
```

API local: `http://localhost:8080`

## Endpoints Principais

Autenticação:
- `POST /auth/cadastrar` - Cria usuário e retorna um token
- `POST /auth/login` - Autentica usuário e retorna um token

Cafés:
- `POST /coffees` - Cria café (requer token)
- `GET /coffees` - Lista cafés (requer token)
- `GET /coffees/{id}` - Busca cafe por id (requer token)
- `PUT /coffees/{id}` - Atualiza cafe (requer token)
- `DELETE /coffees/{id}` - Remove cafe (requer token)

## Build e Testes

Build:

```bash
./mvnw clean package
```

Testes:

```bash
./mvnw test
```

No Windows (PowerShell), use `.\mvnw.cmd` no lugar de `./mvnw`.

## Estrutura do Projeto

- `src/main/java` - Código-fonte da aplicação;
- `src/main/resources` - Configurações e scripts SQL (`schema.sql`, `data.sql`);
- `src/test/java` - Testes automatizados.

## Fluxo de Branches

- `main`: Branch padrão e estável;
- `dev`: Branch para publicação de novas funcionalidades.