package com.aurea.deadcode.model;

import org.junit.Test;

/**
 * Created by ilshat on 27.03.17.
 */
public class GitHubRepoTest {

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotCreateRepoWithoutName() throws Exception {
        GitHubRepoURL repoURL = buildValidRepoURL();
        new GitHubRepo(null, repoURL);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotCreateRepoWithBlankName() throws Exception {
        GitHubRepoURL repoURL = buildValidRepoURL();
        new GitHubRepo("", repoURL);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotCreateRepoWithoutURL() throws Exception {
        new GitHubRepo("Repository Name", null);
    }

    @Test
    public void shouldCreateRepoWithValidParams() throws Exception {
        String repoName = "Test Repo";
        GitHubRepoURL repoURL = buildValidRepoURL();
        new GitHubRepo(repoName, repoURL);
    }

    private static GitHubRepoURL buildValidRepoURL() {
        return new GitHubRepoURL("https://github.com/ilshatlatypov/uni-validator.git");
    }
}