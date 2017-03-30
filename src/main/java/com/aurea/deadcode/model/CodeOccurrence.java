package com.aurea.deadcode.model;

import javax.persistence.*;

/**
 * Created by ilshat on 29.03.17.
 */
@Entity
@Table(name = "code_occurrence")
public class CodeOccurrence {
    @Id
    @GeneratedValue
    private Long id;

    @Enumerated
    @Column(nullable = false)
    private CodeOccurrenceType type;

    private String file;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer line;

    @Column(nullable = false)
    private Integer columnFrom;

    @Column(nullable = false)
    private Integer columnTo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "repo_id")
    private GitHubRepo repo;

    public CodeOccurrence() {}

    public CodeOccurrence(CodeOccurrenceType type, String file, String name,
                          Integer line, Integer columnFrom, Integer columnTo) {
        this.type = type;
        this.file = file;
        this.name = name;
        this.line = line;
        this.columnFrom = columnFrom;
        this.columnTo = columnTo;
    }

    public CodeOccurrenceType getType() {
        return type;
    }

    public String getFile() {
        return file;
    }

    public String getName() {
        return name;
    }

    public Integer getLine() {
        return line;
    }

    public Integer getColumnFrom() {
        return columnFrom;
    }

    public Integer getColumnTo() {
        return columnTo;
    }

    public void setRepo(GitHubRepo repo) {
        this.repo = repo;
    }

    @Override
    public String toString() {
        return "CodeOccurrence{" +
                "id=" + id +
                ", type=" + type +
                ", file=" + file +
                ", name='" + name + '\'' +
                ", line=" + line +
                ", columnFrom=" + columnFrom +
                ", columnTo=" + columnTo +
                ", repo=" + repo +
                '}';
    }
}
