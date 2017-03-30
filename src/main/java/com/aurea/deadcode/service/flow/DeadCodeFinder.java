package com.aurea.deadcode.service.flow;

import com.aurea.deadcode.model.CodeOccurrence;
import com.aurea.deadcode.model.GitHubRepo;
import com.aurea.deadcode.repository.CodeOccurrenceRepository;
import com.aurea.deadcode.repository.RepoRepository;
import com.aurea.deadcode.service.flow.message.OccurrencesSavedMessage;
import com.aurea.deadcode.service.flow.message.UdbFileReadyMessage;
import com.aurea.deadcode.service.integration.UnderstandIntegrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by ilshat on 29.03.17.
 */
@Component
public class DeadCodeFinder {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeadCodeFinder.class);

    @Autowired
    private UnderstandIntegrationService understandIntegrationService;
    @Autowired
    private CodeOccurrenceRepository codeOccurrenceRepository;
    @Autowired
    private RepoRepository repoRepository;

    public OccurrencesSavedMessage findAndSave(UdbFileReadyMessage message) {
        Long repoId = message.getRepoId();
        String udbFilePath = message.getUdbFilePath();
        LOGGER.debug(String.format("Searching for dead code occurrences for repository %s based using UDB file %s",
                repoId, udbFilePath));

        List<CodeOccurrence> deadCodeOccurrences =
                understandIntegrationService.searchForDeadCodeOccurrences(udbFilePath);

        updateCodeOccurrences(repoId, deadCodeOccurrences);

        LOGGER.debug("Dead code occurrences search finished for repository with id " +  repoId);
        return new OccurrencesSavedMessage(repoId);
    }

    @Transactional
    public void updateCodeOccurrences(Long repoId, List<CodeOccurrence> deadCodeOccurrences) {
        // there is a way to optimizations by calculating diff and committing only that occurrences
        GitHubRepo repo = repoRepository.findOne(repoId);
        deadCodeOccurrences.forEach(o -> o.setRepo(repo));
        codeOccurrenceRepository.deleteCodeOccurrencesByGitHubRepo(repo);
        codeOccurrenceRepository.save(deadCodeOccurrences);
    }
}
