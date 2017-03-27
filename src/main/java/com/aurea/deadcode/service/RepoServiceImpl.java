package com.aurea.deadcode.service;

import com.aurea.deadcode.dto.GitHubRepoDetailedDTO;
import com.aurea.deadcode.dto.GitHubRepoAssembler;
import com.aurea.deadcode.dto.GitHubRepoDTO;
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

    public Long addNewRepo(GitHubRepoDTO repoDTO) {
        GitHubRepo repo = repoAssembler.fromDTO(repoDTO);
        GitHubRepo savedRepo = repoRepository.save(repo);
        return savedRepo.getId();
    }

    public List<GitHubRepoDetailedDTO> listRepos() {
        Iterable<GitHubRepo> repos = repoRepository.findAll();
        return repoAssembler.toListItemDTO(repos);
    }
}
