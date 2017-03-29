package com.aurea.deadcode.service.integration;

/**
 * Created by ilshat on 28.03.17.
 */
public interface GitHubIntegrationService {
    void fetchRepositorySources(String repoUrl, String sourcesDirPath);
}
