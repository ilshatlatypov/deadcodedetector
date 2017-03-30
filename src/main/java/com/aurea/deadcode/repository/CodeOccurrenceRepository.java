package com.aurea.deadcode.repository;

import com.aurea.deadcode.model.CodeOccurrence;
import com.aurea.deadcode.model.GitHubRepo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

/**
 * Created by ilshat on 29.03.17.
 */
public interface CodeOccurrenceRepository extends JpaRepository<CodeOccurrence, Long> {

    Iterable<CodeOccurrence> findAllByRepo(GitHubRepo repo);

    @Modifying
    @Transactional
    @Query("delete from CodeOccurrence co where co.repo = ?1")
    void deleteCodeOccurrencesByGitHubRepo(GitHubRepo repo);
}
