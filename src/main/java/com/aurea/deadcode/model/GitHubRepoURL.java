package com.aurea.deadcode.model;

import org.apache.commons.lang3.Validate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ilshat on 27.03.17.
 */
@Embeddable
public class GitHubRepoURL {
    private static final Pattern GITHUB_REPO_URL_PATTERN = Pattern.compile("https://github\\.com/[^/]+/[^.]+.git");

    @Column(name = "url", nullable = false)
    private URL value;

    private GitHubRepoURL() {}

    public GitHubRepoURL(String repoURL) {
        Validate.isTrue(repoURL != null, "Repository URL cannot be null");
        Validate.notBlank(repoURL, "Repository URL cannot be blank");
        value = tryParseAsURL(repoURL);
        checkValidGitHubReposityURL(value);
    }

    private static URL tryParseAsURL(String repoURL) {
        try {
            return new URL(repoURL);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Not valid URL: " + repoURL);
        }
    }

    private static void checkValidGitHubReposityURL(URL value) {
        Matcher m = GITHUB_REPO_URL_PATTERN.matcher(value.toString());
        if (!m.matches()) {
            throw new IllegalArgumentException("Not a GitHub repository URL: " + value.toString());
        }
    }

    public String toString() {
        return value.toString();
    }
}
