package com.aurea.deadcode.service.integration;

import org.springframework.stereotype.Component;

/**
 * Created by ilshat on 28.03.17.
 */
@Component
public class GitHubIntegrationServiceImpl implements GitHubIntegrationService {
    @Override
    public GitHubRepoPayload fetchRepository(GitHubRepoPayload repoPayload) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return repoPayload;
    }
}