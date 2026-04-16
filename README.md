# Microsserviço de Agendador de Tarefas

Microsserviço de logística e agendamento de tarefas.

## 📝 Descrição do Projeto

Este serviço é responsável por todo o ciclo de vida das tarefas (CRUD), gerenciamento de estados e regras de negócio para lembretes e agendamentos.

Consulte o [BFF-Agendador](https://github.com/Ja0Santana/BFF-Agendador) para visualizar o sistema completo.

## 🐋 Docker Hub - Imagem Oficial

```bash
docker pull joaopaul0/api-agendador:latest
```

## 🛠️ Tecnologias e Ferramentas

- Java 21+ & Spring Boot 3
- Spring Data JPA
- PostgreSQL
- Docker
- SonarQube
- Swagger/OpenAPI

## 🔐 Segurança

- **JWT Auth**: Todas as requisições devem estar autenticadas.
- **Ownership Validation**: Cada usuário só pode acessar e editar suas próprias tarefas.
- **Environment Variables**: Nenhuma credencial está exposta no código.

## ⚙️ Variáveis de Ambiente

Copie o arquivo `.env.example` para `.env` e preencha com seus valores:

```bash
cp .env.example .env
```

| Variável | Descrição | Exemplo |
|---|---|---|
| `DB_USER` | Usuário do PostgreSQL | `postgres` |
| `DB_PASS` | Senha do PostgreSQL | `senha` |
| `DB_NAME` | Nome do banco de dados | `db_agendador` |
| `JWT_SECRET` | Chave secreta JWT | `secret` |

## 🚀 Endpoints Principais

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/tarefa` | Criar tarefa |
| `GET` | `/tarefa` | Listar todas as tarefas do usuário |
| `GET` | `/tarefa/{id}` | Buscar tarefa por ID |
| `PUT` | `/tarefa` | Atualizar dados da tarefa |
| `DELETE` | `/tarefa/{id}` | Excluir tarefa |

## 🚦 Como Rodar

### Via Docker (Recomendado)

```bash
docker-compose up --build
```

### Localmente

1. Clone o repositório:
```bash
git clone https://github.com/Ja0Santana/agendador-tarefas.git
```

2. Configure o `.env` e rode:
```bash
./gradlew bootRun
```

Swagger: `http://localhost:8081/swagger-ui.html`

## 🛡️ Qualidade

- **SOLID**: Código limpo, desacoplado e de fácil manutenção.
- **Clean Code**: Código limpo, desacoplado e de fácil manutenção.
- **Data Integrity**: Validações de constraints e segurança de dados.
- **CI/CD**: Pipeline com SonarQube e GitHub Actions para inspeção contínua.
