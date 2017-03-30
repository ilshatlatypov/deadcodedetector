package com.aurea.deadcode.service.flow;

import com.aurea.deadcode.model.GitHubRepo;
import com.aurea.deadcode.repository.RepoRepository;
import com.aurea.deadcode.service.AppFileUtils;
import com.aurea.deadcode.service.flow.message.OccurrencesSavedMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
        AppFileUtils.deleteRepositoryDir(repoId);
        markRepoWithCompletedStatus(repoId);
    }

    private void markRepoWithCompletedStatus(Long repoId) {
        GitHubRepo repo = repoRepository.findOne(repoId);
        repo.setCompleted();
        repoRepository.save(repo);
    }
}
