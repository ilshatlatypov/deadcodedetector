package com.aurea.deadcode.service.integration;

/**
 * Created by ilshat on 28.03.17.
 */
public class GitHubRepoPayload {
    private Long id;
    private String url;
    private boolean alreadyProcessing;

    public GitHubRepoPayload(Long id, String url) {
        this.id = id;
        this.url = url;
        this.alreadyProcessing = false;
    }

    public Long getId() {
        return id;
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
