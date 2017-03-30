package com.aurea.deadcode.service.flow.message;

/**
 * Created by ilshat on 28.03.17.
 */
public class GitHubRepoPayload extends RepoIdMessage {
    private String url;
    private boolean alreadyProcessing;

    public GitHubRepoPayload(Long id, String url) {
        super(id);
        this.url = url;
        this.alreadyProcessing = false;
    }

    public String getUrl() {
        return url;
    }

    public void setAlreadyProcessing(boolean alreadyProcessing) {
        this.alreadyProcessing = alreadyProcessing;
    }

    public boolean isAlreadyProcessing() {
        return alreadyProcessing;
    }
}
