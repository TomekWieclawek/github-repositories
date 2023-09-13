package com.tui.github.repository

import com.tui.github.repository.service.GitHubRepositoryService
import org.spockframework.util.Assert
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class GithubRepositoryApplicationSpec extends Specification {


    @Autowired
    GitHubRepositoryService service

    def 'app context is loaded'() {
        expect:
            Assert.notNull(service)
    }
}
