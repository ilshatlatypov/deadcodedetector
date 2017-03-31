package com.aurea.deadcode.repository;

import com.aurea.deadcode.model.CodeOccurrence;
import com.aurea.deadcode.model.GitHubRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.transaction.Transactional;

/**
 * Created by ilshat on 29.03.17.
 */
public interface CodeOccurrenceRepository extends PagingAndSortingRepository<CodeOccurrence, Long> {

    Page<CodeOccurrence> findAllByRepo(GitHubRepo repo, Pageable pageable);

    @Modifying
    @Transactional
    @Query("delete from CodeOccurrence co where co.repo = ?1")
    void deleteCodeOccurrencesByRepo(GitHubRepo repo);
}
