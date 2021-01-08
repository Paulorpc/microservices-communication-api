# Microservices-Communication-API_Java
API com arquitetura em micro-serviços e comunicação entre módulos usando mensageria.

A proposta deste projeto é criar uma aplicação de aprendizagem com arquitetura em microserviços com 4 módulos comunicando entre si utilizando mensageria.

Serão utilizados para o projeto:

1. Java / Maven
2. Spring Boot / Spring Cloud
3. Docker (docker-compose)
4. MySQL
5. RabbitMQ

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
- fila-entrada: 
