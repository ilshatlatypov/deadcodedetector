package com.aurea.deadcode.service.flow;

import com.aurea.deadcode.model.GitHubRepo;
import com.aurea.deadcode.repository.RepoRepository;
import com.aurea.deadcode.service.utils.AppFileUtils;
import com.aurea.deadcode.service.flow.message.RepoIdMessage;
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
        MessageHandlingException exception = message.getPayload();
        RepoIdMessage repoIdMessage = (RepoIdMessage) exception.getFailedMessage().getPayload();

        Long repoId = repoIdMessage.getRepoId();
        AppFileUtils.deleteRepositoryDir(repoId);

        GitHubRepo repo = repoRepository.findOne(repoId);
        repo.setFailed();
        repoRepository.save(repo);

        String errorMessage = String.format("Processing failed for repository %d", repoId);
        Throwable actualException = exception.getCause();
        LOGGER.error(errorMessage, actualException);
    }
}
