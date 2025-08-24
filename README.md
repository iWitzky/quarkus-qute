# quarkus-qute

Demo of using Quarkus with server-side templates via its Qute extension and Unpoly for client-side enhancements.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/.

This project is based on the [quarkus-qute](https://github.com/gunnarmorling/quarkus-qute) project and blog post [Quarkus Qute – A Test Ride](https://www.morling.dev/blog/quarkus-qute-test-ride/) by Gunnar Morling.  
The project is updated to the latest Quarkus and Unpoly version.  
The project is extended with OpenID Connect authentication, Flyway schema management and Hibernate MultiTenancy.  

## Database / Keycloak set-up

This project uses Postgres and Keycloak which can be started via Docker Compose:

```shell
cd compose
docker-compose up
```

## Unpoly

This project uses [Unpoly](https://unpoly.com/) for a smoother user experience:
links and form submissions will be intercepted and executed as AJAX requests,
avoiding a full page reload by replacing page fragments.

If JavaScript is disabled, the application gracefully falls back to the regular mode and experience of server-side rendered applications.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```
./mvnw quarkus:dev
```

Then open the application in your browser at http://localhost:8080/todo.

## User authentication with OpenID Connect and Keycloak

The application uses Keycloak for user authentication.  
Administration credentials are: `admin` / `admin`.  
Admin console is available at http://localhost:8180/auth/.

Available users: `alice`, `bob`  
The username is used as password.

Use http://localhost:8080/tokens and https://jwt.io/ to debug the tokens.

Based on the Quarkus guide [Protect a web application by using OpenID Connect (OIDC) authorization code flow](https://quarkus.io/guides/security-oidc-code-flow-authentication-tutorial#write-the-application).

## Hibernate MultiTenancy
The TenantId is extracted from the IdToken and used to select the correct discriminator for the Todo entity.

To use multitenancy, annotate the Todo entity field for the discriminator with '@TenantId' and enable multitenancy with 'quarkus.hibernate-orm.multitenant=DISCRIMINATOR' in the 'application.properties'.

```
    @TenantId
    public String tenantId;
```

Based on the blog post [The Tenant Chronicles – Building a Multi-Tenant Todo App with Quarkus](https://www.the-main-thread.com/p/quarkus-multi-tenant-todo-java-hibernate) by Markus Eisele.

## Packaging and running the application

The application is packageable using `./mvnw package`.
It produces the executable `quarkus-qute-1.0.0-SNAPSHOT-runner.jar` file in `/target` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/lib` directory.

The application is now runnable using `java -jar target/quarkus-qute-1.0.0-SNAPSHOT-runner.jar`.

## Creating a native executable

You can create a native executable using: `./mvnw package -Pnative`.

Or you can use Docker to build the native executable using: `./mvnw package -Pnative -Dquarkus.native.container-build=true`.

You can then execute your binary: `./target/quarkus-qute-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/building-native-image-guide .

## Running the native executable via Docker

```
docker build -f src/main/docker/Dockerfile.native -t quarkus-examples/quarkus-qute .
docker run -i --rm -p 8080:8080 --network todo-network -e QUARKUS_DATASOURCE_URL=jdbc:postgresql://todo-db:5432/tododb quarkus-examples/quarkus-qute
```

## License

This code base is available under the Apache License, version 2.
