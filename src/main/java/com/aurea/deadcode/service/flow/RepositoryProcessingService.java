package com.aurea.deadcode.service.flow;

import com.aurea.deadcode.model.GitHubRepo;

import java.util.concurrent.Future;

/**
 * Created by ilshat on 28.03.17.
 */
public interface RepositoryProcessingService {
    Future<Void> runProcessing(GitHubRepo repo);
}
