package com.aurea.deadcode.rest;

import com.aurea.deadcode.dto.GitHubRepoDTO;
import com.aurea.deadcode.dto.GitHubRepoDetailedDTO;
import com.aurea.deadcode.model.CodeOccurrence;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by ilshat on 27.03.17.
 */
@Api(description = "Operations on repositories")
@RequestMapping("repos")
public interface RepoRestService {

    @ApiOperation(value = "Add new repository")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Repository added"),
            @ApiResponse(code = 400, message = "Not valid repository in request"),
            @ApiResponse(code = 409, message = "The same repository already exists")
    })
    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> addNewRepo(@RequestBody GitHubRepoDTO repo);

    @ApiOperation(value = "Get list of all repositories")
    @ApiResponses({
            @ApiResponse(code = 200, message = "List of repositories"),
    })
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody List<GitHubRepoDetailedDTO> listRepos();

    @ApiOperation(value = "Start dead code detection processing for a repository")
    @ApiResponses({
            @ApiResponse(code = 202, message = "Repository processing is going to be started"),
            @ApiResponse(code = 404, message = "Repository not found")
    })
    @RequestMapping(method = RequestMethod.POST, path = "/{id}/processing/start")
    ResponseEntity<?> startProcessing(@PathVariable Long id);

    @ApiOperation(value = "Remove repository")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Repository removed"),
            @ApiResponse(code = 404, message = "Repository not found")
    })
    @RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
    ResponseEntity<?> removeRepo(@PathVariable Long id);

    @ApiOperation(value = "Get list of dead code occurrences")
    @ApiResponses({
            @ApiResponse(code = 200, message = "List of dead code occurrences"),
            @ApiResponse(code = 404, message = "Repository not found")
    })
    @RequestMapping(method = RequestMethod.GET, path = "/{id}/deadcode-occurrences")
    Page<CodeOccurrence> getDeadCodeOccurrences(@PathVariable Long id, Pageable pageable);
}
