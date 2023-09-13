package com.tui.github.repository.controller

import com.tui.github.repository.controller.mapper.RepositoryMapper
import com.tui.github.repository.controller.mapper.RepositoryMapperImpl
import com.tui.github.repository.exception.NotFoundException
import com.tui.github.repository.model.Repository
import com.tui.github.repository.service.GitHubRepositoryService
import com.tui.github.repository.shared.RepositoryFixture
import org.openapitools.model.ErrorResponse
import org.openapitools.model.GitHubRepositoryResponse
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux
import spock.lang.Specification

@WebFluxTest(GitHubRepositoryController.class)
class GitHubRepositoryControllerSpec extends Specification {

    static final String URL = "/api/v1/github/repositories/{username}"

    @Autowired
    WebTestClient client

    @SpringBean
    GitHubRepositoryService service = Mock()

    @SpringBean
    RepositoryMapper repositoryMapper = new RepositoryMapperImpl()

    def "should get list of repositories for given user"() {
        given: 'list of repositories'
            Flux<Repository> repositories = Flux.fromIterable(RepositoryFixture.SERVICE_REPOSITORY_LIST)
            List<GitHubRepositoryResponse> expectedResponse = RepositoryFixture.CONTROLLER_REPOSITORY_LIST

        when: 'list is returned from Github'
            service.getRepositories(RepositoryFixture.USERNAME) >> repositories

        then: 'the http response is 200 and body contains list of repositories'
            client.get()
                    .uri(URL, RepositoryFixture.USERNAME)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().contentType(MediaType.APPLICATION_JSON)
                    .expectBodyList(GitHubRepositoryResponse.class)
                    .isEqualTo(expectedResponse)

    }

    def "should return http code '404' code when user not found"() {
        given: 'a user'
            def user = RepositoryFixture.USERNAME

        and: 'expected error response'
            def errorMessage = "User not found!"
            def errorResponse = new ErrorResponse()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message(errorMessage)

        when: 'user does not exist in Github'
            service.getRepositories(user) >> { throw new NotFoundException(errorMessage) }

        then: 'the http response is 404 and body contains error response'
            client.get()
                    .uri(URL, user)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isNotFound()
                    .expectBody(ErrorResponse.class)
                    .isEqualTo(errorResponse)
    }

    def "should return http code '406' code when invalid 'accept' header provided"() {
        expect:
            client.get()
                    .uri(URL, RepositoryFixture.USERNAME)
                    .accept(MediaType.APPLICATION_XML)
                    .exchange()
                    .expectStatus().isEqualTo(HttpStatus.NOT_ACCEPTABLE)
    }
}
