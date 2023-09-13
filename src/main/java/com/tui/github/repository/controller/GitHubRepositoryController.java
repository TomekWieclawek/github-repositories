package com.tui.github.repository.controller;

import com.tui.github.repository.controller.mapper.RepositoryMapper;
import com.tui.github.repository.service.GitHubRepositoryService;
import lombok.RequiredArgsConstructor;
import org.openapitools.api.GitHubRepositoryControllerApi;
import org.openapitools.model.GitHubRepositoryResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequiredArgsConstructor
public class GitHubRepositoryController implements GitHubRepositoryControllerApi {

    private final GitHubRepositoryService githubRepositoryService;

    private final RepositoryMapper mapper;

    @Override
    public Mono<ResponseEntity<Flux<GitHubRepositoryResponse>>> getGithubRepositoriesByUsername(String username,
                                                                                                ServerWebExchange exchange) {
        Flux<GitHubRepositoryResponse> repositories = githubRepositoryService.getRepositories(username)
                .map(mapper::toRepositoryResponse);

        return Mono.just(ResponseEntity.ok(repositories));
    }
}
