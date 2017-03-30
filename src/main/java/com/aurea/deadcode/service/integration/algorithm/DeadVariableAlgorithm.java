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
public class DeadVariableAlgorithm extends Algorithm {
    @Override
    public List<CodeOccurrence> perform(Database db) {
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
}
