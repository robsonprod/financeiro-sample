# API REST
Somepay

Technology :
- [Java Platform (JDK) 11](https://openjdk.java.net/projects/jdk/11/)
- [Maven](https://maven.apache.org/)
- [PostgreSQL](https://www.postgresql.org/)
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring Security](https://spring.io/projects/spring-security)
- [Hibernate](https://hibernate.org/)
- [IntelliJ IDEA](https://www.jetbrains.com/pt-br/idea/)


Clone the repo:
``` bash
git clone https://github.com/robsonprod/financeiro-sample.git

```

Install dependencies:
``` bash
../mvnw spring-boot:run
```


Update Persistence Config:
``` bash
db.hostname=localhost
db.database=financeiro
db.username=postgres
db.password=123
```

Insert roles:
``` bash
insert into groups (id, role) values (nextval('seq_groups'), 'USER');
insert into groups (id, role) values (nextval('seq_groups'), 'ADMIN');
```

Api Endpoints:

``` bash
POST http://localhost:8080/api/v1/users/signIn
{
    "name":"",
    "email":"",
    "password:""
}
```

``` bash
POST http://localhost:8080/api/v1/authenticate
{
    "username":"",
    "password":""
}
```
With authorization token "Bearer" and hash

``` bash
POST http://localhost:8080/api/v1/empresa/
{
    "nome_fantasia":""
}
```

``` bash
POST http://localhost:8080/api/v1/funcionario
{
    "nome":"",
    "empresa_id":""
}
```

``` bash
POST http://localhost:8080/api/v1/empresa/depositar/{id}
{
    "saldo":""
}
```

``` bash
POST http://localhost:8080/api/v1/empresa/pagar-funcionario
{
    "empresa_id":"",
    "funcionario_id":"",
    "valor":""
}
```

``` bash
GET http://localhost:8080/api/v1/empresa/saldo/{id}

```

