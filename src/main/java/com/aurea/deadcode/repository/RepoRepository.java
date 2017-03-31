package com.aurea.deadcode.repository;

import com.aurea.deadcode.model.GitHubRepo;
import com.aurea.deadcode.model.GitHubRepoURL;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by ilshat on 27.03.17.
 */
public interface RepoRepository extends PagingAndSortingRepository<GitHubRepo, Long> {
    GitHubRepo findByUrl(GitHubRepoURL url);
}
