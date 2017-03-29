package com.aurea.deadcode.service.integration;

import com.aurea.deadcode.model.CodeOccurrence;
import com.aurea.deadcode.model.CodeOccurrenceType;
import com.scitools.understand.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

        String[] udbCreationCommand = {
                "und", "-db", udbFile.getAbsolutePath(),
                "create", "-languages", "Java",
                "add", sourcesDir.getAbsolutePath(), "analyze"
        };

        try {
            Process p = Runtime.getRuntime().exec(udbCreationCommand);
            ignoreInputStream(p);
            p.waitFor();
        } catch (IOException e) {
            // TODO
            throw new RuntimeException("Cannot execute udb command", e);
        } catch (InterruptedException e) {
            // TODO
            e.printStackTrace();
        }
        return udbFile;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    private void ignoreInputStream(Process p) throws IOException {
        BufferedInputStream in = new BufferedInputStream(p.getInputStream());
        byte[] bytes = new byte[4096];
        while (in.read(bytes) != -1) {}
    }

    public Set<CodeOccurrence> searchForDeadCodeOccurrences(String udbFilePath) {
        Set<CodeOccurrence> occurrences = new HashSet<>();
        Database db = null;
        try {
            db = Understand.open(udbFilePath);
            occurrences.addAll(searchForDeadMethods(db));
            occurrences.addAll(searchForDeadParameters(db));
            occurrences.addAll(searchForDeadVariables(db));
        } catch (UnderstandException e) {
            throw new RuntimeException("Could not open Understand database (UDB file)", e);
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return occurrences;
    }

    private List<CodeOccurrence> searchForDeadMethods(Database db) {
        List<CodeOccurrence> occurrences = new ArrayList<>();
        Entity[] privateMethods = db.ents("method private ~constructor ~unknown ~unresolved");
        for (Entity method : privateMethods) {
            if (isEntityCalled(method)) {
                continue;
            }

            CodeOccurrenceType type = CodeOccurrenceType.METHOD;
            String pureMethodName = StringUtils.substringAfterLast(method.name(), ".");

            Reference ref = getDefinitionReference(method);
            String filename = ref.file().longname(true);
            int line = ref.line();
            int columnFrom = ref.column() + 1;
            int columnTo = columnFrom + pureMethodName.length() - 1;
            occurrences.add(new CodeOccurrence(type, filename, pureMethodName, line, columnFrom, columnTo));
        }
        return occurrences;
    }

    private List<CodeOccurrence> searchForDeadParameters(Database db) {
        List<CodeOccurrence> occurrences = new ArrayList<>();
        Entity[] parameters = db.ents("parameter ~catch");
        for (Entity param : parameters) {
            if (isEntityUsed(param)) {
                continue;
            }

            CodeOccurrenceType type = CodeOccurrenceType.PARAMETER;

            Reference ref = getDefinitionReference(param);
            String filename = ref.file().longname(true);
            String paramName = param.name();
            int line = ref.line();
            int columnFrom = ref.column() + 1;
            int columnTo = columnFrom + paramName.length() - 1;
            occurrences.add(new CodeOccurrence(type, filename, paramName, line, columnFrom, columnTo));
        }
        return occurrences;
    }

    private List<CodeOccurrence> searchForDeadVariables(Database db) {
        List<CodeOccurrence> occurrences = new ArrayList<>();
        Entity[] privateMembers = db.ents("variable private");
        for (Entity member : privateMembers) {
            if (isEntityUsed(member)) {
                continue;
            }

            CodeOccurrenceType type = CodeOccurrenceType.VARIABLE;
            String pureMemberName = StringUtils.substringAfterLast(member.name(), ".");

            Reference ref = getDefinitionReference(member);
            String filename = ref.file().longname(true);
            int line = ref.line();
            int columnFrom = ref.column() + 1;
            int columnTo = columnFrom + pureMemberName.length() - 1;
            occurrences.add(new CodeOccurrence(type, filename, pureMemberName, line, columnFrom, columnTo));
        }
        return occurrences;
    }

    private boolean isEntityUsed(Entity ent) {
        return ent.refs("useby", null, false).length > 0;
    }

    private boolean isEntityCalled(Entity ent) {
        return ent.refs("callby", null, false).length > 0;
    }

    private Reference getDefinitionReference(Entity ent) {
        Reference[] defineInRefs = ent.refs("definein", null, false);
        return defineInRefs[0];
    }
}
