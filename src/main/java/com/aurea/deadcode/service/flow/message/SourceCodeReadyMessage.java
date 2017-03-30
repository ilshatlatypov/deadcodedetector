package com.aurea.deadcode.service.flow.message;

/**
 * Created by ilshat on 29.03.17.
 */
public class SourceCodeReadyMessage extends RepoIdMessage {
    private String sourcesDirPath;

    public SourceCodeReadyMessage(String sourcesDirPath, Long repoId) {
        super(repoId);
        this.sourcesDirPath = sourcesDirPath;
    }

    public String getSourcesDirPath() {
        return sourcesDirPath;
    }
}
