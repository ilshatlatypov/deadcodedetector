package com.aurea.deadcode.service.flow;

import com.aurea.deadcode.service.utils.AppFileUtils;
import com.aurea.deadcode.service.flow.message.GitHubRepoPayload;
import com.aurea.deadcode.service.flow.message.SourceCodeReadyMessage;
import com.aurea.deadcode.service.integration.GitHubIntegrationService;
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

    @Autowired
    private GitHubIntegrationService gitHubIntegrationService;

    public SourceCodeReadyMessage download(GitHubRepoPayload repoPayload) {
        Long repoId = repoPayload.getRepoId();
        String repoUrl = repoPayload.getUrl();
        LOGGER.debug(String.format("Download sources for repository %s via URL %s", repoId, repoUrl));

        File sourcesDir = AppFileUtils.getSourceCodeDirForRepository(repoId);
        gitHubIntegrationService.fetchRepositorySources(repoUrl, sourcesDir.getAbsolutePath());

        LOGGER.debug("Sources downloading finished for repository " + repoId);
        return new SourceCodeReadyMessage(sourcesDir.getAbsolutePath(), repoId);
    }
}
