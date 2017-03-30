package com.aurea.deadcode.service.integration.algorithm;

import com.aurea.deadcode.model.CodeOccurrence;
import com.aurea.deadcode.model.CodeOccurrenceType;
import com.scitools.understand.Database;
import com.scitools.understand.Entity;
import com.scitools.understand.Reference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ilshat on 30.03.17.
 */
public class DeadParameterAlgorithm extends Algorithm {

    @Override
    public List<CodeOccurrence> perform(Database db) {
        List<CodeOccurrence> occurrences = new ArrayList<>();
        Entity[] classes = db.ents("type ~interface ~unresolved ~unknown");
        for (Entity clazz : classes) {
            for (Reference classRef : clazz.refs("~unresolved ~unknown", "method", true)) {
                Entity method = classRef.ent();
                if (hasDefinition(method) && definitionsOnSameLine(clazz, method)) {
                    continue;
                }
                occurrences.addAll(processMethodParameters(method));
            }
        }
        return occurrences;
    }

    private List<CodeOccurrence> processMethodParameters(Entity method) {
        List<CodeOccurrence> occurrences = new ArrayList<>();
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
        return occurrences;
    }
}
