package com.aurea.deadcode.dto;

import com.aurea.deadcode.model.GitHubRepo;
import com.aurea.deadcode.model.GitHubRepoURL;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Created by ilshat on 31.03.17.
 */
@Component
public class RepoFromDtoConverter implements Converter<GitHubRepoDTO, GitHubRepo> {

    @Override
    public GitHubRepo convert(GitHubRepoDTO source) {
        String name = source.getName();
        GitHubRepoURL url = new GitHubRepoURL(source.getUrl());
        return new GitHubRepo(name, url);
    }
}
