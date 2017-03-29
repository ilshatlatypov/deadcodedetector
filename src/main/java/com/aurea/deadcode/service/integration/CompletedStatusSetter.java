package com.aurea.deadcode.service.integration;

import com.aurea.deadcode.model.GitHubRepo;
import com.aurea.deadcode.repository.RepoRepository;
import com.aurea.deadcode.service.integration.message.OccurrencesSavedMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by ilshat on 28.03.17.
 */
@Component
public class CompletedStatusSetter {

    @Autowired
    private RepoRepository repoRepository;

    public void setCompletedStatus(OccurrencesSavedMessage message) {
        Long repoId = message.getId();
        GitHubRepo repo = repoRepository.findOne(repoId);
        repo.setCompleted();
        repoRepository.save(repo);
    }
}
