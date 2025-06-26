# Library Admin Panel

This project provides an admin-only Spring Boot application for managing books, authors, users and loans. It uses PostgreSQL for data storage.

## Running

1. Ensure PostgreSQL is running and create a database named `librarydb`.
2. Update `src/main/resources/application.properties` with your database credentials if they differ from the defaults. This file also contains `jwt.secret` used to sign tokens.
3. Build the application with Maven:

```bash
./mvnw package
```

4. Run the application:

```bash
java -jar target/Lista5-0.0.1-SNAPSHOT.jar
```

The application exposes CRUD endpoints under `/api`. Admins can manage books under `/api/books`, authors under `/api/authors`, users under `/api/users` and loans under `/api/loans`.

Swagger UI is available once the application is running at `http://localhost:8080/api/swagger-ui.html` for interactive API documentation.

### Web UI

The application is served under the `/api` path. A browser interface for administrators
is available at `/api/index.html`. Start the application and navigate to
`http://localhost:8080/api/` to log in and manage the library catalogue.

### Authentication

Authentication is performed via:

* `POST /api/auth/register` – create a new user account.
* `POST /api/auth/login` &ndash; authenticate with an administrator username and password. The response contains a JWT token.

Include this token in the `Authorization` header using the `Bearer` scheme when calling secured `/api` endpoints.

