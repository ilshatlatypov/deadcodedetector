package com.aurea.deadcode.dto;

import com.aurea.deadcode.model.GitHubRepo;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Created by ilshat on 31.03.17.
 */
@Component
public class RepoToDetailedDtoConverter implements Converter<GitHubRepo, GitHubRepoDetailedDTO> {

    @Override
    public GitHubRepoDetailedDTO convert(GitHubRepo source) {
        return new GitHubRepoDetailedDTO(
                source.getId(),
                source.getName(),
                source.getUrl().toString(),
                source.getStatus(),
                source.getAddedDate(),
                source.getProcessingCompletionDate(),
                source.getProcessingFailureDate()
        );
    }
}
