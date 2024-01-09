# Webflux API

## Description

This is a simple example of a Reactive Project using Kotlin and Spring Webflux to create, edit, delete and find an item.

This project also uses a PostgreSQL R2DBC Driver to support Reactive connection on the Database.

### Technical Info

* Technologies:
    * Kotlin
    * Spring Boot
    * Spring Webflux
    * PostgreSQL R2DBC
    * Docker Compose
    * Gradle
* Database:
    * PostgreSQL

## Building from Source

To clean/build the project from the console use the command:

```console
    gradle clean build
```

To run only tests from the console use the command:

```console
    gradle test
```

OBS: You can also use your IDE to run Gradle tasks.

## Running the project

The first thing you need to do is prepare your database.

You can do that by using docker compose on docker folder as detailed on `Using Docker Compose` session of this document.

With your database already prepared you just use the command below to Run the project:

```console
    gradle bootRun
```

OBS: You can also use your IDE to run the Project.

## Using Docker Compose

In Tab "Terminal" access the folder `docker` and type `docker-compose up -d {{service_name}}`.

After that, the service will be available in a docker container.

Useful Commands:
* Start Container: `docker-compose up -d {{service_name}}`
* Stop Container: `docker-compose down -d {{service_name}}`
* Logs: `docker-compose logs {{service_name}}`
* Logs in Real Time: `docker-compose logs -f {{service_name}}`

Service names available for this project:
* `postgres` (Database)
