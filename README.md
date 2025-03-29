# API de Chat em Java (MVP)

Este projeto é um MVP de uma API de chat em Java, construído utilizando Clean Architecture e comunicação via WebSocket. O objetivo inicial é fornecer funcionalidades básicas de envio e recebimento de mensagens em tempo real.

## Estrutura do Projeto

O projeto segue a Clean Architecture, organizando o código em camadas distintas:

* **domain:** Lógica de negócio e entidades principais.
* **application:** Casos de uso e interfaces (ports).
* **infrastructure:** Adaptação para tecnologias externas (WebSocket, banco de dados).
* **interfaces:** Controladores que expõem a API.

## Tecnologias Utilizadas

* **Java:** Linguagem de programação principal.
* **Spring Boot:** Framework para desenvolvimento rápido de aplicações Java.
* **WebSocket:** Comunicação em tempo real para o chat.

## Pré-requisitos

* Java Development Kit (JDK) 11 ou superior
* Maven ou Gradle (para gerenciamento de dependências)

## Como Executar

1.  Clone o repositório:

    ```bash
    git clone https://github.com/ChatterMobileApp/chatter-api
    ```

2.  Navegue até o diretório do projeto:

    ```bash
    cd chatter-api
    ```

3.  Execute a aplicação:

    ```bash
    # Usando Maven
    ./mvnw spring-boot:run

    # Usando Gradle
    ./gradlew bootRun
    ```

4.  A API estará disponível em `http://localhost:8080`.

## Endpoints WebSocket

* `/chat`: Endpoint para comunicação via WebSocket.

## Casos de Uso (MVP)

* **Enviar Mensagem:** Permite enviar mensagens para outros usuários.
* **Receber Mensagem:** Permite receber mensagens de outros usuários em tempo real.

## Próximos Passos

* Implementar autenticação e autorização de usuários.
* Adicionar persistência de mensagens em um banco de dados.
* Implementar notificações push.
* Expandir para uma arquitetura de microserviços.

## Contribuição

Contribuições são bem-vindas! Sinta-se à vontade para abrir issues e pull requests.

## Licença

MIT