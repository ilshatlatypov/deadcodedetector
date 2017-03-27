package com.aurea.deadcode.rest;

import com.aurea.deadcode.dto.GitHubRepoDetailedDTO;
import com.aurea.deadcode.dto.GitHubRepoDTO;
import com.aurea.deadcode.service.RepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Created by ilshat on 27.03.17.
 */
@RestController
@RequestMapping("repos")
public class RepoRestService {

    @Autowired
    private RepoService repoService;

    @RequestMapping(method = RequestMethod.POST)
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

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody List<GitHubRepoDetailedDTO> listRepos() {
        return repoService.listRepos();
    }
}
