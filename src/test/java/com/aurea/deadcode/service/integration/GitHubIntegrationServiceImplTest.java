package com.aurea.deadcode.service.integration;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by ilshat on 31.03.17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = GitHubIntegrationServiceImpl.class)
public class GitHubIntegrationServiceImplTest {

    @Autowired
    private GitHubIntegrationService gitHubIntegrationService;

    @Test
    public void fetchRepositorySources() throws Exception {
        String gitRepoUrl = "https://github.com/ilshatlatypov/test-github-service-clone.git";
        String sourcesDirPath = Files.createTempDir().getAbsolutePath();
        gitHubIntegrationService.fetchRepositorySources(gitRepoUrl, sourcesDirPath);

        File readme = new File(sourcesDirPath, "README.md");
        assertTrue(readme.exists());

        List<String> lines = Files.readLines(readme, Charsets.UTF_8);
        assertTrue(!lines.isEmpty());
        assertEquals("Repository for GitHub clone testing", lines.get(0));
    }

    @Test(expected = GitHubIntegrationException.class)
    public void throwsExceptionIfRepositoryDoesNotExist() throws Exception {
        String gitRepoUrl = "https://github.com/ilshatlatypov/not-existing-repo.git";
        String sourcesDirPath = Files.createTempDir().getAbsolutePath();
        gitHubIntegrationService.fetchRepositorySources(gitRepoUrl, sourcesDirPath);
    }
}