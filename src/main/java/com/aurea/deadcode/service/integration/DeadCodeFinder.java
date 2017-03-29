package com.aurea.deadcode.service.integration;

import com.aurea.deadcode.model.CodeOccurrence;
import com.aurea.deadcode.service.integration.message.OccurrencesSavedMessage;
import com.aurea.deadcode.service.integration.message.UdbFileReadyMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Created by ilshat on 29.03.17.
 */
@Component
public class DeadCodeFinder {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeadCodeFinder.class);

    @Autowired
    private UnderstandIntegrationService understandIntegrationService;

    public OccurrencesSavedMessage findAndSave(UdbFileReadyMessage message) {
        Long repoId = message.getRepoId();
        String udbFilePath = message.getUdbFilePath();
        LOGGER.debug(String.format("Searching for dead code occurrences for repository %s based using UDB file %s",
                repoId, udbFilePath));

        Set<CodeOccurrence> deadCodeOccurrences =
                understandIntegrationService.searchForDeadCodeOccurrences(udbFilePath);
        // TODO save occurrences
        deadCodeOccurrences.forEach(System.out::println);


        LOGGER.debug("Dead code occurrences search finished for repository with id " +  repoId);
        return new OccurrencesSavedMessage(repoId);
    }
}
