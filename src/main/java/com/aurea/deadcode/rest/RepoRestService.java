package com.aurea.deadcode.rest;

import com.aurea.deadcode.dto.GitHubRepoDTO;
import com.aurea.deadcode.dto.GitHubRepoDetailedDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by ilshat on 27.03.17.
 */
@Api(description = "Operations on repositories")
@RequestMapping("repos")
public interface RepoRestService {

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value = "Add new repository", response = Void.class)
    @ApiResponses({
            @ApiResponse(code = 201, message = "Repository added"),
            @ApiResponse(code = 400, message = "Not valid repository in request"),
            @ApiResponse(code = 409, message = "The same repository already exists")
    })
    ResponseEntity<?> addNewRepo(@RequestBody GitHubRepoDTO repo);

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Get list of all repositories")
    @ApiResponses({
            @ApiResponse(code = 200, message = "List of repositories"),
    })
    @ResponseBody List<GitHubRepoDetailedDTO> listRepos();

    @RequestMapping(method = RequestMethod.POST, path = "/{id}/processing/start")
    @ApiOperation(value = "Start dead code detection processing for a repository")
    @ApiResponses({
            @ApiResponse(code = 202, message = "Repository processing is going to be started"),
            @ApiResponse(code = 404, message = "Repository not found")
    })
    ResponseEntity<?> startProcessing(@PathVariable Long id);

    @RequestMapping(method = RequestMethod.POST, path = "/{id}/processing/stop")
    @ApiOperation(value = "Stop dead code detection processing for a repository")
    @ApiResponses({
            @ApiResponse(code = 202, message = "Repository processing is going to be stopped"),
            @ApiResponse(code = 404, message = "Repository not found")
    })
    ResponseEntity<?> stopProcessing(@PathVariable Long id);

    @RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
    @ApiOperation(value = "Remove repository")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Repository removed"), // TODO or 202 ?
            @ApiResponse(code = 404, message = "Repository not found")
    })
    ResponseEntity<?> removeRepo(@PathVariable Long id);
}
