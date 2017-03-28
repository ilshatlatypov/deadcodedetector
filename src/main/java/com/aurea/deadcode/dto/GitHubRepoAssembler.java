package com.aurea.deadcode.dto;

import com.aurea.deadcode.model.GitHubRepo;
import com.aurea.deadcode.model.GitHubRepoURL;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by ilshat on 27.03.17.
 */
@Component
public class GitHubRepoAssembler {

    public GitHubRepo fromDTO(GitHubRepoDTO repoDTO) {
        String name = repoDTO.getName();
        GitHubRepoURL url = new GitHubRepoURL(repoDTO.getUrl());
        return new GitHubRepo(name, url);
    }

    public List<GitHubRepoDetailedDTO> toListItemDTO(Iterable<GitHubRepo> repos) {
        return StreamSupport.stream(repos.spliterator(), false)
                .map(this::toListItemDTO)
                .collect(Collectors.toList());
    }

    private GitHubRepoDetailedDTO toListItemDTO(GitHubRepo repo) {
        return new GitHubRepoDetailedDTO(
                repo.getId(),
                repo.getName(),
                repo.getUrl().toString(),
                repo.getStatus(),
                repo.getAddedDate(),
                repo.getProcessingCompletionDate(),
                repo.getProcessingFailureDate()
        );
    }
}
