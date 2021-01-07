# Microservices-Communication-API_Java
API com arquitetura em micro-serviços e comunicação entre módulos usando mensageria.

A proposta deste projeto é criar uma aplicação de aprendizagem com arquitetura em microserviços com 4 módulos comunicando entre si utilizando mensageria.

Serão utilizados para o projeto:

1. Java / Maven
2. Spring Boot / Spring Cloud
3. Docker
4. MySQL
5. RabbitMQ

### MYSQL
Na pasta docker foi criado os YAML para coposição das images. É necessário criar o database `banco`.
```shell
$ docker-compose -f docker-mysql.yml up -d
$ docker exec -it <nome_container> mysql -u root -p
$ root #password
$ create database banco;  
```

### LOMBOK DEPENDENCY
A dependencia do lombok é utilizada para gerar métodos, construtores, etc. através de notações (exemplo: `@Data`). É necessário instalar o projeto lombok no IDE que o mesmo reconheça os métodos que não são criados fisicamente no arquivo .java. 
- https://projectlombok.org/download
```shell
$ java -jar ~/Downloads/lombok.jar
```