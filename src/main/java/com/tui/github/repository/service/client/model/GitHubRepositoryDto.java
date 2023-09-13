package com.tui.github.repository.service.client.model;

import lombok.Builder;

@Builder
public record GitHubRepositoryDto(String name,
                                  GitHubRepositoryOwnerDto owner,
                                  boolean fork) {
}
