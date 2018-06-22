# Spring Data JPA, Spring Data Neo4J (SDN) with Spring Boot.


This example show you how to wire up a Spring Boot application with Spring Data JPA and Spring Data Neo4j running side by side.

## Dependencies

* Spring Data JPA
* Spring Data Neo4j
* Spring Boot 2.0.3.RELEASE
* Neo4j Graph Database 3.1.x 
* H2 SQL Database 1.4.x
* PostgreSQL Database 10.0.x

## Running the Application

Simply do:

```
./mvnw -Pdevelopment -Dspring.profiles.active=development clean spring-boot:run
```

or with Standalone Neo4J-Server and PostgreSQL 10 Server for JPA:

```
./mvnw-e -Pproduction -Dspring.profiles.active=production clean spring-boot:run
```

or run in your favourite IDE.

### Expected behaviour

When you run the Application it will load some data into both databases and query them. 

With Development Profile: As the databases are in memory you won't have to configure a database to run it.
