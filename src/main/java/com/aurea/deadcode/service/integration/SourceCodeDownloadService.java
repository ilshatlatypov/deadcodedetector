package com.aurea.deadcode.service.integration;

import com.aurea.deadcode.DeadCodeDetectorApplication;
import com.aurea.deadcode.service.integration.message.SourceCodeReadyMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * Created by ilshat on 29.03.17.
 */
@Component
public class SourceCodeDownloadService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SourceCodeDownloadService.class);
    private static final String SOURCES_DIR_NAME = "sources";

    @Autowired
    private GitHubIntegrationService gitHubIntegrationService;

    public SourceCodeReadyMessage download(GitHubRepoPayload repoPayload) {
        Long repoId = repoPayload.getId();
        String repoUrl = repoPayload.getUrl();
        LOGGER.debug(String.format("Download sources for repository %s via URL %s", repoId, repoUrl));

        File sourcesDir = getSourcesDirForRepo(repoPayload.getId());
        gitHubIntegrationService.fetchRepositorySources(repoUrl, sourcesDir.getAbsolutePath());

        LOGGER.debug("Sources downloading finished for repository " + repoId);
        return new SourceCodeReadyMessage(sourcesDir.getAbsolutePath(), repoId);
    }

    // TODO duplicated
    private File getSourcesDirForRepo(Long repoId) {
        String dirName = DeadCodeDetectorApplication.ROOT + "/" + repoId + "/" + SOURCES_DIR_NAME;
        return new File(dirName);
    }
}
