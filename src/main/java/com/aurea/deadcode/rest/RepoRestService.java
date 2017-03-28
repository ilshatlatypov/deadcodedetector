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

    // 201
    // 409
    // 400
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

    // 200
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody List<GitHubRepoDetailedDTO> listRepos() {
        return repoService.listRepos();
    }

    // 202
    // 404
    @RequestMapping(method = RequestMethod.POST, path = "/{id}/processing/start")
    public ResponseEntity<?> startProcessing(@PathVariable Long id) {
        repoService.startProcessing(id);
        return ResponseEntity.accepted().build();
    }

    // 202 ?
    // 404
    @RequestMapping(method = RequestMethod.POST, path = "/{id}/processing/stop")
    public ResponseEntity<?> stopProcessing(@PathVariable Long id) {
        repoService.stopProcessing(id);
        return ResponseEntity.accepted().build();
    }
}
