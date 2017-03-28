package com.aurea.deadcode.model;

import org.apache.commons.lang3.Validate;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by ilshat on 27.03.17.
 */
@Entity
@Table(name = "repos", uniqueConstraints = { @UniqueConstraint(columnNames = {"url"}) })
public class GitHubRepo {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    @Column(nullable = false)
    private GitHubRepoURL url;

    @Enumerated
    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private Date addedDate;

    @Column
    private Date processingCompletionDate;

    @Column
    private Date processingFailureDate;

    private GitHubRepo() {}

    public GitHubRepo(String name, GitHubRepoURL url) {
        Validate.isTrue(name != null, "Repository name cannot be null");
        Validate.isTrue(url != null, "Repository URL cannot be null");
        Validate.notBlank(name, "Repository name cannot be blank");
        this.name = name;
        this.url = url;
    }

    @PrePersist
    void onPersist() {
        this.status = Status.ADDED;
        this.addedDate = new Date();
    }

    public Long getId() {
        return id;
    }

    public String getName() { return name; }

    public GitHubRepoURL getUrl() {
        return url;
    }

    public Status getStatus() {
        return status;
    }

    public Date getAddedDate() { return addedDate; }

    public Date getProcessingCompletionDate() {
        return processingCompletionDate;
    }

    public Date getProcessingFailureDate() {
        return processingFailureDate;
    }

    public boolean isProcessing() {
        return this.status == Status.PROCESSING;
    }

    public void setProcessing() {
        this.status = Status.PROCESSING;
    }

    public void setCompleted() {
        this.status = Status.COMPLETED;
        this.processingCompletionDate = new Date();
    }

    public void setFailed() {
        this.status = Status.FAILED;
        this.processingFailureDate = new Date();
    }
}
