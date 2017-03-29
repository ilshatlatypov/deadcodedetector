package com.aurea.deadcode.service;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by ilshat on 29.03.17.
 */
public class AppFileUtils {
    private static final String ROOT =
            System.getProperty("app-dir", System.getProperty("user.home") + "/deadcodedetector-files");
    private static final String UDB_FILES_DIR_NAME = "udb";
    private static final String SOURCES_DIR_NAME = "sources";

    public static void deleteDirRecursively(File dir) {
        try {
            FileUtils.deleteDirectory(dir);
        } catch (IOException e) {
            String message = String.format("Could not delete directory %s recursively", dir.getAbsolutePath());
            throw new RuntimeException(message);
        }
    }

    public static void createDir(File dir) {
        try {
            FileUtils.forceMkdir(dir);
        } catch (IOException e) {
            String message = String.format("Could not create directory %s with parents", dir.getAbsolutePath());
            throw new RuntimeException(message, e);
        }
    }

    public static File getRepositoryDir(Long repoId) {
        String dirName = ROOT + "/" + repoId;
        return new File(dirName);
    }

    public static File getUdbDirForRepository(Long repoId) {
        String dirName = getRepositoryDir(repoId) + "/" + UDB_FILES_DIR_NAME;
        return new File(dirName);
    }

    public static File getSourceCodeDirForRepository(Long repoId) {
        String dirName = getRepositoryDir(repoId) + "/" + SOURCES_DIR_NAME;
        return new File(dirName);
    }
}
