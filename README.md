# GitHub repositories

## About The Project
This service operates as a RESTful API, and its purpose is to furnish data concerning user repositories using the GitHub public API. 
It enables users to obtain a list of repositories associated with a specified username, inclusive of details about their respective branches.

### Built With

- Spring Boot
- WebFlux
- Java 17
- Spock (tests)
- Docker

## Getting Started

### Prerequisites

Before You start installation please check if You have `Java 17` installation.

### Higher requests rate to GitHub API
By default, this application will use unauthenticated requests to the GitHub API. Unauthenticated requests are subject to a lower rate limit, as explained in the [GitHub API Rate Limit documentation](https://docs.github.com/rest/overview/resources-in-the-rest-api#rate-limiting).

If this application needs to handle a higher request rate to the GitHub API, it is recommended to configure authentication with access token.
More information can be found here -> https://docs.github.com/en/rest/guides/getting-started-with-the-rest-api?apiVersion=2022-11-28.

Access token should be set as property in `application.yaml` config -> 'github-api.token'.

### Installation and running

1. Unpack repository from zip file
2. (optional) Set access token in application.yaml file
3. Build & run the application using the Maven wrapper: `./mvnw spring-boot:run`.

### Links
* Local service URL: http://localhost:8080
* Swagger UI: http://localhost:8080/webjars/swagger-ui/index.html


