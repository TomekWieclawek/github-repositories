package com.tui.github.repository.service.client.model;

import lombok.Builder;

@Builder
public record GitHubBranchDto(String name,
                              GitHubBranchCommitDto commit) {
}
