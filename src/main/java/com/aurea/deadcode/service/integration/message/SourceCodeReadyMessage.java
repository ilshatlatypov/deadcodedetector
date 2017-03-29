package com.aurea.deadcode.service.integration.message;

/**
 * Created by ilshat on 29.03.17.
 */
public class SourceCodeReadyMessage {
    private String sourcesDirPath;
    private Long repoId;

    public SourceCodeReadyMessage(String sourcesDirPath, Long repoId) {
        this.sourcesDirPath = sourcesDirPath;
        this.repoId = repoId;
    }

    public String getSourcesDirPath() {
        return sourcesDirPath;
    }

    public Long getRepoId() {
        return repoId;
    }
}
