package com.aurea.deadcode.dto;

/**
 * Created by ilshat on 27.03.17.
 */
public class GitHubRepoDTO {
    private String name;
    private String url;

    public GitHubRepoDTO() {}

    public GitHubRepoDTO(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
