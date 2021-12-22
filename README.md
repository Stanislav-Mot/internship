
# REST CRUD API with Spring and Postgres For 'task tracker'

## Steps to Setup

**1. Clone the application**

```bash
https://github.com/DeniskaButkevich/internship
```

**2. Create Postgres database**
```bash
create database internship
```

**3. Change Postgres username and password as per your installation**

+ open `src/main/resources/application.properties`

+ change `spring.datasource.username` and `spring.datasource.password` as per your Postgres installation

**4. Build and run the app using maven**

```bash
mvn package
java -jar target/internship-0.0.1-SNAPSHOT.jar

```

Alternatively, you can run the app without packaging it using -

```bash
mvn spring-boot:run
```

The app will start running at <http://localhost:8080>.

## Explore Swagger Rest APIs

You can see the app defines following CRUD APIs with a Swagger.

http://localhost:8080/swagger-ui/index.html


