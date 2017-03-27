package com.aurea.deadcode.model;

import org.junit.Test;

/**
 * Created by ilshat on 27.03.17.
 */
public class GitHubRepoURLTest {
    @Test(expected = IllegalArgumentException.class)
    public void urlStringCannotBeNull() throws Exception {
        new GitHubRepoURL(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void plainStringsAreNotValidGitHubRepositoryURL() throws Exception {
        new GitHubRepoURL("plainstring");
    }

    @Test(expected = IllegalArgumentException.class)
    public void validURLButNotValidGitHubRepositoryURL() throws Exception {
        new GitHubRepoURL("https://geektimes.com/");
    }

    @Test(expected = IllegalArgumentException.class)
    public void validGitHubURLButNotValidGitHubRepositoryURL() throws Exception {
        new GitHubRepoURL("https://github.com/ilshatlatypov/");
    }

    @Test
    public void validGitHubRepositoryURL() throws Exception {
        new GitHubRepoURL("https://github.com/ilshatlatypov/uni-validator.git");
    }
}