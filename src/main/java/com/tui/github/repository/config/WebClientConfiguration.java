package com.tui.github.repository.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

public class WebClientConfiguration {

    // #workaround: for first call webclient is getting connection reset. Following requests are fine. Implemented fix from
    // https://jskim1991.medium.com/spring-boot-how-to-solve-webclient-connection-reset-by-peer-error-b1fa38e4106a
    @Bean
    @Primary
    WebClient.Builder webClientBuilder(WebClient.Builder builder) {
        var httpClient = HttpClient.newConnection().keepAlive(false);
        var reactorClientHttpConnector = new ReactorClientHttpConnector(httpClient);

       return builder.clientConnector(reactorClientHttpConnector);
    }
}
