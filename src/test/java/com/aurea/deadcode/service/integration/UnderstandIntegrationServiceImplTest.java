package com.aurea.deadcode.service.integration;

import com.aurea.deadcode.model.CodeOccurrence;
import com.aurea.deadcode.model.CodeOccurrenceType;
import com.google.common.io.Files;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by ilshat on 29.03.17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = UnderstandIntegrationServiceImpl.class)
public class UnderstandIntegrationServiceImplTest {

    @Autowired
    private UnderstandIntegrationService understandIntegrationService;

    @Test
    public void deadMethodSearch() throws Exception {
        final String sourcesDirName = "sources/dead-method";
        final CodeOccurrenceType expectedType = CodeOccurrenceType.DEAD_METHOD;
        final String expectedFile = "/DeadMethodTest.java";
        final String expectedName = "notUsedPrivateMethod";
        final int expectedLine = 4;
        final int expectedColumnFrom = 18;
        final int expectedColumnTo = 37;

        String sourcesPath = getResourceAbsolutePath(sourcesDirName);
        String udbFilePath = getUdbFilePath();

        understandIntegrationService.createUdbFile(udbFilePath, sourcesPath);
        List<CodeOccurrence> occurrences =
                understandIntegrationService.searchForDeadCodeOccurrences(udbFilePath, sourcesPath);

        assertEquals(1, occurrences.size());

        CodeOccurrence occurrence = occurrences.iterator().next();
        assertEquals(expectedType, occurrence.getType());
        assertEquals(expectedFile, occurrence.getFile());
        assertEquals(expectedName, occurrence.getName());
        assertEquals(expectedLine, occurrence.getLine().intValue());
        assertEquals(expectedColumnFrom, occurrence.getColumnFrom().intValue());
        assertEquals(expectedColumnTo, occurrence.getColumnTo().intValue());
    }

    @Test
    public void deadVariableSearch() throws Exception {
        final String sourcesDirName = "sources/dead-variable";
        final CodeOccurrenceType expectedType = CodeOccurrenceType.DEAD_VARIABLE;
        final String expectedFile = "/DeadVariableTest.java";
        final String expectedName = "notUsedPrivateVariable";
        final int expectedLine = 5;
        final int expectedColumnFrom = 17;
        final int expectedColumnTo = 38;

        String sourcesPath = getResourceAbsolutePath(sourcesDirName);
        String udbFilePath = getUdbFilePath();

        understandIntegrationService.createUdbFile(udbFilePath, sourcesPath);
        List<CodeOccurrence> occurrences =
                understandIntegrationService.searchForDeadCodeOccurrences(udbFilePath, sourcesPath);

        assertEquals(1, occurrences.size());

        CodeOccurrence occurrence = occurrences.iterator().next();
        assertEquals(expectedType, occurrence.getType());
        assertEquals(expectedFile, occurrence.getFile());
        assertEquals(expectedName, occurrence.getName());
        assertEquals(expectedLine, occurrence.getLine().intValue());
        assertEquals(expectedColumnFrom, occurrence.getColumnFrom().intValue());
        assertEquals(expectedColumnTo, occurrence.getColumnTo().intValue());
    }

    @Test
    public void deadParameterSearch() throws Exception {
        final String sourcesDirName = "sources/dead-parameter";
        final CodeOccurrenceType expectedType = CodeOccurrenceType.DEAD_PARAMETER;
        final String expectedFile = "/DeadParameterTest.java";
        final String expectedName = "notUsedParameter";
        final int expectedLine = 5;
        final int expectedColumnFrom = 46;
        final int expectedColumnTo = 61;

        String sourcesPath = getResourceAbsolutePath(sourcesDirName);
        String udbFilePath = getUdbFilePath();

        understandIntegrationService.createUdbFile(udbFilePath, sourcesPath);
        List<CodeOccurrence> occurrences =
                understandIntegrationService.searchForDeadCodeOccurrences(udbFilePath, sourcesPath);

        assertEquals(1, occurrences.size());

        CodeOccurrence occurrence = occurrences.iterator().next();
        assertEquals(expectedType, occurrence.getType());
        assertEquals(expectedFile, occurrence.getFile());
        assertEquals(expectedName, occurrence.getName());
        assertEquals(expectedLine, occurrence.getLine().intValue());
        assertEquals(expectedColumnFrom, occurrence.getColumnFrom().intValue());
        assertEquals(expectedColumnTo, occurrence.getColumnTo().intValue());
    }

    @Test
    @Ignore // Scitools cannot properly determine method call as a part of lambda
    public void shouldNotFindMethodAsDeadIfCalledFromLambda() throws Exception {
        final String sourcesDirName = "sources/method-calledby-lambda";

        String sourcesPath = getResourceAbsolutePath(sourcesDirName);
        String udbFilePath = getUdbFilePath();

        understandIntegrationService.createUdbFile(udbFilePath, sourcesPath);
        List<CodeOccurrence> occurrences =
                understandIntegrationService.searchForDeadCodeOccurrences(udbFilePath, sourcesPath);
        assertTrue(occurrences.isEmpty());
    }

    @Test
    public void shouldNotFindParameterAsDeadIfInInterface() throws Exception {
        final String sourcesDirName = "sources/interface";

        String sourcesPath = getResourceAbsolutePath(sourcesDirName);
        String udbFilePath = getUdbFilePath();

        understandIntegrationService.createUdbFile(udbFilePath, sourcesPath);
        List<CodeOccurrence> occurrences =
                understandIntegrationService.searchForDeadCodeOccurrences(udbFilePath, sourcesPath);
        assertTrue(occurrences.isEmpty());
    }

    @Test
    public void shouldNotConsiderEnumNameAsParameter() throws Exception {
        final String sourcesDirName = "sources/enumeration";

        String sourcesPath = getResourceAbsolutePath(sourcesDirName);
        String udbFilePath = getUdbFilePath();

        understandIntegrationService.createUdbFile(udbFilePath, sourcesPath);
        List<CodeOccurrence> occurrences =
                understandIntegrationService.searchForDeadCodeOccurrences(udbFilePath, sourcesPath);
        assertTrue(occurrences.isEmpty());
    }

    @Test
    public void shouldNotFindLambdaMethodAsDead() throws Exception {
        final String sourcesDirName = "sources/lambda";

        String sourcesPath = getResourceAbsolutePath(sourcesDirName);
        String udbFilePath = getUdbFilePath();

        understandIntegrationService.createUdbFile(udbFilePath, sourcesPath);
        List<CodeOccurrence> occurrences =
                understandIntegrationService.searchForDeadCodeOccurrences(udbFilePath, sourcesPath);
        assertTrue(occurrences.isEmpty());
    }

    @SuppressWarnings("ConstantConditions")
    private String getResourceAbsolutePath(String resourceName) {
        ClassLoader classLoader = getClass().getClassLoader();
        File resourceFile = new File(classLoader.getResource(resourceName).getFile());
        return resourceFile.getAbsolutePath();
    }

    private String getUdbFilePath() {
        return Files.createTempDir().getAbsolutePath() + "/" + "test-project.udb";
    }
}