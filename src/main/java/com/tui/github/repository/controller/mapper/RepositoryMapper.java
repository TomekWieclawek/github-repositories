package com.tui.github.repository.controller.mapper;

import com.tui.github.repository.model.Repository;
import org.mapstruct.Mapper;
import org.openapitools.model.GitHubRepositoryResponse;

@Mapper(componentModel = "spring")
public interface RepositoryMapper {
    GitHubRepositoryResponse toRepositoryResponse(Repository repository);
}
