package com.aurea.deadcode.dto;

import com.aurea.deadcode.model.Status;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

/**
 * Created by ilshat on 27.03.17.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GitHubRepoDetailedDTO {
    private Long id;
    private String name;
    private String url;
    private Status status;
    private Date addedDate;
    private Date processingCompletionDate;
    private Date processingFailureDate;

    public GitHubRepoDetailedDTO(Long id, String name, String url, Status status,
                                 Date addedDate, Date processingCompletionDate, Date processingFailureDate) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.status = status;
        this.addedDate = addedDate;
        this.processingCompletionDate = processingCompletionDate;
        this.processingFailureDate = processingFailureDate;

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

    public Date getProcessingCompletionDate() {
        return processingCompletionDate;
    }

    public Date getProcessingFailureDate() {
        return processingFailureDate;
    }
}
