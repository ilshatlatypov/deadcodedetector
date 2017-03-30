package com.aurea.deadcode.service.flow.message;

/**
 * Created by ilshat on 29.03.17.
 */
public class UdbFileReadyMessage extends RepoIdMessage {
    private String udbFilePath;
    private String sourcesDirPath;

    public UdbFileReadyMessage(String udbFilePath, String sourcesDirPath, Long repoId) {
        super(repoId);
        this.udbFilePath = udbFilePath;
        this.sourcesDirPath = sourcesDirPath;
    }

    public String getUdbFilePath() {
        return udbFilePath;
    }

    public String getSourcesDirPath() {
        return sourcesDirPath;
    }
}
