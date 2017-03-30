package com.aurea.deadcode.service.integration;

import com.aurea.deadcode.service.utils.AppFileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;

import static com.aurea.deadcode.service.utils.AppFileUtils.createDir;

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
            AppFileUtils.deleteDirRecursively(repoDir);
        }
        createDir(repoDir);
        cloneRepository(repoUrl, repoDir);
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
            throw new GitHubIntegrationException(message, e);
        }
    }
}