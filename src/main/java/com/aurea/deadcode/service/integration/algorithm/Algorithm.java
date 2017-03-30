package com.aurea.deadcode.service.integration.algorithm;

import com.aurea.deadcode.model.CodeOccurrence;
import com.aurea.deadcode.model.CodeOccurrenceType;
import com.scitools.understand.Database;
import com.scitools.understand.Entity;
import com.scitools.understand.Reference;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by ilshat on 30.03.17.
 */
public abstract class Algorithm {

    public abstract List<CodeOccurrence> perform(Database db);

    boolean isLambda(Entity methodEntity) {
        return getPureEntityName(methodEntity).startsWith("(");
    }

    String getPureEntityName(Entity entity) {
        return StringUtils.substringAfterLast(entity.name(), ".");
    }

    CodeOccurrence buildCodeOccurrence(CodeOccurrenceType type, String name, Reference definitionReference) {
        String filename = definitionReference.file().longname(true);
        int line = definitionReference.line();
        int columnFrom = definitionReference.column() + 1;
        int columnTo = columnFrom + name.length() - 1;
        return new CodeOccurrence(type, filename, name, line, columnFrom, columnTo);
    }

    boolean hasDefinition(Entity ent) {
        return getDefinitionReference(ent) != null;
    }

    boolean definitionsOnSameLine(Entity ent1, Entity ent2) {
        return getDefinitionReference(ent1).line() == getDefinitionReference(ent2).line();
    }

    boolean isEntityUsed(Entity ent) {
        return ent.refs("useby", null, false).length > 0;
    }

    boolean isEntityCalled(Entity ent) {
        return ent.refs("callby", null, false).length > 0;
    }

    Reference getDefinitionReference(Entity ent) {
        Reference[] defineInRefs = ent.refs("definein", null, false);
        return defineInRefs.length > 0 ? defineInRefs[0] : null;
    }
}
