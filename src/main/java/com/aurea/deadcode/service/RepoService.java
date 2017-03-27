package com.aurea.deadcode.service;

import com.aurea.deadcode.dto.GitHubRepoDetailedDTO;
import com.aurea.deadcode.dto.GitHubRepoDTO;

import java.util.List;

/**
 * Created by ilshat on 27.03.17.
 */
public interface RepoService {

    Long addNewRepo(GitHubRepoDTO repoDTO);

    List<GitHubRepoDetailedDTO> listRepos();
}
