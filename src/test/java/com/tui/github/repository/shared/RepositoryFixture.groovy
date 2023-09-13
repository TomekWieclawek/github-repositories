package com.tui.github.repository.shared

import com.tui.github.repository.model.Branch
import com.tui.github.repository.model.Repository
import com.tui.github.repository.service.client.model.GitHubBranchCommitDto
import com.tui.github.repository.service.client.model.GitHubBranchDto
import com.tui.github.repository.service.client.model.GitHubRepositoryDto
import com.tui.github.repository.service.client.model.GitHubRepositoryOwnerDto
import org.openapitools.model.GitHubBranchResponse
import org.openapitools.model.GitHubRepositoryResponse

interface RepositoryFixture {

    String USERNAME = "user"
    String REPOSITORY_A_NAME = "Repo_A"
    String REPOSITORY_B_NAME = "Repo_B"
    String BRANCH_A = "Branch_A"
    String BRANCH_B = "Branch_B"
    String COMMIT_SHA = "1"

    public List<GitHubRepositoryDto> CLIENT_REPOSITORY_DTO_LIST = List.of(
            GitHubRepositoryDto.builder()
                    .name(REPOSITORY_A_NAME)
                    .fork(false)
                    .owner(new GitHubRepositoryOwnerDto(USERNAME))
                    .build(),
            GitHubRepositoryDto.builder()
                    .name(REPOSITORY_B_NAME)
                    .fork(true)
                    .owner(new GitHubRepositoryOwnerDto(USERNAME))
                    .build()
    )

    public List<GitHubBranchDto> CLIENT_BRANCH_DTO_LIST = List.of(
            GitHubBranchDto.builder()
                    .name(BRANCH_A)
                    .commit(new GitHubBranchCommitDto(COMMIT_SHA))
                    .build(),
            GitHubBranchDto.builder()
                    .name(BRANCH_B)
                    .commit(new GitHubBranchCommitDto(COMMIT_SHA))
                    .build()
    )

    public List<Repository> SERVICE_REPOSITORY_LIST = List.of(
            Repository.builder()
                    .name(REPOSITORY_A_NAME)
                    .ownerLogin(USERNAME)
                    .branches(List.of(
                            Branch.builder()
                                    .name(BRANCH_A)
                                    .lastCommitSha(COMMIT_SHA)
                                    .build(),
                            Branch.builder()
                                    .name(BRANCH_B)
                                    .lastCommitSha(COMMIT_SHA)
                                    .build()))
                    .build())


    public List<GitHubRepositoryResponse> CONTROLLER_REPOSITORY_LIST = List.of(
            new GitHubRepositoryResponse()
                    .name(REPOSITORY_A_NAME)
                    .ownerLogin(USERNAME)
                    .addBranchesItem(new GitHubBranchResponse()
                            .name(BRANCH_A)
                            .lastCommitSha(COMMIT_SHA))
                    .addBranchesItem(new GitHubBranchResponse()
                            .name(BRANCH_B)
                            .lastCommitSha(COMMIT_SHA)))

}
