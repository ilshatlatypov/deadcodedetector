package com.aurea.deadcode.service.integration;

import com.aurea.deadcode.DeadCodeDetectorApplication;
import com.aurea.deadcode.service.integration.message.SourceCodeReadyMessage;
import com.aurea.deadcode.service.integration.message.UdbFileReadyMessage;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by ilshat on 29.03.17.
 */
@Component
public class UdbFileBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(UdbFileBuilder.class);
    private static final String UDB_FILES_DIR_NAME = "udb";
    private static final String UDB_FILE_NAME = "project.udb";

    @Autowired
    private UnderstandIntegrationService understandIntegrationService;

    public UdbFileReadyMessage build(SourceCodeReadyMessage message) {
        Long repoId = message.getRepoId();
        LOGGER.debug("Building UDB file for repository with id " + repoId);

        File udbDir = getUdbDirForRepo(repoId);
        createDir(udbDir);

        String udbFilePath = udbDir.getAbsolutePath() + "/" + UDB_FILE_NAME;
        String sourcesDirPath = message.getSourcesDirPath();

        try {
            File udbFile = understandIntegrationService.createUdbFile(udbFilePath, sourcesDirPath);
            LOGGER.debug("UDB file building finished for with id " + repoId);
            return new UdbFileReadyMessage(udbFile.getAbsolutePath(), repoId);
        } catch (FileNotFoundException e) {
            String errorMessage = String.format("Could not create UDB file %s for sources directory %s",
                    udbFilePath, sourcesDirPath);
            throw new RuntimeException(errorMessage, e);
        }
    }

    // TODO duplication
    private File getUdbDirForRepo(Long repoId) {
        String dirName = DeadCodeDetectorApplication.ROOT + "/" + repoId + "/" + UDB_FILES_DIR_NAME;
        return new File(dirName);
    }

    // TODO duplication
    private void createDir(File dir) {
        LOGGER.debug("Creating directory " + dir);
        try {
            FileUtils.forceMkdir(dir);
        } catch (IOException e) {
            String message = String.format("Could not create directory %s", dir.getAbsolutePath());
            throw new RuntimeException(message, e);
        }
    }
}
