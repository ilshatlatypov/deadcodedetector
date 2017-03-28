package com.aurea.deadcode.service;

import com.aurea.deadcode.dto.GitHubRepoAssembler;
import com.aurea.deadcode.dto.GitHubRepoDTO;
import com.aurea.deadcode.dto.GitHubRepoDetailedDTO;
import com.aurea.deadcode.exception.RepositoryAlreadyExistsException;
import com.aurea.deadcode.exception.RepositoryNotFoundException;
import com.aurea.deadcode.model.GitHubRepo;
import com.aurea.deadcode.repository.RepoRepository;
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

    @Override
    public Long addNewRepo(GitHubRepoDTO repoDTO) {
        GitHubRepo repo = repoAssembler.fromDTO(repoDTO);
        repoUrlMustBeUnique(repo);
        GitHubRepo savedRepo = repoRepository.save(repo);
        runProcessingFlow(savedRepo.getId());
        return savedRepo.getId();
    }

    private void repoUrlMustBeUnique(GitHubRepo repo) {
        GitHubRepo repoWithSameUrl = repoRepository.findByUrl(repo.getUrl());
        if (repoWithSameUrl != null) {
            throw new RepositoryAlreadyExistsException(repo.getUrl().toString());
        }
    }

    @Override
    public List<GitHubRepoDetailedDTO> listRepos() {
        Iterable<GitHubRepo> repos = repoRepository.findAll();
        return repoAssembler.toListItemDTO(repos);
    }

    @Override
    public void startProcessing(Long id) {
        repoMustExist(id);
        runProcessingFlow(id);
    }

    public void stopProcessing(Long id) {
        repoMustExist(id);
        interruptProcessingFlow(id);
    }

    private void repoMustExist(Long id) {
        if (!repoRepository.exists(id)) {
            throw new RepositoryNotFoundException(id);
        }
    }

    private void runProcessingFlow(Long repoId) {

    }

    private void interruptProcessingFlow(Long id) {

    }
}
