package com.aurea.deadcode.service;

import com.aurea.deadcode.dto.GitHubRepoDetailedDTO;
import com.aurea.deadcode.dto.GitHubRepoDTO;
import com.aurea.deadcode.model.CodeOccurrence;

import java.util.List;

public interface RepoService {

    Long addNewRepo(GitHubRepoDTO repoDTO);

    void removeRepo(Long id);

    List<GitHubRepoDetailedDTO> listRepos();

    void startProcessing(Long id);

    List<CodeOccurrence> getDeadCodeOccurrences(Long repoId);
}
