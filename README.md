# coffee-break-api

É uma API criada inicialmente como um projeto universitário, utilizada para cadastrar e avaliar os cafézinhos consumidos na pausa do trabalho.

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
- H2 Database para desenvolvimento e testes
- PostgreSQL para produção

## Configuração de Ambiente

As configurações estão em `src/main/resources/application.properties`, com um arquivo por perfil:

| Perfil | Banco | Quando é usado |
| --- | --- | --- |
| (padrão) | H2 em memória | testes (`mvn test`); os dados somem ao parar |
| `dev` | H2 em arquivo, em `./data/` | `mvn spring-boot:run`; contas e cafés continuam após reiniciar |
| `prod` | PostgreSQL | `SPRING_PROFILES_ACTIVE=prod` |

Para zerar o banco local, pare a API e apague a pasta `data/`.

Propriedades relevantes:
- `app.jwt.secret` (`APP_JWT_SECRET`): chave usada para assinar JWT. Em produção, defina sempre uma chave própria;
- `app.jwt.expiration-ms`: tempo de expiração do token;
- `spring.datasource.url` (`DB_URL`), `DB_USERNAME`, `DB_PASSWORD`: conexão com o banco. No perfil `prod` são obrigatórias, por exemplo `DB_URL=jdbc:postgresql://localhost:5432/coffeebreak`;
- `spring.jpa.hibernate.ddl-auto=update`: o Hibernate cria/atualiza as tabelas (em produção pode ser trocado por `DDL_AUTO`);
- `spring.h2.console.enabled`: console H2 ligado no dev e desligado no `prod`.

## Executar Localmente

Linux/macOS:

```bash
./mvnw spring-boot:run
```

Windows (PowerShell):

```powershell
.\mvnw.cmd spring-boot:run
```

O `spring-boot:run` já usa o perfil `dev`. Ao rodar pela IDE, ative o perfil `dev` na configuração de execução.

API local: `http://localhost:8090` (para usar outra porta, defina a variável `PORT`)

O app web do Expo (`http://localhost:*`) pode chamar a API pelo navegador. Para liberar outras origens, defina `CORS_ORIGINS` com a lista separada por vírgulas.

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
