package com.aurea.deadcode.service.integration;

import com.aurea.deadcode.model.CodeOccurrence;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created by ilshat on 29.03.17.
 */
public interface UnderstandIntegrationService {

    File createUdbFile(String udbFilePath, String sourcesDirPath) throws FileNotFoundException;

    List<CodeOccurrence> searchForDeadCodeOccurrences(String udbFilePath, String sourcesDirPath);
}
