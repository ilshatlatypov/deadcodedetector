package com.aurea.deadcode.service.flow.message;

/**
 * Created by ilshat on 29.03.17.
 */
public class UdbFileReadyMessage {
    private String udbFilePath;
    private Long repoId;

    public UdbFileReadyMessage(String udbFilePath, Long repoId) {
        this.udbFilePath = udbFilePath;
        this.repoId = repoId;
    }

    public String getUdbFilePath() {
        return udbFilePath;
    }

    public Long getRepoId() {
        return repoId;
    }
}
