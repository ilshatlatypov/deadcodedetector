package com.aurea.deadcode.service.integration;

import com.aurea.deadcode.model.CodeOccurrence;
import com.aurea.deadcode.model.CodeOccurrenceType;
import com.scitools.understand.*;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by ilshat on 29.03.17.
 */
@Component
public class UnderstandIntegrationServiceImpl implements UnderstandIntegrationService {

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

        try {
            String[] udbCreationCommand = {
                    "und", "-db", udbFile.getAbsolutePath(),
                    "create", "-languages", "Java",
                    "add", sourcesDir.getAbsolutePath(), "analyze"
            };
            Process p = Runtime.getRuntime().exec(udbCreationCommand);
            ignoreInputStream(p);
            p.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return udbFile;
    }

    private void ignoreInputStream(Process p) throws IOException {
        BufferedInputStream in = new BufferedInputStream(p.getInputStream());
        byte[] bytes = new byte[4096];
        while (in.read(bytes) != -1) {}
    }

    public Set<CodeOccurrence> searchForDeadCodeOccurrences(String udbFilePath) {
        Set<CodeOccurrence> occurrences = new HashSet<>();
        try {
            Database db = Understand.open(udbFilePath);

            Entity[] privateMethods = db.ents("method private ~constructor ~unknown ~unresolved");
            for (Entity method : privateMethods) {
                Reference[] callByRefs = method.refs("callby", null, false);
                if (callByRefs.length > 0) {
                    continue;
                }

                Reference[] defineInRefs = method.refs("definein", null, false);
                Reference definitionReference = defineInRefs[0];
                int line = definitionReference.line();
                int columnFrom = definitionReference.column();
                int columnTo = columnFrom + method.name().length();
                CodeOccurrenceType type = CodeOccurrenceType.METHOD;

                occurrences.add(new CodeOccurrence(type, method.name(), line, columnFrom, columnTo));
            }

            db.close();
        } catch (UnderstandException e) {
            // TODO
            e.printStackTrace();
        }
        return occurrences;
    }
}
