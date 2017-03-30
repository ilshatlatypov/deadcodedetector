package com.aurea.deadcode.service.integration;

import com.aurea.deadcode.model.CodeOccurrence;
import com.aurea.deadcode.service.integration.algorithm.Algorithm;
import com.google.common.collect.ImmutableSet;
import com.scitools.understand.Database;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by ilshat on 29.03.17.
 */
@Component
public class UnderstandIntegrationServiceImpl implements UnderstandIntegrationService {

    private DatabaseFactory databaseFactory = new DatabaseFactory();

    private Set<Algorithm> algorithms;

    public void setAlgorithms(Set<Algorithm> algorithms) {
        this.algorithms = ImmutableSet.copyOf(algorithms);
    }

    @Override
    public File createUdbFile(String udbFilePath, String sourcesDirPath) throws FileNotFoundException {
        File sourcesDir = new File(sourcesDirPath);
        if (!sourcesDir.exists() || !sourcesDir.isDirectory()) {
            String message = "Could not find sources directory at " + sourcesDir.getAbsolutePath();
            throw new FileNotFoundException(message);
        }

        File udbFile = new File(udbFilePath);
        File udbFileDir = udbFile.getParentFile();
        if (!udbFileDir.exists() || !udbFileDir.isDirectory()) {
            String message = "Could not find parent directory UDB file at " + udbFileDir.getAbsolutePath();
            throw new FileNotFoundException(message);
        }

        String[] udbCreationCommand = {
                "und", "-db", udbFile.getAbsolutePath(),
                "create", "-languages", "Java",
                "add", sourcesDir.getAbsolutePath(), "analyze"
        };

        try {
            Process p = Runtime.getRuntime().exec(udbCreationCommand);
            ignoreInputStream(p);
            p.waitFor();
            if (p.exitValue() != 0) {
                throw new UnderstandIntegrationException("UDB command finished with code " + p.exitValue());
            }
        } catch (IOException e) {
            throw new UnderstandIntegrationException("Cannot execute udb command", e);
        } catch (InterruptedException e) {
            throw new UnderstandIntegrationException("UDB file creation command interrupted", e);
        }
        return udbFile;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    private void ignoreInputStream(Process p) throws IOException {
        BufferedInputStream in = new BufferedInputStream(p.getInputStream());
        byte[] bytes = new byte[4096];
        while (in.read(bytes) != -1) {}
    }

    @Override
    public List<CodeOccurrence> searchForDeadCodeOccurrences(String udbFilePath, String sourcesDirPath) {
        List<CodeOccurrence> occurrences = new ArrayList<>();
        Database db = databaseFactory.getDatabase(udbFilePath);
        for (Algorithm algorithm : algorithms) {
            occurrences.addAll(algorithm.perform(db));
        }
        makeFilePathsRelative(sourcesDirPath, occurrences);
        return occurrences;
    }

    private void makeFilePathsRelative(String sourcesDirPath, List<CodeOccurrence> deadCodeOccurrences) {
        int sourcesDirPathLength = sourcesDirPath.length();
        for (CodeOccurrence co : deadCodeOccurrences) {
            String relativeFilePath = co.getFile().substring(sourcesDirPathLength);
            co.setFile(relativeFilePath);
        }
    }
}
