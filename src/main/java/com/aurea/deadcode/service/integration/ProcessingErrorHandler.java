package com.aurea.deadcode.service.integration;

import com.aurea.deadcode.model.GitHubRepo;
import com.aurea.deadcode.repository.RepoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandlingException;

/**
 * Created by ilshat on 28.03.17.
 */
public class ProcessingErrorHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessingErrorHandler.class);

    @Autowired
    private RepoRepository repoRepository;

    public void handleFailure(Message<MessageHandlingException> message) {
        GitHubRepoPayload repoPayload = (GitHubRepoPayload) message.getPayload().getFailedMessage().getPayload();

        Long repoId = repoPayload.getId();
        GitHubRepo repo = repoRepository.findOne(repoId);
        repo.setFailed();
        repoRepository.save(repo);

        String reason = message.getPayload().getCause().getMessage();
        LOGGER.error("Processing failed for repository []. Reason: " + reason);
    }
}
