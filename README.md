# 🎬 DSMovie — Testes de API com RestAssured

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.0.0-brightgreen)
![RestAssured](https://img.shields.io/badge/RestAssured-API%20testing-25A162)
![JUnit](https://img.shields.io/badge/JUnit-5-blue)
![OAuth2](https://img.shields.io/badge/OAuth2-JWT-red)

Implementação dos **testes de API** do projeto DSMovie, usando **RestAssured** para validar status codes, corpo das respostas, autenticação OAuth2 e autorização por papel de usuário.

Este repositório é a resolução do **Desafio DSMovie RestAssured**, do curso **Java Spring Expert**, da [DevSuperior](https://devsuperior.com.br).

---

## 📋 Sobre o desafio

O objetivo é implementar testes de API de ponta a ponta, batendo nos endpoints HTTP de uma instância real da aplicação em execução — diferente dos testes unitários de service, que isolam a lógica com mocks.

- **10 cenários** de teste: 7 em `MovieControllerRA` e 3 em `ScoreControllerRA`
- **Mínimo para aprovação:** 8 dos 10 testes
- Cobertura de autenticação (OAuth2 password grant), autorização por papel (`ADMIN`/`CLIENT`) e validação de DTOs

### Competências trabalhadas

- Testes de API com RestAssured
- Autenticação e autorização com OAuth2 e JWT
- Validação de contratos HTTP (status codes, corpo da resposta)

---

## 🎞️ Sobre o projeto DSMovie

O DSMovie é uma API REST de filmes e avaliações de filmes.

- A **consulta** de filmes é pública (não exige login)
- **Inserir, atualizar e deletar** filmes é permitido apenas para usuários `ADMIN`
- **Avaliar** um filme pode ser feito por qualquer usuário logado (`CLIENT` ou `ADMIN`)
- A entidade `Score` guarda a nota (de 0 a 5) que cada usuário deu a cada filme
- A cada nova avaliação, o sistema recalcula a **média** das notas de todos os usuários e a grava no filme (`score`), junto com a contagem de votos (`count`)

### Endpoints cobertos pelos testes

| Método | Rota | Acesso | Descrição |
|--------|------|--------|-----------|
| `GET` | `/movies` | Público | Lista filmes paginados (filtro opcional `title`) |
| `GET` | `/movies/{id}` | Público | Busca um filme por id |
| `POST` | `/movies` | `ADMIN` | Insere um filme |
| `PUT` | `/scores` | `CLIENT` ou `ADMIN` | Registra uma nota e recalcula a média do filme |
| `POST` | `/oauth2/token` | Cliente OAuth2 | Obtém o token de acesso (grant `password`) |

---

## 🗂️ Estrutura do repositório

Este repositório contém **dois projetos Maven independentes**, sem dependência de módulo entre eles:

```
dsmovie-restassured/
├── dsmovie/                  ← API (projeto base do desafio)
│   ├── src/main/...
│   └── pom.xml
└── dsmovie-restassured/      ← Testes de API com RestAssured
    ├── src/test/java/com/devsuperior/dsmovie
    │   ├── controllers
    │   │   ├── MovieControllerRA.java
    │   │   └── ScoreControllerRA.java
    │   └── tests
    │       └── TokenUtil.java
    └── pom.xml
```

Cada pasta é importada separadamente na IDE, como dois projetos Maven distintos.

---

## 🛠️ Tecnologias

**API (`dsmovie/`)**
- Java 17, Spring Boot 3.0.0 (Web, Data JPA, Validation)
- Spring Security com OAuth2 Authorization Server (1.0.0) e Resource Server (JWT)
- Banco H2 em memória (perfil `test`)

**Testes (`dsmovie-restassured/`)**
- Java 17, Spring Boot 3.1.0 (apenas como parent do Maven)
- RestAssured para chamadas HTTP e validação de respostas
- JUnit 5
- `org.json` / `json-simple` para montar corpos de requisição JSON

---

## 🧪 Cenários de teste

### MovieControllerRA

- [ ] `findAllShouldReturnOkWhenMovieNoArgumentsGiven`
- [ ] `findAllShouldReturnPagedMoviesWhenMovieTitleParamIsNotEmpty`
- [ ] `findByIdShouldReturnMovieWhenIdExists`
- [ ] `findByIdShouldReturnNotFoundWhenIdDoesNotExist`
- [ ] `insertShouldReturnUnprocessableEntityWhenAdminLoggedAndBlankTitle`
- [ ] `insertShouldReturnForbiddenWhenClientLogged`
- [ ] `insertShouldReturnUnauthorizedWhenInvalidToken`

### ScoreControllerRA

- [ ] `saveScoreShouldReturnNotFoundWhenMovieIdDoesNotExist`
- [ ] `saveScoreShouldReturnUnprocessableEntityWhenMissingMovieId`
- [ ] `saveScoreShouldReturnUnprocessableEntityWhenScoreIsLessThanZero`

### Como os testes funcionam

- Os testes rodam **contra a API em execução** (`http://localhost:8080`), e não contra um contexto Spring simulado
- O `TokenUtil` obtém tokens de acesso via `POST /oauth2/token`, usando o grant `password`
- Cada classe de teste configura tokens de usuários `ADMIN` e `CLIENT` (e um token inválido, para o cenário de `401`) no `setUp`
- Os ids de filmes e usuários usados nos testes vêm dos dados fixos carregados pelo `import.sql` da API

---

## 🚀 Como executar

**Pré-requisitos:** JDK 17 e Maven instalados. As duas pastas devem ser importadas como projetos Maven separados.

### 1. Suba a API

```bash
cd dsmovie
mvn spring-boot:run
```

A API sobe na porta `8080`, com o perfil `test` (H2 em memória).

### 2. Rode os testes de API

Com a API em execução, em outro terminal:

```bash
cd dsmovie-restassured
mvn test
```

---

## 👨‍💻 Autor

**Breno Oliveira de Souza**
Desenvolvedor Backend Java · Estudante de Engenharia de Software

[![GitHub](https://img.shields.io/badge/GitHub-obrenoxs-181717?logo=github)](https://github.com/obrenoxs)
