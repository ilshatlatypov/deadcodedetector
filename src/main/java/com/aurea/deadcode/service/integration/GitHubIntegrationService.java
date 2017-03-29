package com.aurea.deadcode.service.integration;

import com.aurea.deadcode.service.integration.message.SourceCodeReadyMessage;

/**
 * Created by ilshat on 28.03.17.
 */
public interface GitHubIntegrationService {
    SourceCodeReadyMessage fetchRepositorySources(GitHubRepoPayload repoPayload);

    void deleteRepositorySources(Long repoId);
}
