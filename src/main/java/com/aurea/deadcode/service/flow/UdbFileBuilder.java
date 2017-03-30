package com.aurea.deadcode.service.flow;

import com.aurea.deadcode.service.utils.AppFileUtils;
import com.aurea.deadcode.service.flow.message.SourceCodeReadyMessage;
import com.aurea.deadcode.service.flow.message.UdbFileReadyMessage;
import com.aurea.deadcode.service.integration.UnderstandIntegrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by ilshat on 29.03.17.
 */
@Component
public class UdbFileBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(UdbFileBuilder.class);
    private static final String UDB_FILE_NAME = "project.udb";

    @Autowired
    private UnderstandIntegrationService understandIntegrationService;

    public UdbFileReadyMessage build(SourceCodeReadyMessage message) {
        Long repoId = message.getRepoId();
        LOGGER.debug("Building UDB file for repository with id " + repoId);

        File udbDir = AppFileUtils.getUdbDirForRepository(repoId);
        if (udbDir.exists()) {
            AppFileUtils.deleteDirRecursively(udbDir);
        }
        AppFileUtils.createDir(udbDir);

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
}
