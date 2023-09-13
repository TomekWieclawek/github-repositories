package com.tui.github.repository.model;

import lombok.Builder;

@Builder
public record Branch(String name,
                     String lastCommitSha) {
}
