package com.aurea.deadcode.service;

import com.aurea.deadcode.dto.GitHubRepoAssembler;
import com.aurea.deadcode.dto.GitHubRepoDTO;
import com.aurea.deadcode.dto.GitHubRepoDetailedDTO;
import com.aurea.deadcode.exception.ConflictException;
import com.aurea.deadcode.exception.NotFoundException;
import com.aurea.deadcode.model.GitHubRepo;
import com.aurea.deadcode.repository.RepoRepository;
import com.aurea.deadcode.service.integration.GitHubIntegrationService;
import com.aurea.deadcode.service.integration.RepositoryProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by ilshat on 27.03.17.
 */
@Component
public class RepoServiceImpl implements RepoService {

    @Autowired
    private RepoRepository repoRepository;

    @Autowired
    private GitHubRepoAssembler repoAssembler;

    @Autowired
    private RepositoryProcessingService processingService;

    @Autowired
    private GitHubIntegrationService gitHubIntegrationService;

    @Override
    public Long addNewRepo(GitHubRepoDTO repoDTO) {
        GitHubRepo repo = repoAssembler.fromDTO(repoDTO);
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
    public void removeRepo(Long id) {
        repoMustExist(id);
        // TODO if processing - stop it
        gitHubIntegrationService.deleteRepositorySources(id);
        repoRepository.delete(id);
    }

    @Override
    public List<GitHubRepoDetailedDTO> listRepos() {
        Iterable<GitHubRepo> repos = repoRepository.findAll();
        return repoAssembler.toListItemDTO(repos);
    }

    @Override
    public void startProcessing(Long id) {
        GitHubRepo repo = repoRepository.findOne(id);
        if (repo == null) {
            throw new NotFoundException("Could not find repository with id " + id);
        }
        processingService.runProcessing(repo);
    }

    public void stopProcessing(Long id) {
        repoMustExist(id);
        interruptProcessingFlow(id);
    }

    private void repoMustExist(Long id) {
        if (!repoRepository.exists(id)) {
            throw new NotFoundException("Could not find repository with id " + id);
        }
    }

    private void interruptProcessingFlow(Long id) {

    }
}
