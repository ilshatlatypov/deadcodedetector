package com.aurea.deadcode.service.flow.message;

/**
 * Created by ilshat on 30.03.17.
 */
public abstract class RepoIdMessage {
    private Long repoId;

    RepoIdMessage(Long repoId) {
        this.repoId = repoId;
    }

    public Long getRepoId() {
        return repoId;
    }
}
