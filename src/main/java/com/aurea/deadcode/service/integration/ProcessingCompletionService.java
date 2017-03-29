package com.aurea.deadcode.service.integration;

import com.aurea.deadcode.DeadCodeDetectorApplication;
import com.aurea.deadcode.model.GitHubRepo;
import com.aurea.deadcode.repository.RepoRepository;
import com.aurea.deadcode.service.integration.message.OccurrencesSavedMessage;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

/**
 * Created by ilshat on 28.03.17.
 */
@Component
public class ProcessingCompletionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessingCompletionService.class);

    @Autowired
    private RepoRepository repoRepository;

    public void performCompletionOperations(OccurrencesSavedMessage message) {
        Long repoId = message.getId();
        deleteRepoDir(repoId);
        markRepoWithCompletedStatus(repoId);
    }

    private void deleteRepoDir(Long repoId) {
        File dir = getRepositoryDir(repoId);
        deleteDir(dir);
    }

    private void markRepoWithCompletedStatus(Long repoId) {
        GitHubRepo repo = repoRepository.findOne(repoId);
        repo.setCompleted();
        repoRepository.save(repo);
    }

    // TODO duplication
    private File getRepositoryDir(Long repoId) {
        String dirName = DeadCodeDetectorApplication.ROOT + "/" + repoId;
        return new File(dirName);
    }

    // TODO duplication
    private void deleteDir(File dir) {
        LOGGER.debug("Deleting directory " + dir);
        try {
            FileUtils.deleteDirectory(dir);
        } catch (IOException e) {
            String message = String.format("Could not delete directory %s", dir.getAbsolutePath());
            throw new RuntimeException(message);
        }
    }
}
