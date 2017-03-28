package com.aurea.deadcode.service.integration;

/**
 * Created by ilshat on 28.03.17.
 */
public interface GitHubIntegrationService {
    GitHubRepoPayload fetchRepositorySources(GitHubRepoPayload repoPayload);

    void deleteRepositorySources(Long repoId);
}
