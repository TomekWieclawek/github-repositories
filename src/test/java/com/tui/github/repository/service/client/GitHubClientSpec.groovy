package com.tui.github.repository.service.client

import com.tui.github.repository.exception.NotFoundException
import com.tui.github.repository.service.client.model.GitHubBranchCommitDto
import com.tui.github.repository.service.client.model.GitHubBranchDto
import com.tui.github.repository.service.client.model.GitHubRepositoryDto
import com.tui.github.repository.service.client.model.GitHubRepositoryOwnerDto
import com.tui.github.repository.shared.RepositoryFixture
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import reactor.test.StepVerifier
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Path

import static com.tui.github.repository.shared.RepositoryFixture.*

class GitHubClientSpec extends Specification {

    MockWebServer mockWebServer
    GitHubClient gitHubClient

    def setup() {
        mockWebServer = new MockWebServer();
        mockWebServer.start()

        GitHubClientProperties properties = GitHubClientProperties.builder()
                .url(mockWebServer.url("/").toString())
                .build()

        gitHubClient = new GitHubClient(properties);
    }

    def cleanup() {
        mockWebServer.shutdown()
    }

    def 'should get repositories for given user '(){
        given:
            List<GitHubRepositoryDto> expectedRepositories = List.of(
                    new GitHubRepositoryDto(REPOSITORY_A_NAME, new GitHubRepositoryOwnerDto(USERNAME), false),
                    new GitHubRepositoryDto(REPOSITORY_B_NAME, new GitHubRepositoryOwnerDto(USERNAME), true))

            mockWebServer.enqueue(new MockResponse()
                        .setResponseCode(HttpStatus.OK.value())
                        .setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .setBody(readFile("client-github-repositories.json")))

        when:
            def result = gitHubClient.getGitHubRepositories(USERNAME)

        then:
            StepVerifier.create(result)
                    .expectNextSequence(expectedRepositories)
                    .verifyComplete()
    }

    def 'should get branches for given repository'(){
        given:
            List<GitHubBranchDto> expectedRepositories = List.of(
                    new GitHubBranchDto(BRANCH_A, new GitHubBranchCommitDto(COMMIT_SHA)),
                    new GitHubBranchDto(BRANCH_B, new GitHubBranchCommitDto(COMMIT_SHA)))

            mockWebServer.enqueue(new MockResponse()
                        .setResponseCode(HttpStatus.OK.value())
                        .setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .setBody(readFile("client-github-branches.json")))

        when:
            def result = gitHubClient.getGitHubBranches(USERNAME, REPOSITORY_A_NAME)

        then:
            StepVerifier.create(result)
                    .expectNextSequence(expectedRepositories)
                    .verifyComplete()
    }

    def 'should throw NotFoundException when user not found'() {
        given:
            mockWebServer.enqueue(new MockResponse()
                    .setResponseCode(HttpStatus.NOT_FOUND.value()))

        when:
            def branches = gitHubClient
                    .getGitHubRepositories(RepositoryFixture.USERNAME)

        then:
            StepVerifier.create(branches)
                    .expectError(NotFoundException.class)
                    .verify()
    }

    def 'should throw NotFoundException when branch not found'() {
        given:
            mockWebServer.enqueue(new MockResponse()
                    .setResponseCode(HttpStatus.NOT_FOUND.value()))

        when:
            def branches = gitHubClient
                    .getGitHubBranches(RepositoryFixture.USERNAME, RepositoryFixture.REPOSITORY_A_NAME)

        then:
            StepVerifier.create(branches)
                    .expectError(NotFoundException.class)
                    .verify()
    }


    String readFile(String fileName) throws IOException {
        def resources = Path.of("", "src/test/resources")
        def file = resources.resolve(fileName)
        return Files.readString(file)
    }

}
