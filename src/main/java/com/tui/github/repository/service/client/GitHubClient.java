package com.tui.github.repository.service.client;

import com.tui.github.repository.exception.NotFoundException;
import com.tui.github.repository.service.client.model.GitHubBranchDto;
import com.tui.github.repository.service.client.model.GitHubRepositoryDto;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class GitHubClient {

    private static final String API_VERSION_HEADER = "X-GitHub-Api-Version";
    private final WebClient webClient;

    public GitHubClient(GitHubClientProperties properties) {
        webClient =  WebClient.builder()
                .baseUrl(properties.url())
                .defaultHeader(API_VERSION_HEADER, properties.version())
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeaders(h -> {
                    if(Strings.isNotBlank(properties.token())) {
                        h.setBearerAuth(properties.token());
                    }})
                .build();
    }

    public Flux<GitHubRepositoryDto> getGitHubRepositories(final String username) {
        return webClient.get()
                .uri("/users/{username}/repos", username)
                .retrieve()
                .onStatus(
                        HttpStatus.NOT_FOUND::equals,
                        error -> Mono.error(
                                new NotFoundException(String.format("User %s not found", username))
                        )
                )
                .bodyToFlux(GitHubRepositoryDto.class);
    }


    public Flux<GitHubBranchDto> getGitHubBranches(final String username, final String repository) {
        return webClient.get()
                .uri("/repos/{username}/{repositoryName}/branches", username, repository)
                .retrieve()
                .onStatus(
                        HttpStatus.NOT_FOUND::equals,
                        error -> Mono.error(
                                new NotFoundException(
                                        String.format("Branch for user %s and repository %s not found", username, repository)
                                )
                        )
                )
                .bodyToFlux(GitHubBranchDto.class);
    }
}
