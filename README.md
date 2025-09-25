# OndeTáMoto? API – FIAP Challenger (Java)

**OndeTáMoto?** é uma solução IoT desenvolvida para a empresa **Mottu**, especializada em motofrete, com o objetivo de otimizar o controle de entrada, saída e localização de motos dentro da garagem da empresa.

## 🔍 Sobre o Projeto

A dinâmica do sistema é simples, porém poderosa: cada moto da frota é equipada com uma tag inteligente, que funciona como um identificador exclusivo. Assim, toda movimentação é registrada instantaneamente, sem necessidade de intervenção manual.

Esses dados são enviados para um aplicativo mobile, que centraliza todas as informações em uma interface amigável. A equipe da Mottu pode, com poucos toques na tela, visualizar o status de cada moto, saber onde ela está estacionada, identificar quais estão dentro ou fora da garagem e até categorizá-las conforme sua finalidade ou situação atual.
## 📱 Funcionalidades

- Monitoramento em tempo real das motos da garagem
- Visualização via aplicativo mobile
- Identificação das motos com tags inteligentes
- Categorização por status ou função

## 🔗 Rotas Pricipais (Swagger)

A API do projeto pode ser acessada via Swagger na rota:

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui/index.html)

## ⚠️ Atenção Importante

Devido ao relacionamento entre **Estabelecimentos** e **Usuários**, **é necessário criar um Estabelecimento antes de cadastrar um Usuário**. O usuário precisa estar vinculado a um estabelecimento já existente no sistema.

## 🔗 Rotas principais:

### 🏍️ Motos
- `GET /motos` – Lista todas as motos  
- `POST /motos` – Cadastra uma nova moto  
- `GET /motos/{id}` – Detalhes de uma moto  
- `DELETE /motos/{id}` – Remove uma moto
- `PUT /motos/{id}` – Alterar uma moto  

---

### 👤 Usuários
- `GET /usuarios` – Lista de usuários  
- `POST /usuarios` – Cadastro de usuário  
- `GET /usuarios/{id}` – Detalhes de um usuário  
- `DELETE /usuarios/{id}` – Remove um usuário
- `PUT /usuarios/{id}` – Alterar um usuário 

---

### 🏢 Estabelecimentos
- `GET /estabelecimentos` – Lista estabelecimentos  
- `POST /estabelecimentos` – Cadastro de estabelecimento  
- `GET /estabelecimentos/{id}` – Detalhes de um estabelecimento  
- `DELETE /estabelecimentos/{id}` – Remove um estabelecimento
- `PUT /estabelecimentos/{id}` – Alterar um estabelecimento 

## Rotas recomendadas para o Teste:

- `POST /estabelecimentos` – Cadastro de estabelecimento
  ```bash
   {
     "endereco": "Rua Hermando Colch, 193"
   }
- `GET /estabelecimentos` – Lista estabelecimentos
- `GET /estabelecimentos/{id}` – Detalhes de um estabelecimento
- `DELETE /estabelecimentos/{id}` – Remove um estabelecimento
- `PUT /estabelecimentos/{id}` – Alterar um estabelecimento
  ```bash
  {
    "endereco": "Rua Santo Agustinho Vieras, 225"
  }
---
- `POST /usuarios` – Cadastro de usuário
  ```bash
  {
    "email": "luizneri18@gmail.com",
    "senha": "Luizjava01",
    "idEstabelecimento": 1
  }
- `GET /usuarios` – Lista de usuários
- `GET /usuarios/{id}` – Detalhes de um usuário
- `DELETE /usuarios/{id}` – Remove um usuário
- `PUT /usuarios/{id}` – Alterar um usuário
  ```bash
  {
    "email": "guilhermeroma123@gmail.com",
    "senha": "Guizin01",
    "idEstabelecimento": 1
  }

## 🛠️ Tecnologias Utilizadas

- ☕ Java 17
- 🌱 Spring Boot
- 🟦 Spring Data JPA
- 🟩 Bean Validation
- 📦 Spring Cache
- 📄 Swagger/OpenAPI
- 🛢️ Banco de Dados H2 
- 🐳 Docker (containerização da API)

## 🚀 Como Executar

1. Clone o repositório:
   ```bash
   git clone https://github.com/GuiRomanholi/ondetamoto.git
   cd ondetamoto

## 🧑‍💻 Integrantes do Grupo

- **Guilherme Romanholi Santos - RM557462**
- **Murilo Capristo - RM556794**
- **Nicolas Guinante Cavalcanti - RM557844**

---

# 🐳 Parte 2: DevOps — Containerização da API

## 🎥 Link do Vídeo
[Link do Video](https://www.youtube.com/watch?v=GOuvrEtKBo4)

**OndeTáMoto?** é uma solução IoT desenvolvida para a empresa **Mottu**, especializada em motofrete, com o objetivo de otimizar o controle de entrada, saída e localização de motos dentro da garagem da empresa.

## 🔍 Sobre o Projeto

A dinâmica do sistema é simples, porém poderosa: cada moto da frota é equipada com uma tag inteligente, que funciona como um identificador exclusivo. Assim, toda movimentação é registrada instantaneamente, sem necessidade de intervenção manual.

Esses dados são enviados para um aplicativo mobile, que centraliza todas as informações em uma interface amigável. A equipe da Mottu pode, com poucos toques na tela, visualizar o status de cada moto, saber onde ela está estacionada, identificar quais estão dentro ou fora da garagem e até categorizá-las conforme sua finalidade ou situação atual.
## 📱 Funcionalidades

- Monitoramento em tempo real das motos da garagem
- Visualização via aplicativo mobile
- Identificação das motos com tags inteligentes
- Categorização por status ou função

### Build e execução

1. **Gere o JAR:**
    ```sh
    gradlew build
    ```

2. **Construa a imagem Docker:**
    ```sh
    docker build -t ondetamoto-app .
    ```

3. **Rode o container:**
    ```sh
    docker run -d -p 8080:8080 --name ondetamoto-app ondetamoto-app
    ```

4. **Acesse a aplicação:**
    - [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
    - [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
      - JDBC URL: jdbc:h2:mem:testdb
      - User Name: sa
      - Password: deixar em branco

---
