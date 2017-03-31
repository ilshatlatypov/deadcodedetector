package com.aurea.deadcode.service;

import com.aurea.deadcode.dto.GitHubRepoDTO;
import com.aurea.deadcode.dto.GitHubRepoDetailedDTO;
import com.aurea.deadcode.model.CodeOccurrence;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by ilshat on 27.03.17.
 */
public interface RepoService {

    Long addNewRepo(GitHubRepoDTO repoDTO);

    void removeRepo(Long id);

    Page<GitHubRepoDetailedDTO> listRepos(Pageable pageable);

    void startProcessing(Long id);

    Page<CodeOccurrence> getDeadCodeOccurrences(Long repoId, Pageable pageable);
}
