package com.tui.github.repository.service;

import com.tui.github.repository.model.Branch;
import com.tui.github.repository.model.Repository;
import com.tui.github.repository.service.client.GitHubClient;
import com.tui.github.repository.service.client.model.GitHubBranchDto;
import com.tui.github.repository.service.client.model.GitHubRepositoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class GitHubRepositoryService {

    /**
     * Setting maximal 10 http requests in parallel - default is 256
     */
    public static final int CONCURRENCY = 10;
    private final GitHubClient githubClient;

    public Flux<Repository> getRepositories(String username) {
        Flux<GitHubRepositoryDto> gitHubRepositories = githubClient.getGitHubRepositories(username);
        return gitHubRepositories
                .filter(repository -> !repository.fork())
                .flatMap(repository ->
                        githubClient.getGitHubBranches(username, repository.name())
                                .collectList()
                                .map(toRepository(repository)), CONCURRENCY);
    }

    private Function<List<GitHubBranchDto>, Repository> toRepository(GitHubRepositoryDto repositoryDto) {
        return branches -> Repository.builder()
                .name(repositoryDto.name())
                .ownerLogin(getLogin(repositoryDto))
                .branches(toBranches(branches))
                .build();
    }

    private String getLogin(GitHubRepositoryDto repositoryDto) {
        return repositoryDto.owner() != null ? repositoryDto.owner().login() : null;
    }

    private List<Branch> toBranches(List<GitHubBranchDto> branches) {
        return branches.stream()
                .map(branch -> Branch.builder()
                        .name(branch.name())
                        .lastCommitSha(getSha(branch))
                        .build())
                .toList();
    }

    private String getSha(GitHubBranchDto branch) {
        return branch != null ? branch.commit().sha() : null;
    }
}
