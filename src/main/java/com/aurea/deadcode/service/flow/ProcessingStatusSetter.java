package com.aurea.deadcode.service.flow;

import com.aurea.deadcode.model.GitHubRepo;
import com.aurea.deadcode.repository.RepoRepository;
import com.aurea.deadcode.service.flow.message.GitHubRepoPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by ilshat on 28.03.17.
 */
@Component
public class ProcessingStatusSetter {

    @Autowired
    private RepoRepository repoRepository;

    private Lock processingStatusLock = new ReentrantLock();

    public GitHubRepoPayload checkProcessingStatusAndSet(GitHubRepoPayload repoPayload) {
        boolean alreadyProcessing = false;
        processingStatusLock.lock();
        try {
            Long repoId = repoPayload.getRepoId();
            GitHubRepo repo = repoRepository.findOne(repoId);
            if (!repo.isProcessing()) {
                repo.setProcessing();
                repoRepository.save(repo);
            } else {
                alreadyProcessing = true;
            }
        } finally {
            processingStatusLock.unlock();
        }
        repoPayload.setAlreadyProcessing(alreadyProcessing);
        return repoPayload;
    }
}
