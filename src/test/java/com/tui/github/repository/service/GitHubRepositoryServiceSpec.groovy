package com.tui.github.repository.service


import com.tui.github.repository.model.Repository
import com.tui.github.repository.service.client.GitHubClient
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import spock.lang.Specification

import static com.tui.github.repository.shared.RepositoryFixture.*

class GitHubRepositoryServiceSpec extends Specification {

    GitHubClient githubClient = Mock(GitHubClient)
    GitHubRepositoryService service = new GitHubRepositoryService(githubClient)

    def 'should return repository with branches'() {
        given: '2 repositories (one is forked)'
            githubClient.getGitHubRepositories(USERNAME) >> Flux.fromIterable(CLIENT_REPOSITORY_DTO_LIST)

        and: 'not forked repository contains two branches'
            githubClient.getGitHubBranches(USERNAME, REPOSITORY_A_NAME) >> Flux.fromIterable(CLIENT_BRANCH_DTO_LIST)

        when: 'service is asked for repositories for given user'
            Flux<Repository> result = service.getRepositories(USERNAME)

        then: 'only one repository with branches is returned'
            StepVerifier.create(result)
                    .expectNextSequence(SERVICE_REPOSITORY_LIST)
                    .expectComplete()
    }

    def 'should not return repository if branch request will fail'() {
        given: 'there are 2 repositories (one is forked)'
            githubClient.getGitHubRepositories(USERNAME) >> Flux.fromIterable(CLIENT_REPOSITORY_DTO_LIST)

        and: 'and there is connection issue with branches endpoint'
            githubClient.getGitHubBranches(USERNAME, REPOSITORY_A_NAME) >> { throw new ConnectException("") }

        when: 'service is asked for branch for given user'
            Flux<Repository> result = service.getRepositories(USERNAME)

        then: 'exception is returned and processing is stopped'
            StepVerifier.create(result)
                    .expectError(ConnectException.class)
    }
}
