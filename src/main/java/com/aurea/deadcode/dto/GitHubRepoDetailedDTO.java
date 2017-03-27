package com.aurea.deadcode.dto;

import com.aurea.deadcode.model.Status;

import java.util.Date;

/**
 * Created by ilshat on 27.03.17.
 */
public class GitHubRepoDetailedDTO {
    private Long id;
    private String name;
    private String url;
    private Status status;
    private Date addedDate;

    public GitHubRepoDetailedDTO(Long id, String name, String url, Status status, Date addedDate) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.status = status;
        this.addedDate = addedDate;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public Status getStatus() {
        return status;
    }

    public Date getAddedDate() {
        return addedDate;
    }
}
