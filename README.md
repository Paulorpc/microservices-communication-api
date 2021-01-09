# Microservices-Communication-API_Java
API com arquitetura em micro-serviços e comunicação entre módulos usando mensageria.

A proposta deste projeto é criar uma aplicação de aprendizagem com arquitetura em microserviços com 4 módulos utilizando mensageria. É importante pensar em tolerêcia a falhas (_Design for Failure_) quando se trata de mensageria, já que em grande volumes a troca de mensagens entre os sistema podem sofrer falhas e o client não deve ser impactado. Para issso é usado o _Pattern Circuit Breaker_. 

Serão utilizados para o projeto:
1. Java / Maven
2. Spring Boot / Spring Cloud
3. Docker (docker-compose)
4. MySQL
5. RabbitMQ


Dependências Relevantes:
1. Lombok (source code implementation)
2. Spring Cloud Binder Rabbit (Rabbitmq Mapper)
3. Hystrix (Circuit Breaker)
4. Actuator (API Monitor/Manager)


### MÓDULOS
#### ms-communication-buytrip
REST API resposável pela recepção de compra. Recebe a requisição de compra e envia para fila de entrada onde fica aguardando processamento.
Recebe:  Body: `JSON` | Method: `POST` | Dados de compra da realizada pelo cliente.
Retorna: Status: `200` | msg: `"Compra registrada com sucesso. Aguarda a confirmação do pagamento."`

#### ms-communication-buyprocess
API resposável pelo processamento do pagamento. Irá fazer a recepção das mensagem na fila de aguardando compra, enviará para a API de banco processar o pagamento e envia msg para fila de compras finalizadas. Se o API do banco estiver fora por alguma razão a msg é republicada na fila de compras aguardando processamento para processamento. 
Recebe:  Body: `JSON` | Method: `POST` | Dados do pagamento da compra.
Retorna: Status: `200` | msg: `"Pagamento registrado com sucesso."`

#### ms-communication-bank
REST API resposável pela recepição do pagamento, validar cartão e saldo e atualizar conta do usuário. 
Recebe:  Body: `JSON` | Method: `POST` | Dados do pagamento da compra.
Retorna: Status: `200` | msg: `"Pagamento registrado com sucesso."`


### AMBIENTE DESENVOLVIMENTO
Todo ambiente pode ser criado utilizando docker-compose. Na pasta docker foi criado o YAML para coposição das imagens, utilizando a nomenclatura padrão para simplificar a execução. 
```shell
$ cd <project_folder>/docker
$ docker-compose up -d
```

### MYSQL
Após rodar o docker-compose.yml, é necessário criar o database `banco` no banco de dados MySQL.
```shell
$ docker exec -it <container_name> mysql -u root -p
$ root #password
$ create database banco;
```

### LOMBOK DEPENDENCY
A dependencia do lombok é utilizada para gerar métodos, construtores, etc. através de notações (exemplo: `@Data`). É necessário instalar o projeto lombok no IDE que o mesmo reconheça os métodos que não são criados fisicamente no arquivo .java. 
- https://projectlombok.org/download
```shell
$ java -jar ~/Downloads/lombok.jar
```

### RABBITMQ
Com o Rabbitmq funcionando, deve-se criar as filas. Para isso, basta acessar a tab queus e criar as filas necessárias. 
Atenção para o nome que deve ser identico ao configurado no `application.properties` da API. 
- fila-saida:    `fila-compras-aguardando`
  Fila de compras realizada aguardando processamento do pagamento

- fila-entrada:  `fila-compras-finalizado`
  Fila de 
