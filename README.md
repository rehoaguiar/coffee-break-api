# coffee-break-api

É uma API criada inicialmente como um projeto universitário, utilizada para cadastrar e avaliar os cafezinhos consumidos na pausa do trabalho.

## Objetivo

O projeto oferece uma API REST para:
- Autenticar usuários;
- Cadastrar, listar, consultar, atualizar e remover cafés.

## Stack e Versões

- Java 21
- Spring Boot 3.5.14
- Maven 3.9+
- Spring Security + JWT
- Spring Data JPA
- H2 Database em memória para ambiente local

## Configuração de Ambiente

As configurações atuais estão em `src/main/resources/application.properties`.

Propriedades relevantes:
- `app.jwt.secret`: chave usada para assinar JWT;
- `app.jwt.expiration-ms`: tempo de expiração do token;
- `spring.datasource.url`: banco H2 em memória (`jdbc:h2:mem:coffeebreakdb`);
- `spring.h2.console.enabled=true`: habilita o console H2.

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
- `POST /auth/register` - Cria usuário e retorna um token
- `POST /auth/login` - Autentica usuário e retorna um token

Cafés:
- `POST /coffees` - Cria café (requer token)
- `GET /coffees` - Lista cafés
- `GET /coffees/{id}` - Busca café por id
- `PUT /coffees/{id}` - Atualiza café (requer token)
- `DELETE /coffees/{id}` - Remove café (requer token)

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
- `src/main/resources` - Configurações da aplicação;
- `src/test/java` - Testes automatizados.

## Fluxo de Branches

- `main`: branch padrão e estável;
- `dev`: branch para publicação de novas funcionalidades.
