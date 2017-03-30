package com.aurea.deadcode.service.integration;

import com.aurea.deadcode.model.CodeOccurrence;
import com.aurea.deadcode.model.CodeOccurrenceType;
import com.scitools.understand.Database;
import com.scitools.understand.Entity;
import com.scitools.understand.Reference;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ilshat on 29.03.17.
 */
@Component
public class UnderstandIntegrationServiceImpl implements UnderstandIntegrationService {

    private DatabaseFactory databaseFactory = new DatabaseFactory();

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
        occurrences.addAll(searchForDeadMethods(db));
        occurrences.addAll(searchForDeadParameters(db));
        occurrences.addAll(searchForDeadVariables(db));
        makeFilePathsRelative(sourcesDirPath, occurrences);
        return occurrences;
    }

    private List<CodeOccurrence> searchForDeadMethods(Database db) {
        List<CodeOccurrence> occurrences = new ArrayList<>();
        Entity[] privateMethods = db.ents("method private ~constructor ~unknown ~unresolved");
        for (Entity method : privateMethods) {
            if (isEntityCalled(method)) {
                continue;
            }
            if (isLambda(method)) {
                continue;
            }

            CodeOccurrenceType type = CodeOccurrenceType.DEAD_METHOD;
            String pureMethodName = getPureEntityName(method);
            Reference definitionReference = getDefinitionReference(method);
            occurrences.add(buildCodeOccurrence(type, pureMethodName, definitionReference));
        }
        return occurrences;
    }

    private boolean isLambda(Entity methodEntity) {
        return getPureEntityName(methodEntity).startsWith("(");
    }

    private String getPureEntityName(Entity entity) {
        return StringUtils.substringAfterLast(entity.name(), ".");
    }

    private List<CodeOccurrence> searchForDeadParameters(Database db) {
        List<CodeOccurrence> occurrences = new ArrayList<>();
        Entity[] classes = db.ents("type ~interface ~unresolved ~unknown");
        for (Entity clazz : classes) {
            for (Reference classRef : clazz.refs("~unresolved ~unknown", "method", true)) {
                Entity method = classRef.ent();
                if (hasDefinition(method) && definitionsOnSameLine(clazz, method)) {
                    continue;
                }

                for (Reference methodRefs : method.refs("~unresolved ~unknown ~catch", "parameter", true)) {
                    Entity param = methodRefs.ent();
                    if (isEntityUsed(param)) {
                        continue;
                    }

                    CodeOccurrenceType type = CodeOccurrenceType.DEAD_PARAMETER;
                    String paramName = param.name();
                    Reference definitionReference = getDefinitionReference(param);
                    occurrences.add(buildCodeOccurrence(type, paramName, definitionReference));
                }
            }
        }
        return occurrences;
    }

    private List<CodeOccurrence> searchForDeadVariables(Database db) {
        List<CodeOccurrence> occurrences = new ArrayList<>();
        Entity[] privateVariables = db.ents("variable private");
        for (Entity var : privateVariables) {
            if (isEntityUsed(var)) {
                continue;
            }

            CodeOccurrenceType type = CodeOccurrenceType.DEAD_VARIABLE;
            String pureMemberName = getPureEntityName(var);
            Reference definitionReference = getDefinitionReference(var);
            occurrences.add(buildCodeOccurrence(type, pureMemberName, definitionReference));
        }
        return occurrences;
    }

    private CodeOccurrence buildCodeOccurrence(CodeOccurrenceType type, String name, Reference definitionReference) {
        String filename = definitionReference.file().longname(true);
        int line = definitionReference.line();
        int columnFrom = definitionReference.column() + 1;
        int columnTo = columnFrom + name.length() - 1;
        return new CodeOccurrence(type, filename, name, line, columnFrom, columnTo);
    }

    private boolean hasDefinition(Entity ent) {
        return getDefinitionReference(ent) != null;
    }

    private boolean definitionsOnSameLine(Entity ent1, Entity ent2) {
        return getDefinitionReference(ent1).line() == getDefinitionReference(ent2).line();
    }

    private boolean isEntityUsed(Entity ent) {
        return ent.refs("useby", null, false).length > 0;
    }

    private boolean isEntityCalled(Entity ent) {
        return ent.refs("callby", null, false).length > 0;
    }

    private Reference getDefinitionReference(Entity ent) {
        Reference[] defineInRefs = ent.refs("definein", null, false);
        return defineInRefs.length > 0 ? defineInRefs[0] : null;
    }

    private void makeFilePathsRelative(String sourcesDirPath, List<CodeOccurrence> deadCodeOccurrences) {
        int sourcesDirPathLength = sourcesDirPath.length();
        for (CodeOccurrence co : deadCodeOccurrences) {
            String relativeFilePath = co.getFile().substring(sourcesDirPathLength);
            co.setFile(relativeFilePath);
        }
    }
}
