package com.tui.github.repository.model;

import lombok.Builder;

import java.util.List;

@Builder
public record Repository(String name,
                         List<Branch> branches,
                         String ownerLogin) {
}
