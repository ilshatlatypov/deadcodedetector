package com.aurea.deadcode.service.integration;

import com.aurea.deadcode.DeadCodeDetectorApplication;
import com.aurea.deadcode.service.integration.message.SourceCodeReadyMessage;
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

    private static final String SOURCES_DIR_NAME = "sources";

    @Override
    public SourceCodeReadyMessage fetchRepositorySources(GitHubRepoPayload repoPayload) {
        LOGGER.debug("Fetch sources for repository " + repoPayload);

        Long repoId = repoPayload.getId();
        String repoUrl = repoPayload.getUrl();

        File repoDir = getSourcesDirForRepo(repoId);
        if (repoDir.exists()) {
            try {
                pullRepository(repoDir);
            } catch (IOException e) {
                deleteDir(repoDir);
                createDir(repoDir);
                cloneRepository(repoUrl, repoDir);
            }
        } else {
            createDir(repoDir);
            cloneRepository(repoUrl, repoDir);
        }
        LOGGER.debug("Fetching sources finished for repository " + repoPayload);
        return new SourceCodeReadyMessage(repoDir.getAbsolutePath(), repoId);
    }

    @Override
    public void deleteRepositorySources(Long repoId) {
         File dir = getSourcesDirForRepo(repoId);
         if (dir.exists()) {
             deleteDir(dir);
         }
    }

    private File getSourcesDirForRepo(Long repoId) {
        String dirName = DeadCodeDetectorApplication.ROOT + "/" + repoId + "/" + SOURCES_DIR_NAME;
        return new File(dirName);
    }

    private void createDir(File dir) {
        LOGGER.debug("Creating directory " + dir);
        try {
            FileUtils.forceMkdir(dir);
        } catch (IOException e) {
            String message = String.format("Could not create directory %s", dir.getAbsolutePath());
            throw new RuntimeException(message, e);
        }
    }

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

    private void pullRepository(File dir) throws IOException {
        LOGGER.debug("Pulling repository to directory " + dir);
        try {
            Git.open(dir)
                    .pull()
                    .call();
        } catch (GitAPIException e) {
            throw new RuntimeException("Could not pull repository to directory " + dir.getAbsolutePath());
        }
    }
}