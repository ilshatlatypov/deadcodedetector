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

    public CodeOccurrence(CodeOccurrenceType type, String name, Integer line, Integer columnFrom, Integer columnTo) {
        this.type = type;
        this.name = name;
        this.line = line;
        this.columnFrom = columnFrom;
        this.columnTo = columnTo;
    }

    @Override
    public String toString() {
        return "CodeOccurrence{" +
                "id=" + id +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", line=" + line +
                ", columnFrom=" + columnFrom +
                ", columnTo=" + columnTo +
                ", repo=" + repo +
                '}';
    }
}
