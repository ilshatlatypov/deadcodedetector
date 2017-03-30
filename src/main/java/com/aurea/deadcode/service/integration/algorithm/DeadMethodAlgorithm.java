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
public class DeadMethodAlgorithm extends Algorithm {
    @Override
    public List<CodeOccurrence> perform(Database db) {
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
}
