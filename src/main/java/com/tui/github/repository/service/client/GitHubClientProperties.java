package com.tui.github.repository.service.client;

import lombok.Builder;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("github-api")
@Builder
public record GitHubClientProperties(String url,
                                     String version,
                                     String token) {
}
