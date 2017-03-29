package com.aurea.deadcode.service.integration;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

/**
 * Created by ilshat on 28.03.17.
 */
@Component
public class GitHubIntegrationServiceImpl implements GitHubIntegrationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GitHubIntegrationServiceImpl.class);

    @Override
    public void fetchRepositorySources(String repoUrl, String sourcesDirPath) {
        File repoDir = new File(sourcesDirPath);
        if (repoDir.exists()) {
            deleteDir(repoDir);
        }
        createDir(repoDir);
        cloneRepository(repoUrl, repoDir);
    }

    // TODO to utils
    private void createDir(File dir) {
        LOGGER.debug("Creating directory " + dir);
        try {
            FileUtils.forceMkdir(dir);
        } catch (IOException e) {
            String message = String.format("Could not create directory %s", dir.getAbsolutePath());
            throw new RuntimeException(message, e);
        }
    }

    // TODO to utils
    private void deleteDir(File dir) {
        LOGGER.debug("Deleting directory " + dir);
        try {
            FileUtils.deleteDirectory(dir);
        } catch (IOException e) {
            String message = String.format("Could not delete directory %s", dir.getAbsolutePath());
            throw new RuntimeException(message);
        }
    }

    private void cloneRepository(String uri, File dir) {
        LOGGER.debug(String.format("Cloning repository %s to directory %s", uri, dir));
        try {
            Git.cloneRepository()
                    .setURI(uri)
                    .setDirectory(dir)
                    .call();
        } catch (GitAPIException e) {
            String message = String.format("Could not clone repository %s to directory %s", uri, dir.getAbsolutePath());
            throw new RuntimeException(message);
        }
    }
}