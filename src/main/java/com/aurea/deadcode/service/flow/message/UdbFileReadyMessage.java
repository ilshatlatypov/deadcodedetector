package com.aurea.deadcode.service.flow.message;

/**
 * Created by ilshat on 29.03.17.
 */
public class UdbFileReadyMessage extends RepoIdMessage {
    private String udbFilePath;

    public UdbFileReadyMessage(String udbFilePath, Long repoId) {
        super(repoId);
        this.udbFilePath = udbFilePath;
    }

    public String getUdbFilePath() {
        return udbFilePath;
    }
}
