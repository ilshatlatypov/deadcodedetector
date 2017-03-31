package com.aurea.deadcode.service;

import com.aurea.deadcode.dto.GitHubRepoDTO;
import com.aurea.deadcode.dto.GitHubRepoDetailedDTO;
import com.aurea.deadcode.dto.RepoFromDtoConverter;
import com.aurea.deadcode.dto.RepoToDetailedDtoConverter;
import com.aurea.deadcode.exception.CantRemoveRepositoryException;
import com.aurea.deadcode.exception.ConflictException;
import com.aurea.deadcode.exception.NotFoundException;
import com.aurea.deadcode.model.CodeOccurrence;
import com.aurea.deadcode.model.GitHubRepo;
import com.aurea.deadcode.repository.CodeOccurrenceRepository;
import com.aurea.deadcode.repository.RepoRepository;
import com.aurea.deadcode.service.flow.RepositoryProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

/**
 * Created by ilshat on 27.03.17.
 */
@Component
public class RepoServiceImpl implements RepoService {

    @Autowired
    private RepoRepository repoRepository;
    @Autowired
    private CodeOccurrenceRepository codeOccurrenceRepository;

    @Autowired
    private RepoFromDtoConverter fromDtoConverter;
    @Autowired
    private RepoToDetailedDtoConverter toDetailedDtoConverter;

    @Autowired
    private RepositoryProcessingService processingService;

    @Override
    public Long addNewRepo(GitHubRepoDTO repoDTO) {
        GitHubRepo repo = fromDtoConverter.convert(repoDTO);
        repoUrlMustBeUnique(repo);
        GitHubRepo savedRepo = repoRepository.save(repo);
        processingService.runProcessing(savedRepo);
        return savedRepo.getId();
    }

    private void repoUrlMustBeUnique(GitHubRepo repo) {
        GitHubRepo repoWithSameUrl = repoRepository.findByUrl(repo.getUrl());
        if (repoWithSameUrl != null) {
            String message = String.format("Repository with URL %s already exists with id %d",
                    repo.getUrl().toString(), repoWithSameUrl.getId());
            throw new ConflictException(message);
        }
    }

    @Override
    @Transactional
    public void removeRepo(Long id) {
        GitHubRepo repo = repoRepository.findOne(id);
        checkNotNullOrNotFoundEx(repo, id);
        checkNotProcessing(repo);
        codeOccurrenceRepository.deleteCodeOccurrencesByRepo(repo);
        repoRepository.delete(id);
    }

    private void checkNotProcessing(GitHubRepo repo) {
        if (repo.isProcessing()) {
            String message = String.format("Repository with id %d is currently processing", repo.getId());
            throw new CantRemoveRepositoryException(message);
        }
    }

    @Override
    public Page<GitHubRepoDetailedDTO> listRepos(Pageable pageable) {
        Page<GitHubRepo> page = repoRepository.findAll(pageable);
        return page.map(toDetailedDtoConverter);
    }

    @Override
    public void startProcessing(Long id) {
        GitHubRepo repo = repoRepository.findOne(id);
        checkNotNullOrNotFoundEx(repo, id);
        processingService.runProcessing(repo);
    }

    @Override
    public Page<CodeOccurrence> getDeadCodeOccurrences(Long repoId, Pageable pageable) {
        GitHubRepo repo = repoRepository.findOne(repoId);
        checkNotNullOrNotFoundEx(repo, repoId);
        return codeOccurrenceRepository.findAllByRepo(repo, pageable);
    }

    private void checkNotNullOrNotFoundEx(GitHubRepo repo, Long repoId) {
        if (repo == null) {
            throw new NotFoundException("Could not find repository with id " + repoId);
        }
    }
}
