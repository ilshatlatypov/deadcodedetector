package com.aurea.deadcode.rest;

import com.aurea.deadcode.dto.GitHubRepoDTO;
import com.aurea.deadcode.dto.GitHubRepoDetailedDTO;
import com.aurea.deadcode.service.RepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Created by ilshat on 28.03.17.
 */
@RestController
public class RepoRestServiceImpl implements RepoRestService {

    @Autowired
    private RepoService repoService;

    public ResponseEntity<?> addNewRepo(@RequestBody GitHubRepoDTO repo) {
        Long addedRepoId = repoService.addNewRepo(repo);
        URI location = buildLocationURI(addedRepoId);
        return ResponseEntity.created(location).build();
    }

    private static URI buildLocationURI(Long addedRepoId) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(addedRepoId).toUri();
    }

    public List<GitHubRepoDetailedDTO> listRepos() {
        return repoService.listRepos();
    }

    public ResponseEntity<?> startProcessing(Long id) {
        repoService.startProcessing(id);
        return ResponseEntity.accepted().build();
    }

    public ResponseEntity<?> stopProcessing(Long id) {
        repoService.stopProcessing(id);
        return ResponseEntity.accepted().build();
    }

    public ResponseEntity<?> removeRepo(Long id) {
        repoService.removeRepo(id);
        return ResponseEntity.noContent().build();
    }
}
