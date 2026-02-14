# FieldFlowWeb - Sistema de Gestão de Materiais

Este projeto é uma solução Web Full Stack para o gerenciamento de materiais e insumos, permitindo controle de estoque e movimentações.

## 🚀 Tecnologias Utilizadas

### Backend
- **Java 17**
- **Spring Boot 4+**
- **Spring Data JPA**
- **PostgreSQL** (Banco de Dados Relacional)
- **JUnit 5** (Testes automatizados)

### Frontend (Previsto)
- **Angular 13/14/15**

## 📦 Estrutura do Projeto

O projeto segue uma arquitetura em camadas padrão de mercado:
- `controller`: Camada REST que expõe os endpoints.
- `service`: Regras de negócio.
- `repository`: Acesso a dados.
- `model`: Entidades do domínio.
- `dto`: Objetos de transferência de dados.
- `config`: Configurações globais (ex: CORS).


## 🐳 Como Executar com Docker (Recomendado)

### Pré-requisitos
- Docker e Docker Compose instalados.
- Arquivo `secrets.properties` na raiz do projeto (para desenvolvimento local/build).

### Passos
1. Navegue até a raiz do projeto.
2. Execute o comando:
   ```bash
   docker-compose up --build
   ```
3. Aguarde até que todos os serviços estejam saudáveis.

### Acesso
- **Frontend**: `http://localhost:4200`
- **Backend**: `http://localhost:8081/api`
- **Banco de Dados**: `localhost:5432`

## 🛠️ Como Executar o Backend

### Pré-requisitos
- JDK 17 instalado.

### Passos
1. Clone o repositório.
2. Navegue até a pasta raiz do projeto.
3. Execute o comando via terminal:
   - **Linux/Mac**: `./gradlew bootRun`
   - **Windows**: `gradlew.bat bootRun`

A aplicação estará disponível em: `http://localhost:8081/api/materiais`

## 🎨 Como Executar o Frontend

### Pré-requisitos
- Node.js (v16+) e npm instalados.

### Passos
1. Navegue até a pasta `frontend`.
2. Instale as dependências:
   ```bash
   npm install
   ```
3. Inicie o servidor de desenvolvimento:
   ```bash
   npm start
   ```

A aplicação estará disponível em: `http://localhost:4200`

## ✅ Testes

Para executar os testes unitários:
- **Linux/Mac**: `./gradlew test`
- **Windows**: `gradlew.bat test`

## 🔗 Endpoints Principais

- `GET /api/materiais`: Lista todos os materiais.
- `POST /api/materiais`: Cadastra um novo material.
- `PUT /api/materiais/{id}`: Atualiza um material existente.
- `GET /api/materiais/{id}`: Busca um material por ID.

