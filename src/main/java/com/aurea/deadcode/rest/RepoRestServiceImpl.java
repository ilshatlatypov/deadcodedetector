package com.aurea.deadcode.rest;

import com.aurea.deadcode.dto.GitHubRepoDTO;
import com.aurea.deadcode.dto.GitHubRepoDetailedDTO;
import com.aurea.deadcode.exception.ConflictException;
import com.aurea.deadcode.exception.NotFoundException;
import com.aurea.deadcode.model.CodeOccurrence;
import com.aurea.deadcode.service.RepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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

    public ResponseEntity<?> startProcessing(@PathVariable Long id) {
        repoService.startProcessing(id);
        return ResponseEntity.accepted().build();
    }

    public ResponseEntity<?> removeRepo(@PathVariable Long id) {
        repoService.removeRepo(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public List<CodeOccurrence> getDeadCodeOccurrences(@PathVariable Long id) {
        return repoService.getDeadCodeOccurrences(id);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    void handleBadRequests(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler({NotFoundException.class})
    void handleNotFound(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value());
    }

    @ExceptionHandler({ConflictException.class})
    void handleConflicts(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.CONFLICT.value());
    }
}
